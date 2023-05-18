/**
 * This abstract class is used to keep the general functionality of all entities
 * @author Linus Magnusson
 * @author Simon Sandén
 */

package entity.player;
//Imports from within project
import entity.enemy.Enemy;
import entity.enemy.EnemyManager;
//Imports from Javas library
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
//Imports of static variables and methods
import static utils.AssistanceMethods.*;
import static utils.Constants.EntityConstants.AIR_SPEED_OFFSET;
import static utils.Constants.EntityConstants.MAX_AIR_SPEED;
import static utils.Constants.GameConstants.*;
import static utils.Constants.StartPlayerConstants.*;

public abstract class Player {
    //Floats
    protected float x;
    protected float y;
    protected float airSpeed;
    protected float gravity;
    protected float jumpSpeed;
    protected float fallSpeedAfterCollision;
    protected float horizontalSpeed;
    protected float xDrawOffset = PLAYER_X_DRAW_OFFSET;
    protected float yDrawOffset = PLAYER_Y_DRAW_OFFSET;
    protected float rightPlayerSpeed = PLAYER_SPEED;
    protected float leftPlayerSpeed = -PLAYER_SPEED;
    protected float knockbackSpeed;
    protected final float rightPushSpeed = 0.6f;
    protected final float leftPushSpeed = -0.6f;

    //Ints
    protected int width;
    protected int height;
    protected int maxHealth;
    protected int currentHealth;
    protected int attackDamage;
    protected int animationIndex;
    protected int animationTick;
    protected int animationSpeed = 30;
    protected int entityState = IDLE;
    protected int[][] levelData;
    protected int flipX = 0;
    protected int flipW = 1;
    protected int facingDirection;

    // Shapes
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected Rectangle2D.Float boxAttackBox;

    //Booleans
    protected boolean inAir = false;
    protected boolean jumping = false;
    protected boolean isMoving = false;
    protected boolean isHit;
    protected boolean movingLeft;
    protected boolean movingRight;
    protected boolean isPushing;
    protected boolean standingOnInteractable;
    protected boolean stuckInBoxRight;
    protected boolean stuckInBoxLeft;
    protected boolean jumpOnce;
    protected boolean canAttack = true;


    protected BufferedImage[][] playerAnimations;
    protected EnemyManager enemyManager;

    protected AttackTimer attackTimer;
    protected Enemy attackingEnemy;





    /**
     * Second constructor initialises more variables
     * @param x
     * @param y
     * @param width
     * @param height
     * @param maxHealth
     * @param attackDamage
     */
    public Player(float x, float y, int width, int height, int maxHealth, int attackDamage, EnemyManager enemyManager){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        initialiseVariables(maxHealth, attackDamage, enemyManager);
    }

    //*************************************
    //*********** INITIALISING ************
    //*************************************

    /**
     * This method initialises the variables of the entity
     * @param maxHealth
     * @param attackDamage
     */
    protected void initialiseVariables(int maxHealth, int attackDamage, EnemyManager enemyManager){
        airSpeed = 0f;
        gravity = 0.03f * SCALE;
        jumpSpeed = -1.65f * SCALE;
        fallSpeedAfterCollision = 0.5f * SCALE;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.enemyManager = enemyManager;
    }

    /**
     * This method initializes the hitbox of the entity
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected void initialiseHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y,width,height);
    }

    /**
     * This method initialises the attackbox of the entity
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected void initialiseAttackBox(float x, float y, float width, float height){
        attackBox = new Rectangle2D.Float(x, y, width, height);
    }

    protected void initialiseBoxAttackBox(float x, float y, float width, float height){
        boxAttackBox = new Rectangle2D.Float(x, y, width, height);
    }


    //*************************************
    //*********** MOVEMENT ****************
    //*************************************

    /**
     *  This method updates the players position and checks for collisions
     */
    protected void updateEntityPosition(int [][] levelData) {
        isMoving = false;
        checkIfPlayerIsJumping();
        if(checkIfPlayerIsMoving() == true){
            return;
        }

        changeMovingDirection();
        knockbackPlayerIfHit();
        checkIfPlayerIsStandingOnInteractable(levelData);
        moveEntity(levelData);
        isMoving = true;
    }

    /**
     * This method checks if the player is currently jumping
     */
    protected void checkIfPlayerIsJumping(){
        if(jumping == true){
            jump();
        }
    }

    /**
     * this method makes the player jump
     */
    protected void jump() {
        if(inAir == true){
            return;
        } else if (jumpOnce == true){
            checkIfPlayerIsPushing();
        }
    }

    /**
     * This method checks if the player is pushing something, if not
     * it allows the player to jump
     */
    protected void checkIfPlayerIsPushing(){
        if(isPushing == false){
            inAir = true;
            airSpeed = jumpSpeed;
            jumpOnce = false;
        }
    }

    /**
     * This method checks if the player is moving in any way
     * @return
     */
    protected boolean checkIfPlayerIsMoving() {
        return movingLeft == false && movingRight == false && inAir == false && isHit == false;
    }

    /**
     * This method changes the moving direction for the player and calls the methods
     * to flip the player the same way the player is moving
     */
    protected void changeMovingDirection(){
        if(movingLeft == true){
            flipPlayerLeft();
        } else if (movingRight == true) {
            flipPlayerRight();
        }
    }

    /**
     * This method flips the player to the left
     */
    protected void flipPlayerLeft(){
        if (!isPushing) {
            horizontalSpeed = leftPlayerSpeed;
        }
        else {
            if (stuckInBoxLeft == false) {
                horizontalSpeed = leftPushSpeed;
            }
            else {
                horizontalSpeed = 0;
            }
        }
        flipX = width;
        flipW = -1;
        facingDirection = 0;
        isPushing = false;
    }

    /**
     * This method flips the player to the rigth
     */
    protected void flipPlayerRight(){
        if (!isPushing) {
            horizontalSpeed = rightPlayerSpeed;
        }
        else {
            if (stuckInBoxRight == false) {
                horizontalSpeed = rightPushSpeed;
            }
            else {
                horizontalSpeed = 0;
            }
        }
        flipX = 0;
        flipW = 1;
        facingDirection = 1;
        isPushing = false;
    }

    /**
     * This method calls the method for knocking the player back if
     * the player is hit
     */
    protected void knockbackPlayerIfHit(){
        if(isHit == true){
            knockbackPlayer(attackingEnemy);
        }
    }

    /**
     * This method knocks the player back
     * @param enemy
     */
    public void knockbackPlayer(Enemy enemy){
        horizontalSpeed = 0;
        getKnockbackDirection(enemy);
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
            hitbox.x += horizontalSpeed;
        }
    }

    /**
     * This method gets the knockback direction for the player
     * @param enemy
     */
    protected void getKnockbackDirection(Enemy enemy){
        if(hitbox.x < enemy.getHitbox().x){
            setHorizontalKnockbackSpeed(PLAYER_KNOCKBACK_LEFT);
        } else if (hitbox.x > enemy.getHitbox().x){
            setHorizontalKnockbackSpeed(PLAYER_KNOCKBACK_RIGHT);
        }
    }

    /**
     * This method checks if the player is standing on an interactable
     * @param levelData
     */
    protected void checkIfPlayerIsStandingOnInteractable(int[][] levelData){
        if(standingOnInteractable == false){
            checkIfPlayerIsInAirWhenNotJumping(levelData);
        }
    }

    /**
     * This method checks if the player is in the air if the player is not jumping
     * @param levelData
     */
    protected void checkIfPlayerIsInAirWhenNotJumping(int[][] levelData){
        if(inAir == false ){
            isEntityInAir(levelData);
        }
    }

    /**
     * This method checks if the entity is in air and changes the boolean to true or false
     * @param levelData
     */
    protected void isEntityInAir(int[][] levelData){
        if(IsEntityOnFloor(hitbox, levelData) == false){
            jump();
            setAirSpeed(0.1f);
            resetBooleanJumpOnce();
            inAir = true;
        }
    }

    /**
     * This method is used to move the entity with all the checks that is needed to be done
     * Makes the physics and collisions work
     * @param levelData
     */
    protected void moveEntity(int[][] levelData) {
        if (inAir == true) {
            checkIfEntityCanMoveInAir(levelData);
        } else {
            updateEntityXPosition(horizontalSpeed, levelData);
        }
    }

    /**
     * This method checks if the entity can move to the new tile when in air
     * @param levelData
     */
    protected void checkIfEntityCanMoveInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed - AIR_SPEED_OFFSET, hitbox.width, hitbox.height, levelData) == true) {
            hitbox.y += airSpeed;
            changeAirSpeed();
            updateEntityXPosition(horizontalSpeed, levelData);
        } else {
            checkIfCollidingWithRoofOrFloor();
            updateEntityXPosition(horizontalSpeed, levelData);
        }
    }

    /**
     * This method changes the airspeed of the entity
     */
    protected void changeAirSpeed(){
        if(airSpeed < MAX_AIR_SPEED){
            airSpeed += gravity;
        }
    }

    /**
     * This method updates the x position of the entity by taking in the speed and leveldata to check
     * if the move is valid
     * @param horizontalSpeed
     * @param levelData
     */
    protected void updateEntityXPosition(float horizontalSpeed, int [][] levelData) {
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
            hitbox.x += horizontalSpeed;
        } else {
            if (movingLeft) {
                hitbox.x = GetEntityXPosNextToWall(hitbox);
            }
            else {
                hitbox.x = GetEntityXPosNextToWall(hitbox) + 18; //18 är antalet X-koordinater den måste lägga till för att inte studsa tillbaka
            }
        }
    }

    /**
     * This method changes the airspeed if the entity is colliding with the roof
     */
    protected void checkIfCollidingWithRoofOrFloor(){
        hitbox.y = GetEntityYPosUnderOrAboveTile(hitbox, airSpeed);
        if (airSpeed > 0) {
            resetBooleanInAir();
        } else {
            airSpeed = fallSpeedAfterCollision;
        }
    }

    public void resetStuckBooleans(){
        stuckInBoxLeft = false;
        stuckInBoxRight = false;
    }


    //*************************************
    //*********** ATTACKING ***************
    //*************************************

    public abstract void attack();

    public void attackWithBox(){
        enemyManager.checkIfEnemyIsHitByBox(boxAttackBox);
    }

    /**
     * This method starts the attack cooldown
     */
    protected void startAttackCooldown(){
        canAttack = false;
        attackTimer = new Start_Player.AttackTimer();
        attackTimer.start();
    }

    //TODO Flytta till enemy, annars om 2 fiender skadar kan man inte ta damage samtidigt
    /**
     * This method sets the variables for when the player is hit
     * @param attackingEnemy
     */
    public void playerHit(Enemy attackingEnemy){
        this.attackingEnemy = attackingEnemy;
        isHit = true;
        HitTimer ht = new HitTimer();
        ht.start();
    }


    //*************************************
    //************** HEALTH ***************
    //*************************************

    /**
     * This method makes the enemy take damage
     * @param damage
     */
    public void entityTakeDamage(int damage){
        currentHealth -= damage;
    }

    /**
     * This method checks if the entity is dead and returns a boolean accordingly
     * @return
     */
    public boolean isEntityDead(){
        if(currentHealth <= 0){
            return true;
        }
        return false;
    }



    //*************************************
    //*********** RENDERING ***************
    //*************************************

    /**
     * This method renders the player
     * @param g
     */
    public void renderPlayer(Graphics g, int levelOffset) {

        g.drawImage(playerAnimations[entityState][animationIndex],
                (int) (hitbox.x - xDrawOffset) + flipX - levelOffset,
                (int) (hitbox.y - yDrawOffset),
                width * flipW,
                height, null);
        //drawHitbox(g, levelOffset); //For debugging purposes
        //drawAttackBox(g, levelOffset); //For debugging purposes
        //drawBoxAttackBox(g, levelOffset); //For debugging purposes
    }

    //For Debugging boxAttackBox
    protected void drawAttackBox(Graphics g,int levelOffset){
        g.setColor(Color.GREEN);
        g.drawRect((int)(attackBox.x - levelOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    //For Debugging box
    protected void drawBoxAttackBox(Graphics g,int levelOffset){
        g.setColor(Color.GREEN);
        g.drawRect((int)(boxAttackBox.x - levelOffset), (int) boxAttackBox.y, (int) boxAttackBox.width, (int) boxAttackBox.height);
    }

    //For Debugging hitbox
    protected void drawHitbox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x - levelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }



    //*************************************
    //******* LOADING AND UPDATES *********
    //*************************************

    /**
     * This method loads the players animations into a 2d array of bufferedimages
     */
    public void loadPlayerAnimations(String pngName) {

        InputStream is = getClass().getResourceAsStream(pngName);
        try {
            BufferedImage player = ImageIO.read(is);
            playerAnimations = new BufferedImage[5][8];
            for (int i = 0; i < playerAnimations.length; i++) {
                for (int j = 0; j < playerAnimations[i].length; j++) {
                    playerAnimations[i][j] = player.getSubimage(j * 64, i * 64, 64,64 );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method loads the leveldata into the player
     * @param levelData
     */
    public void loadLvlData(int [][] levelData){
        this.levelData = levelData;
        if(IsEntityOnFloor(hitbox,levelData) == false){
            inAir = true;
        }
    }

    public abstract void update();

    /**
     * This method updates the animationtick to keep track of which stage of the animation
     * is the next
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(entityState)) {
                animationIndex = 0;
                animationTick = 0;
            }
        }
    }

    /**
     * This method updates the x and yPosition of the attackbox
     * @param xOffset
     * @param facingDirection
     */
    protected void updateAttackBox(int xOffset, int facingDirection){
        if(facingDirection == 0){
            attackBox.x = hitbox.x - xOffset;
            attackBox.y = hitbox.y;
        } else if (facingDirection == 1){
            attackBox.x = hitbox.x + xOffset;
            attackBox.y = hitbox.y;
        }
    }

    protected void updateBoxAttackBox(int xOffset, int facingDirection){
        if(facingDirection == 0){
            //Vänster
            boxAttackBox.x = hitbox.x - (xOffset + 500);
            boxAttackBox.y = hitbox.y;
        } else if (facingDirection == 1){
            //Höger
            boxAttackBox.x = hitbox.x + xOffset;
            boxAttackBox.y = hitbox.y;
        }
    }


    //*************************************
    //******* GETTERS AND SETTERS *********
    //*************************************

    /**
     * This method sets the horixontal speed of the player
     * @param knockbackSpeed
     */
    protected void setHorizontalKnockbackSpeed(float knockbackSpeed) {
        horizontalSpeed = knockbackSpeed;
        hitbox.x += horizontalSpeed;
    }

    /**
     * This method sets the variables for the player if the player is standing on an interactabel object
     */
    public void setPlayerStandingOnInteractable(){
        if(getStandingOnInteractable() == false){
            setStandingOnInteractable(true);
        }
        resetBooleanInAir();
    }

    /**
     * This method is used to set the inAir variable to false when
     * the entity is no longer in the air
     */
    protected void resetBooleanInAir() {
        inAir = false;
        airSpeed = 0;
        if (isMoving) {
            entityState = RUNNING;
        }
        else {
            entityState = IDLE;
        }
    }

    /**
     * This method resets the variables for the animations
     */
    protected void resetAnimationTick(){
        animationTick = 0;
        animationIndex = 0;
    }

    /**
     * This method sets the player animation based on current state
     */
    protected void setEntityAnimation() {

        int startAnimation = entityState;
        if(isHit == true){
            entityState = HIT;
        }
       /* else if (isPushing == true){
            entityState = PUSHING;
        } */
        else if (isMoving == true) {
            entityState = RUNNING;
        }
        else {
            entityState = IDLE;
            horizontalSpeed = 0;
            isPushing = false;
        }

        if(inAir == true) {
            entityState = JUMP;
        }

        if (startAnimation != entityState){
            resetAnimationTick();
        }
    }

    /**
     * This method sets the spawnpoint for the player
     * @param spawn
     */
    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * This method resets the variable jumponce to make the player able to jump again
     */
    public void resetBooleanJumpOnce(){
        this.jumpOnce = true;
    }

    /**
     * This method returns the hitbox of the entity
     * @return
     */
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public float getRightPlayerSpeed(){
        return rightPlayerSpeed;
    }

    public float getLeftPlayerSpeed(){
        return leftPlayerSpeed;
    }

    public void setRightPlayerSpeed(float speed){
        rightPlayerSpeed = speed;
    }

    public void setLeftPlayerSpeed(float speed){
        leftPlayerSpeed = speed;
    }

    public void setStandingOnInteractable(boolean b){
        this.standingOnInteractable = b;
    }

    public boolean getStandingOnInteractable(){
        return standingOnInteractable;
    }

    public boolean isPushing() {
        return isPushing;
    }

    public void setPushing(boolean pushing) {
        isPushing = pushing;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isInAir() {
        return inAir;
    }

    public int getEntityState() {
        return entityState;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
    public void setEntityState(int entityState){
        this.entityState = entityState;
    }

    public void setHorizontalSpeed(float horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setAirSpeed(float airSpeed){this.airSpeed = airSpeed;}
    public float getAirSpeed() {return airSpeed;}

    public boolean isStuckInBoxRight() {return stuckInBoxRight;}
    public void setStuckInBoxRight(boolean stuckInBoxRight) {this.stuckInBoxRight = stuckInBoxRight;}
    public boolean isStuckInBoxLeft() {return stuckInBoxLeft;}
    public void setStuckInBoxLeft(boolean stuckInBoxLeft) {this.stuckInBoxLeft = stuckInBoxLeft;}

    /**
     * This method sets all moving booleans to false
     */
    public void allMovingBooleansFalse() {
        setMovingRight(false);
        setMovingLeft(false);
        isMoving = false;
    }

    //*************************************
    //************* THREADS ***************
    //*************************************

    /**
     * This class is responsible for setting a cooldown for how many times you can hit an enemy per second
     */
    protected class HitTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                isHit = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This class is responsible for setting a cooldown for how many times you can attack per second
     */
    protected class AttackTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(300);
                canAttack = true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
