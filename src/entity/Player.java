/**
 * This class is responsible for the players attributes and functions
 * @author Linus Magnusson
 */

package entity;

import main.Game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utils.AssistanceMethods.IsEntityOnFloor;

public class Player extends Entity {
    private BufferedImage[][] playerAnimations;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private int[][] levelData;
    private float playerSpeed = 1.2f;
    private int flipX = 0;
    private int flipW = 1;

    /**
     * Constructor for Player
     * @param x
     * @param y
     * @param width
     * @param heigth
     */
    public Player(float x, float y, int width, int heigth, int maxHealth, int attackDamage) {
        super(x, y, width, heigth, maxHealth, attackDamage);
        loadPlayerAnimations();
        initialiseHitbox(x,y, 20 * Game.SCALE, 27 * Game.SCALE);
    }

    /**
     * This method is responsible for updating the player
     */
    public void updatePlayer() {
        updateEntityPos(levelData);
        updateAnimationTick();
        setEntityAnimation();
    }

    /**
     * This method renders the player
     * @param g
     */
    public void renderPlayer(Graphics g, int levelOffset) {
        g.drawImage(playerAnimations[entityState][animationIndex],
                (int) (hitbox.x - xDrawOffset) + flipX - levelOffset,
                (int) (hitbox.y - yDrawOffset),
                width * flipW,
                height, null);
    }

    /**
     *  This method updates the players position and checks for collisions
     */
    protected void updateEntityPos(int [][] lvlData) {
        isMoving = false;

        if(jumping){
            jump();
        }
        if(!movingLeft && !movingRight && ! inAir){
            return;
        }

        xSpeed = 0;
        if(movingLeft){
            xSpeed -=playerSpeed;
            flipX = width;
            flipW = -1;
        } else if (movingRight){
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir){
            isEntityInAir(lvlData);
        }

        moveEntity(lvlData);
        isMoving = true;
    }

    /**
     * This method loads the players animations into a 2d array of bufferedimages
     */
    public void loadPlayerAnimations() {

        InputStream is = getClass().getResourceAsStream("/Test.png");
        try {
            BufferedImage player = ImageIO.read(is);
            playerAnimations = new BufferedImage[9][6];
            for (int i = 0; i < playerAnimations.length; i++) {
                for (int j = 0; j < playerAnimations[i].length; j++) {
                    playerAnimations[i][j] = player.getSubimage(j * 64, i * 40, 64, 40);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method loads the leveldata into the player
     * @param lvlData
     */
    public void loadLvlData(int [][] lvlData){
        this.levelData = lvlData;
        if(!IsEntityOnFloor(hitbox,lvlData)){
            inAir = true;
        }
    }
}
