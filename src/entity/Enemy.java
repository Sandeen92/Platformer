package entity;

import static utils.Constants.EnemyConstants.*;

public abstract class Enemy extends Entity{
    private int enemyType;

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

    public void update(){
        updateAnimationTick();
    }

    @Override
    protected void updateEntityPos(int[][] lvldata) {

    }

    public int getAnimationIndex(){
        return animationIndex;
    }
    public int getEnemyState(){
        return entityState;
    }
}
