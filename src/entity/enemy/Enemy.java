/**
 * This abstract class is responsible for the general functions of the enemies
 * @author Linus Magnusson
 */

package entity.enemy;

import entity.player.Entity;
import entity.player.Player;
import gamestates.Gamestate;

import java.awt.geom.Rectangle2D;

import static utils.AssistanceMethods.canMoveHere;
import static utils.AssistanceMethods.IsFloor;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.HIT;
import static utils.Constants.Directions.*;

public abstract class Enemy extends Entity {
    private int enemyType;
    private final float patrolSpeed = RAT_PATROL_SPEED;
    private boolean firstUpdate = true;
    private int walkDirection = LEFT;
    private boolean canAttack = true;
    private AttackCooldownThread attackCooldownTimer;
    private int flipX = 0;
    private int flipW = 1;


    /**
     * Constructor for enemy
     * @param x
     * @param y
     * @param width
     * @param height
     * @param enemyType
     */
    public Enemy(float x, float y, int width, int height, int enemyType, int maxHealth, int attackDamage) {
        super(x, y, width, height, maxHealth, attackDamage);
        this.enemyType = enemyType;
    }

    /**
     * This metod checks if the player is hit by the Enemy by checking if the player hitbox
     * and enemy attackbox intersects
     * @param enemy
     * @param player
     */
    protected void checkIfPlayerIsHit(Enemy enemy, Player player){
        if(enemy.attackBox.intersects(player.hitbox) == true){
            if(canAttack == true){
                attackPlayer(enemy, player);
            }
            changePlayerToHit(enemy,player);
        }
    }

    /**
     * This method makes the enemy attack the player and calls for the cooldown to start
     * @param enemy
     * @param player
     */
    private void attackPlayer(Enemy enemy, Player player){
        player.currentHealth -= enemy.attackDamage;
        checkIfPlayerIsDead(player);
        canAttack = false;
        startAttackCooldown();
    }

    /**
     * This method checks if the player is dead and if the player
     * is dead the gamestate switches to DeathScreen
     * @param player
     */
    private void checkIfPlayerIsDead(Player player){
        if(player.isEntityDead() == true) {
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
     * @param player
     */
    private void changePlayerToHit(Enemy enemy, Player player){
        player.playerHit(enemy);
        player.entityState = HIT;
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

    /**
     * This method updates the enemy movement and checks for collisions and invalid moves
     * @param levelData
     */
    @Override
    protected void updateEntityPosition(int[][] levelData) {
        isEntityInAir(levelData);
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
