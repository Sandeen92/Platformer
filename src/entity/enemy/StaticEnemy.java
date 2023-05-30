package entity.enemy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.EnemyConstants.*;

public class StaticEnemy extends Enemy{
   private BufferedImage[] staticEnemyImages;
   public boolean isActive = false;
   public StaticEnemyTimer staticEnemyTimer;
    /**
     * Constructor for enemy
     *
     * @param x
     * @param y
     */
    public StaticEnemy(float x, float y, int enemyType) {
        super(x, y, STEAM_WIDTH, STEAM_HEIGHT, enemyType, STEAM_HEALTHPOINTS, STEAM_ATTACKDAMAGE);
        initialiseHitbox(x,y,STEAM_WIDTH,STEAM_HEIGHT);
        initialiseAttackBox(x,y,STEAM_WIDTH, STEAM_HEIGHT);
        chooseLoadAnimation();
        initialiseVariables();
    }

    public void initialiseVariables(){
        staticEnemyTimer = new StaticEnemyTimer();
        staticEnemyTimer.start();
    }

    public void update(){
    updateAnimationTick();
    }

    private void chooseLoadAnimation(){
        switch (enemyType){
            case STEAM:
                loadAnimations(STEAM_IMAGE);
                break;
            case FIRE:
                loadAnimations(FIRE_IMAGE);
                break;
            case WATER:
                loadAnimations(WATER_IMAGE);
                break;
        }
    }

    private void loadAnimations(String fileName){
        InputStream is = getClass().getResourceAsStream(fileName);
        try {
            BufferedImage staticEnemy = ImageIO.read(is);
            staticEnemyImages = new BufferedImage[14];
            for (int i = 0; i < staticEnemyImages.length; i++) {
                staticEnemyImages[i] = staticEnemy.getSubimage(i * 28, 0, 28, 50);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage[] getStaticEnemyImages(){
        return staticEnemyImages;
    }


    /**
     * This method updates the animationtick to keep track of the animation
     */
    protected void updateAnimationTick(){
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 5) {
                animationIndex = 0;
            }
        }
    }


    private class StaticEnemyTimer extends Thread{

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(isActive == false){
                    isActive = true;
                } else {
                    isActive = false;
                }
            }
        }
    }





}
