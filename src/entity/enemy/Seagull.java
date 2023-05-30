package entity.enemy;

import entity.player.Player;
import utils.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.SCALE;
import static utils.LoadSave.SEAGULL_IMAGE;

public class Seagull extends Enemy{
   private Rectangle2D.Float visionBox;
   private Player player;
   private float xSpeed;
   private float ySpeed;
   public BufferedImage[] seagullAnimation;
   private boolean patrolDirection;
   private PatrolChanger patrolChanger;
    /**
     * Constructor for enemy
     *
     * @param x
     * @param y
     */
    public Seagull(float x, float y) {
        super(x, y, SEAGULL_WIDTH, SEAGULL_HEIGHT, SEAGULL, SEAGULL_HEALTHPOINTS, SEAGULL_ATTACKDAMAGE);
        initialiseSeagull(x, y);
    }

    private void initialiseSeagull(float x, float y){
        visionBox = new Rectangle2D.Float(x,y,1200,500);
        initialiseHitbox(x,y,20 * SCALE,15*SCALE);
        initialiseAttackBox(x,y,20 * SCALE,15 * SCALE);
        this.xSpeed = 0;
        this.ySpeed = 0;
        patrolDirection = false;
        patrolChanger = new PatrolChanger();
        patrolChanger.start();
        loadAnimations(SEAGULL_IMAGE);
    }

    protected void updateEntityPosition(int levelData[][]){
        moveEntity(levelData);
        updateVisionAndAttackBox();
    }

    /**
     * This method is used to move the enemy with all the checks that is needed to be done
     * Makes the physics and collisions work
     * @param levelData
     */
    protected void moveEntity(int[][] levelData) {
        if (player.getHitbox().intersects(visionBox)){
            updateSeagullPosition();
        } else {
            if(patrolDirection == false){
                hitbox.x += SEAGULL_X_SPEED;
                walkDirection = RIGHT;
            } else if(patrolDirection == true){
                hitbox.x -= SEAGULL_X_SPEED;
                walkDirection = LEFT;
            }
        }
        changeFacingDirection();
    }


    private void updateVisionAndAttackBox(){
        visionBox.x = hitbox.x - SEAGULL_HITBOX_X_OFFSET;
        visionBox.y = hitbox.y - SEAGULL_HITBOX_Y_OFFSET;
        attackBox = hitbox;
    }

    private void updateSeagullPosition(){
       checkWhichWayToMove();
       this.hitbox.y += ySpeed;
       this.hitbox.x += xSpeed;
    }

    private void checkWhichWayToMove(){
        if(hitbox.y < player.getHitbox().y + 10){
            ySpeed = SEAGULL_Y_SPEED;
        } else if(hitbox.y > player.getHitbox().y){
            ySpeed = -SEAGULL_Y_SPEED;
        }
        if(hitbox.x < player.getHitbox().x ){
            xSpeed = SEAGULL_X_SPEED;
            walkDirection = RIGHT;
        } else if(hitbox.x > player.getHitbox().x){
            xSpeed = -SEAGULL_X_SPEED;
            walkDirection = LEFT;
        }
    }


    private void loadAnimations(String fileName){
        InputStream is = getClass().getResourceAsStream(fileName);
        try {
            BufferedImage staticEnemy = ImageIO.read(is);
            seagullAnimation = new BufferedImage[8];
            for (int i = 0; i < seagullAnimation.length; i++) {
                seagullAnimation[i] = staticEnemy.getSubimage(i * 18, 0, 16, 16);
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


    /**
     * This method updates the animationtick to keep track of the animation
     */
    protected void updateAnimationTick(){
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 8) {
                animationIndex = 0;
            }
        }
    }


    //For Debugging hitbox
    protected void drawVisionBox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) visionBox.x - levelOffset, (int) visionBox.y, (int) visionBox.width, (int) visionBox.height);
    }

    protected void drawAttackBox(Graphics g, int levelOffset){
        g.setColor(Color.BLUE);
        g.drawRect((int) attackBox.x - levelOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private class PatrolChanger extends Thread{
        @Override
        public void run() {
           while(true){
               try {
                   Thread.sleep(4500);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               if(patrolDirection == false){
                   patrolDirection = true;
               } else {
                   patrolDirection = false;
               }
           }

        }
    }
}
