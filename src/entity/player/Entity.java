/**
 * This abstract class is used to keep the general functionality of all entities
 * @author Linus Magnusson
 */

package entity.player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import static utils.AssistanceMethods.*;
import static utils.Constants.EntityConstants.MAX_AIR_SPEED;
import static utils.Constants.GameConstants.*;
import static utils.Constants.PlayerConstants.*;

public abstract class Entity {
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected int maxHealth;
    protected int currentHealth;
    protected int attackDamage;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected int animationIndex;
    protected int animationTick;
    protected int animationSpeed = 30;
    protected int entityState = IDLE;
    protected float airSpeed;
    protected float gravity;
    protected float jumpSpeed;
    protected float fallSpeedAfterCollision;
    protected boolean inAir = false;
    protected boolean jumping = false;
    protected boolean isMoving = false;
    protected boolean isHit;
    protected float horizontalSpeed;
    protected boolean movingLeft;
    protected boolean movingRight;


    /**
     * Constructor for the entity-class initializes all the important variables
     */
    public Entity(float x, float y,int width, int height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    /**
     * Second constructor initialises more variables
     * @param x
     * @param y
     * @param width
     * @param height
     * @param maxHealth
     * @param attackDamage
     */
    public Entity(float x, float y,int width, int height, int maxHealth, int attackDamage){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        initialiseVariables(maxHealth, attackDamage);
    }

    /**
     * This method initialises the variables of the entity
     * @param maxHealth
     * @param attackDamage
     */
    private void initialiseVariables(int maxHealth, int attackDamage){
        airSpeed = 0f;
        gravity = 0.03f * SCALE;
        jumpSpeed = -1.65f * SCALE;
        fallSpeedAfterCollision = 0.5f * SCALE;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackDamage = attackDamage;
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

    //For Debugging hitbox
    protected void drawHitbox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x - levelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    //For Debugging attackBox
    protected void drawAttackBox(Graphics g,int levelOffset){
        g.setColor(Color.GREEN);
        g.drawRect((int)(attackBox.x - levelOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Abstract method that is used to handle logic of the movement of different
     * entities therefore it should be declared in each class
     * @param levelData
     */
    protected abstract void updateEntityPosition(int[][] levelData);

    /**
     * This method checks if the player is in the air and sets the boolean jump to true
     */
    protected void jump() {
        if(inAir == true){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
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
            hitbox.x = GetEntityXPosNextToWall(hitbox, horizontalSpeed);
        }
    }

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

    /**
     * This method is used to set the inAir variable to false when
     * the entity is no longer in the air
     */
    protected void resetBooleanInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * This method checks if the entity is in air and changes the boolean to true or false
     * @param levelData
     */
    protected void isEntityInAir(int[][] levelData){
        if(IsEntityOnFloor(hitbox, levelData) == false){
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
    private void checkIfEntityCanMoveInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData) == true) {
            hitbox.y += airSpeed;
            changeAirSpeed();
            updateEntityXPosition(horizontalSpeed, levelData);
        } else {
            changeAirSpeedIfCollidingWithRoof();
            updateEntityXPosition(horizontalSpeed, levelData);
        }
    }

    /**
     * This method changes the airspeed if the entity is colliding with the roof
     */
    private void changeAirSpeedIfCollidingWithRoof(){
        hitbox.y = GetEntityYPosUnderOrAboveTile(hitbox, airSpeed);
        if (airSpeed > 0) {
            resetBooleanInAir();
        } else {
            airSpeed = fallSpeedAfterCollision;
        }
    }

    /**
     * This method changes the airspeed of the entity
     */
    private void changeAirSpeed(){
        if(airSpeed < MAX_AIR_SPEED){
            airSpeed += gravity;
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
            }
        }
    }

    /**
     * This method sets the player animation based on current state
     */
    protected void setEntityAnimation() {

        int startAnimation = entityState;
        if(isHit == true){
            entityState = HIT;
        } else if (isMoving == true) {
            entityState = RUNNING;
        } else {
            entityState = IDLE;
        }

        if(inAir == true) {
            entityState = JUMP;
        }

        if (startAnimation != entityState){
            resetAnimationTick();
        }
    }

    /**
     * This method returns the hitbox of the entity
     * @return
     */
    public Rectangle2D.Float getHitbox(){
        return hitbox;
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
    public

    /**
     * This method sets all moving booleans to false
     */
    public void allMovingBooleansFalse() {
        setMovingRight(false);
        setMovingLeft(false);
        isMoving = false;
    }



}
