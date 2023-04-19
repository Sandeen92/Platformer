package entity;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static utils.Constants.GameConstants.*;
import static utils.Constants.Directions.LEFT;


public class Box extends Entity{

    public static float moveSpeed = 0.6f;
    private BufferedImage box;
    private Player player;
    private boolean firstUpdate;
    public Box(int x, int y, int width, int height){
        super(x,y,width,height);
        airSpeed = 0f;
        gravity = 0.03f * SCALE;
        jumpSpeed = -2.25f * SCALE;
        fallSpeedAfterCollision = 0.5f * SCALE;
        loadImg();
        initialiseHitbox(x,y,width,height);
        firstUpdate = true;
    }

    public void initPlayerToBox(Player player){
        this.player = player;
    }

    public void checkPlayerHit(Box box, Player player){
        xSpeed = 0;
        if(box.hitbox.intersects(player.hitbox)){
            if((hitbox.y < player.hitbox.y+height)){
                if(hitbox.x > player.hitbox.x){
                    xSpeed += moveSpeed;
                } else if (hitbox.x < player.hitbox.x){
                    xSpeed -= moveSpeed;
                }
                player.setSpeed(moveSpeed);
                player.setPushing(true);
            } else if(hitbox.y -1 > player.hitbox.y+height){
                if(player.getStandingOnInteractable() == false){
                    player.setStandingOnInteractable(true);

                    System.out.println("Set interactable sltanding true");
                }
                player.resetInAir();
            }
        } else if (player.getPlayerSpeed() != 1.2 || player.getStandingOnInteractable() == true){
            player.setSpeed(1.2f);
            player.setStandingOnInteractable(false);
            player.setPushing(false);
        }
    }

    public void checkIfEnemyIsHit(ArrayList<Crabby> crabbies){
        for(Crabby c : crabbies){
            if (hitbox.intersects(c.hitbox)) {
                if(c.getWalkDir() == LEFT) {
                       c.hitbox.x += 3;
                } else {
                    c.hitbox.x -= 3;
                }
                c.changeWalkDir();
            }
        }
    }

    @Override
    protected void updateEntityPos(int[][] lvldata) {
        if(firstUpdate){
            isEntityInAir(lvldata);
            firstUpdate = false;
        }

        if(!inAir){
            isEntityInAir(lvldata);
        }
        moveEntity(lvldata);
        isMoving = true;
    }

    public void update(int[][] lvlData){
        updateEntityPos(lvlData);
    }



    private void loadImg() {
        InputStream is = getClass().getResourceAsStream("/Box_dark.png");
        try{
            box = ImageIO.read(is);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int xOffset){
        g.drawImage(box, (int) hitbox.x- xOffset, (int) hitbox.y+1, (int) width, (int) height, null);
        //drawHitbox(g,xOffset);
    }
}
