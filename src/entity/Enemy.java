/**
 * This abstract class is responsible for the general functions of the enemies
 * @author Linus Magnusson
 */

package entity;

import main.Game;
import static utils.AssistanceMethods.canMoveHere;
import static utils.AssistanceMethods.IsFloor;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.Directions.*;

public abstract class Enemy extends Entity{
    private int enemyType;
    private final float patrolSpeed = 0.3f * Game.SCALE;
    private boolean firstUpdate = true;
    private int walkDir = LEFT;

    /**
     * Constructor for enemy
     * @param x
     * @param y
     * @param width
     * @param height
     * @param enemyType
     */
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initialiseHitbox(x, y, width, height);
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
        updateAnimationTick();
    }

    /**
     * This method updates the enemy movement and checks for collisions and invalid moves
     * @param lvlData
     */
    @Override
    protected void updateEntityPos(int[][] lvlData) {
        if(firstUpdate){
            isEntityInAir(lvlData);
            firstUpdate = false;
        }
        moveEntity(lvlData);

        switch (entityState){
            case IDLE:
                entityState = RUNNING;
                break;
            case RUNNING:
                xSpeed = 0;
                setEnemyToPatrol();
                if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
                    if(IsFloor(hitbox, xSpeed, lvlData)){

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
    private void changeWalkDir() {
        if(walkDir == LEFT){
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    /**
     * This method makes the enemy start patrolling
     */
    public void setEnemyToPatrol(){
        if(walkDir == LEFT){
            xSpeed = -patrolSpeed;
        } else {
            xSpeed = patrolSpeed;
        }
    }

    public int getAnimationIndex(){
        return animationIndex;
    }

    public int getEnemyState(){
        return entityState;
    }
}
