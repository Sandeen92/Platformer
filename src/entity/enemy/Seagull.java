package entity.enemy;

import entity.player.Player;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.SCALE;

public class Seagull extends Enemy{
   private Rectangle2D.Float visionBox;
   private Player player;
   private float xSpeed;
   private float ySpeed;
   public Image seagullImage;

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
        seagullImage = LoadSave.GetImage(SEAGULL_IMAGE);
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
        }
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
        if(hitbox.y < player.getHitbox().y){
            ySpeed = SEAGULL_Y_SPEED;
        } else if(hitbox.y > player.getHitbox().y){
            ySpeed = -SEAGULL_Y_SPEED;
        }
        if(hitbox.x < player.getHitbox().x){
            xSpeed = SEAGULL_X_SPEED;
        } else if(hitbox.x > player.getHitbox().x){
            xSpeed = -SEAGULL_X_SPEED;
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
}
