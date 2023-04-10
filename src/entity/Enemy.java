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

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initialiseHitbox(x, y, width, height);
    }

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

    public void update(int[][] lvlData){
        updateEntityPos(lvlData);
        updateAnimationTick();
    }

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

    private void changeWalkDir() {
        if(walkDir == LEFT){
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

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
