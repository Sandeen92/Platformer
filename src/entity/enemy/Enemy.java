/**
 * This abstract class is responsible for the general functions of the enemies
 * @author Linus Magnusson
 */

package entity.enemy;

import entity.player.Start_Player;
import gamestates.Gamestate;

import java.awt.geom.Rectangle2D;

import static utils.AssistanceMethods.*;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.EntityConstants.MAX_AIR_SPEED;
import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.PlayerConstants.HIT;
import static utils.Constants.Directions.*;

public abstract class Enemy{
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected float horizontalSpeed;
    protected int maxHealth;
    protected int currentHealth;
    protected int attackDamage;
    private EnemyManager enemyManager;
    private int enemyType;
    private final float patrolSpeed = RAT_PATROL_SPEED;
    private boolean firstUpdate = true;
    private int walkDirection = LEFT;
    private boolean canAttack = true;
    private AttackCooldownThread attackCooldownTimer;
    private int flipX = 0;
    private int flipW = 1;
    protected int animationIndex;
    protected int animationTick;
    protected int animationSpeed = 30;
    protected int entityState = IDLE;
    protected boolean inAir;
    protected float airSpeed;
    protected float gravity = 0.03f * SCALE;
    protected int timesEnemyChangedDirection = 0;


    /**
     * Constructor for enemy
     * @param x
     * @param y
     * @param width
     * @param height
     * @param enemyType
     */
    public Enemy(float x, float y, int width, int height, int enemyType, int maxHealth, int attackDamage) {
        initialiseVariables(x,y,width,height,enemyType,maxHealth,attackDamage);
    }

    private void initialiseVariables(float x, float y, int width, int height, int enemyType, int maxHealth, int attackDamage){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enemyType = enemyType;
        this.maxHealth = maxHealth;
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


    /**
     * This metod checks if the player is hit by the Enemy by checking if the player hitbox
     * and enemy attackbox intersects
     * @param enemy
     * @param startPlayer
     */
    protected void checkIfPlayerIsHit(Enemy enemy, Start_Player startPlayer){
        if(enemy.attackBox.intersects(startPlayer.getHitbox()) == true){
            if(canAttack == true){
                attackPlayer(enemy, startPlayer);
            }
            changePlayerToHit(enemy, startPlayer);
        }
    }

    /**
     * This method makes the enemy attack the player and calls for the cooldown to start
     * @param enemy
     * @param startPlayer
     */
    private void attackPlayer(Enemy enemy, Start_Player startPlayer){
        startPlayer.entityTakeDamage(enemy.attackDamage);
        checkIfPlayerIsDead(startPlayer);
        canAttack = false;
        startAttackCooldown();
    }

    /**
     * This method checks if the player is dead and if the player
     * is dead the gamestate switches to DeathScreen
     * @param startPlayer
     */
    private void checkIfPlayerIsDead(Start_Player startPlayer){
        if(startPlayer.isEntityDead() == true) {
            Gamestate.state = Gamestate.DEATHSCREEN;
        }
    }
    /**
     * This method starts the attack cooldown
     */
    private void startAttackCooldown(){
        attackCooldownTimer = new AttackCooldownThread();
        attackCooldownTimer.start();
    }

    /**
     * This method changes the players state to hit and calls for the playerHit method
     * @param enemy
     * @param startPlayer
     */
    private void changePlayerToHit(Enemy enemy, Start_Player startPlayer){
        startPlayer.playerHit(enemy);
        startPlayer.setEntityState(HIT);
    }

    /**
     * This method updates the animationtick to keep track of the animation
     */
    protected void updateAnimationTick(){
        animationTick++;
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(enemyType, entityState)){
                animationIndex = 0;
            }
        }
    }

    /**
     * This method updates the enemies
     * @param levelData
     */
    public void update(int[][] levelData){
        updateEntityPosition(levelData);
        updateAttackBox(0, 0);
        updateAnimationTick();
    }

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
     * This method checks if the enemy is in air and changes the boolean to true or false
     * @param levelData
     */

    protected void isEnemyInAir(int[][] levelData){
        if(IsEntityOnFloor(hitbox, levelData) == false){
            inAir = true;
        }
    }


    private void checkIfEntityCanMoveInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData) == true) {
            hitbox.y += airSpeed;
            changeAirSpeed();
            updateEnemyXPosition(horizontalSpeed, levelData);
        } else {
            updateEnemyXPosition(horizontalSpeed, levelData);
        }
    }

    private void changeAirSpeed(){
        if(airSpeed < MAX_AIR_SPEED){
            airSpeed += gravity;
        }
    }

    /**
     * This method is used to move the enemy with all the checks that is needed to be done
     * Makes the physics and collisions work
     * @param levelData
     */
    protected void moveEntity(int[][] levelData) {
        if (inAir == true) {
            checkIfEntityCanMoveInAir(levelData);
        } else {
            updateEnemyXPosition(horizontalSpeed, levelData);
        }
    }

    protected void updateEnemyXPosition(float horizontalSpeed, int [][] levelData) {
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
            hitbox.x += horizontalSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, horizontalSpeed);
        }
    }

    /**
     * This method updates the enemy movement and checks for collisions and invalid moves
     * @param levelData
     */
    protected void updateEntityPosition(int[][] levelData) {
        isEnemyInAir(levelData);
        moveEntity(levelData);

        switch (entityState){
            case IDLE:
                entityState = RUNNING;
                break;
            case RUNNING:
                horizontalSpeed = 0;
                setEnemyToPatrol();
                checkIfWalkDirectionShouldChange(levelData);
                break;
        }
    }

    /**
     * This method checks if the walkdirection of the enemy should change
     * @param levelData
     */
    private void checkIfWalkDirectionShouldChange(int[][] levelData){
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
            if(IsFloor(hitbox, horizontalSpeed, levelData) == true){
                return;
            }
        }
        changeFacingDirection();
    }

    /**
     * This method changes the enemy walkdirektion and sets an offset that helps the enemy not getting
     * stuck
     *
     */
    public void changeEnemyWalkDirection(){
        if(getWalkDirection() == LEFT) {
            hitbox.x += 3;
        } else {
            hitbox.x -= 3;
        }
        changeFacingDirection();
    }

    /**
     * This method changes the facing direction for the enemy
     */
    public void changeFacingDirection() {
        if(walkDirection == LEFT){
            walkDirection = RIGHT;
            flipX = 0;
            flipW = 1;
        } else {
            walkDirection = LEFT;
            flipX = width+20;
            flipW = -1;
        }
    }

    /**
     * This method makes the enemy start patrolling
     */
    public void setEnemyToPatrol(){
        if(walkDirection == LEFT){
            horizontalSpeed = -patrolSpeed;
        } else {
            horizontalSpeed = patrolSpeed;
        }
    }

    /**
     * This method makes the enemy take damage
     * @param damage
     */
    public void enemyTakeDamage(int damage){
        currentHealth -= damage;
    }

    public boolean isEntityDead(){
        if(currentHealth <= 0){
            return true;
        }
        return false;
    }

    public int getWalkDirection(){
       return walkDirection;
    }

    public int getAnimationIndex(){
        return animationIndex;
    }

    public int getEnemyState(){
        return entityState;
    }

    public int getFlipX() {
        return flipX;
    }

    public int getFlipW() {
        return flipW;
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getTimesEnemyChangedDirection() {
        return timesEnemyChangedDirection;
    }

    public void setTimesEnemyChangedDirection(int timesEnemyChangedDirection) {
        this.timesEnemyChangedDirection = timesEnemyChangedDirection;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void setEnemyManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    private class AttackCooldownThread extends Thread{
        /**
         * This thread is responsible for the attackCooldown
         * @author Linus Magnusson
         */
        @Override
        public void run() {
            try {
                Thread.sleep(1500);
                canAttack = true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
