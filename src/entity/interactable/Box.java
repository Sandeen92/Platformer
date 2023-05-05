/**
 * This class is an interactable box that the player can move and jump on
 * @author Linus Magnusson
 */

package entity.interactable;

import entity.enemy.EnemyManager;
import entity.enemy.EnemyRat;
import entity.player.Start_Player;
import gamestates.Playing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utils.Constants.GameConstants.*;
import static utils.Constants.InteractableConstants.BOX_MOVESPEED;


public class Box extends Interactable {
    private float moveSpeed = BOX_MOVESPEED;
    private int timesEnemyChangedDirection = 0;
    private boolean coolingDown;
    private EnemyManager enemyManager;


    /**
     * Constructor for Box and calls methods to instantiate the variables
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Box(int x, int y, int width, int height){
        super(x,y,width,height);
        initialiseVariables(x,y,width,height);
    }

    /**
     * This method initialises the boxes variables
     */
    public void initialiseVariables(int x, int y, int width, int height){
        airSpeed = 0f;
        gravity = 0.03f * SCALE;
        loadBoxImage();
        initialiseHitbox(x,y,width,height);
        firstUpdate = true;
    }

    /**
     * This method initialises the player to the object
     * @param startPlayer
     */
    public void initialisePlayerToBox(Start_Player startPlayer){
        this.startPlayer = startPlayer;
    }

    public void setEnemyManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    /**
     * This method checks if the player is colliding the box and makes checks where
     * @param box
     * @param startPlayer
     */
    public void checkIfPlayerCollidesWithBox(Box box, Start_Player startPlayer){
        if(box.hitbox.intersects(startPlayer.getHitbox()) == true){
            checkIfPlayerIsAboveBox();
        } else if (startPlayer.getRightPlayerSpeed() != 1.2f
                || startPlayer.getLeftPlayerSpeed() != -1.2f
                || startPlayer.getStandingOnInteractable() == true){
            resetChangedPlayerVariables();
        }
    }

    /**
     * This method checks if the enemies collides with the box
     * @param rats
     */
    public void checkIfEnemyIsCollidingWithBox(ArrayList<EnemyRat> rats) {
        for (EnemyRat rat : rats) {
            if (hitbox.intersects(rat.getHitbox()) == true) {
                rat.changeEnemyWalkDirection();
                if (timesEnemyChangedDirection == 0 && coolingDown == false){
                    coolingDown = true;
                    new BounceResetCooldownTimer().start();
                }
                timesEnemyChangedDirection++;
                if (timesEnemyChangedDirection >= 15){
                    rat.setCurrentHealth(0);
                }
            }
        }
    }


    /**
     * This method takes in the leveldata and updates the position of the entity
     * @param levelData
     */

    private void updateEntityPosition(int[][] levelData) {
        if(firstUpdate == true){
            isInteractableInAir(levelData);
            firstUpdate = false;
        }
        if(inAir == false){
            isInteractableInAir(levelData);
        }
        moveInteractable(levelData);
        isMoving = true;
    }

    /**
     * This method is responsible for running all the update methods in the class
     * @param levelData
     */
    public void update(int[][] levelData, Box box, Playing playing ){
        updateEntityPosition(levelData);
        box.checkIfPlayerCollidesWithBox(box, playing.getPlayer());
        box.checkIfEnemyIsCollidingWithBox(playing.getEnemyManager().getRats());
    }


    /**
     * This method checks which direction the box is pushed from and assigns the speed according to that
     */
    private void checkPushDirection(){
        if(hitbox.x > startPlayer.getHitbox().x){
            horizontalSpeed += moveSpeed;
        } else if (hitbox.x < startPlayer.getHitbox().x){
            horizontalSpeed -= moveSpeed;
        }
        startPlayer.setStandingOnInteractable(false);
    }


    /**
     * This method resets the variables changed in player from this class
     */
    private void resetChangedPlayerVariables(){
        startPlayer.setHorizontalSpeed(0);
        startPlayer.setStandingOnInteractable(false);
        startPlayer.setPushing(false);
    }

    /**
     * this method checks if the player is above the box and calls the appropiate methods
     * for standing on the box or pushing
     */
    private void checkIfPlayerIsAboveBox(){
        if(hitbox.y > (startPlayer.getHitbox().y+57.8f)){
            startPlayer.setPlayerStandingOnInteractable();
            startPlayer.setPushing(false);
            startPlayer.setInAir(false);
        }
        else {
            startPlayer.setPushing(true);
            checkPushDirection();
        }
    }


    /**
     * This method loads the image of the box
     */
    private void loadBoxImage() {
        InputStream inputStream = getClass().getResourceAsStream("/BOX_DARK_SPRITE.png");
        try{
            interactableImage = ImageIO.read(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method draws the box with an xOffset
     * @param g
     * @param xOffset
     */
    public void draw(Graphics g, int xOffset){
        g.drawImage(interactableImage, (int) hitbox.x- xOffset, (int) hitbox.y+1, (int) width, (int) height, null);
    }


    public void drawHitbox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x - levelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }


    private class BounceResetCooldownTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            timesEnemyChangedDirection = 0;
            coolingDown = false;
        }
    }
}
