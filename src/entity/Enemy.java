/**
 * This abstract class is responsible for the general functions of the enemies
 * @author Linus Magnusson
 */

package entity;

import gamestates.Gamestate;
import static utils.Constants.GameConstants.*;
import static utils.AssistanceMethods.canMoveHere;
import static utils.AssistanceMethods.IsFloor;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.HIT;
import static utils.Constants.Directions.*;

public abstract class Enemy extends Entity{
    private int enemyType;
    private final float patrolSpeed = 0.3f * SCALE;
    private boolean firstUpdate = true;
    private int walkDir = LEFT;
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
    protected void checkPlayerHit(Enemy enemy, Player player){
        if(enemy.attackBox.intersects(player.hitbox) == true){
            if(canAttack == true){
                enemyAttack(enemy, player);
            }
            changePlayerToHit(enemy,player);
        }
    }

    /**
     * This method makes the enemy attack the player and calls for the cooldown to start
     * @param enemy
     * @param player
     */
    private void enemyAttack(Enemy enemy, Player player){
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
     * @param lvlData
     */
    public void update(int[][] lvlData){
        updateEntityPos(lvlData);
        updateAttackBox(0, 0);
        updateAnimationTick();
    }

    /**
     * This method updates the enemy movement and checks for collisions and invalid moves
     * @param levelData
     */
    @Override
    protected void updateEntityPos(int[][] levelData) {
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
     * @param lvlData
     */
    private void checkIfWalkDirectionShouldChange(int[][] lvlData){
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData) == true){
            if(IsFloor(hitbox, horizontalSpeed, lvlData) == true){
                return;
            }
        }
        changeWalkDirection();
    }

    /**
     * This method changes the walking direction for the enemy
     */
    public void changeWalkDirection() {
        if(walkDir == LEFT){
            walkDir = RIGHT;
            flipX = 0;
            flipW = 1;
        } else {
            walkDir = LEFT;
            flipX = width+20;
            flipW = -1;
        }
    }

    /**
     * This method makes the enemy start patrolling
     */
    public void setEnemyToPatrol(){
        if(walkDir == LEFT){
            horizontalSpeed = -patrolSpeed;
        } else {
            horizontalSpeed = patrolSpeed;
        }
    }

    public int getWalkDir(){
       return walkDir;
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
