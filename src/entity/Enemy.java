/**
 * This abstract class is responsible for the general functions of the enemies
 * @author Linus Magnusson
 */

package entity;

import gamestates.Gamestate;
import main.Game;

import static utils.AssistanceMethods.canMoveHere;
import static utils.AssistanceMethods.IsFloor;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.HIT;
import static utils.Constants.Directions.*;

public abstract class Enemy extends Entity{
    private int enemyType;
    private final float patrolSpeed = 0.3f * Game.SCALE;
    private boolean firstUpdate = true;
    private int walkDir = LEFT;
    private boolean canAttack;
    private TimerThread timer;
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
        canAttack = true;
    }

    protected void checkPlayerHit(Enemy enemy, Player player){
        if(enemy.attackBox.intersects(player.hitbox)){
            if(canAttack){
                player.currentHealth -= enemy.attackDamage;
                if(player.isEntityDead()){
                    Gamestate.state = Gamestate.DEATHSCREEN;
                }
                canAttack = false;
                timer = new TimerThread();
                timer.start();
            }
            player.playerHit(enemy);
            player.entityState = HIT;
        }

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
     * @param lvlData
     */
    @Override
    protected void updateEntityPos(int[][] lvlData) {
            isEntityInAir(lvlData);

        moveEntity(lvlData);

        switch (entityState){
            case IDLE:
                entityState = RUNNING;
                break;
            case RUNNING:
                horizontalSpeed = 0;
                setEnemyToPatrol();
                if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
                    if(IsFloor(hitbox, horizontalSpeed, lvlData)){
                        return;
                    }
                }
                changeWalkDir();
                break;
        }

    }

    /**
     * This method changes the walking direction for the enemy
     */
    public void changeWalkDir() {
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

    public int getWalkDir(){
       return walkDir;
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

    private class TimerThread extends Thread{
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
