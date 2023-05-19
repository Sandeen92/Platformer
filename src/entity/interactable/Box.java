/**
 * This class is an interactable box that the player can move and jump on
 * @author Linus Magnusson
 * @author Simon Sandén
 */

package entity.interactable;
//Imports from within project
import entity.enemy.EnemyManager;
import entity.enemy.EnemyRat;
import entity.player.Player;
import entity.player.Start_Player;
import gamestates.Playing;
import main.Game;
//Imports from Javas library
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
//Imports of static variables and methods
import static utils.Constants.EnemyConstants.DEAD;
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
     * @param player
     */
    public void initialisePlayerToBox(Player player){
        this.player = player;
    }

    /**
     * This method checks if the player is colliding the box and makes checks where
     * @param box
     * @param player
     */
    public void checkIfPlayerCollidesWithBox(Box box, Player player){
        if(box.hitbox.intersects(player.getHitbox()) == true){
            player.setTouchingInteractable(true);
            checkIfPlayerIsAboveBox();
        } else if (player.getRightPlayerSpeed() != 1.2f
                || player.getLeftPlayerSpeed() != -1.2f
                || player.getStandingOnInteractable() == true){
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
                if (rat.getEntityState() != DEAD){
                      rat.changeEnemyWalkDirection();
                }
                if (timesEnemyChangedDirection == 0 && coolingDown == false) {
                    coolingDown = true;
                    new BounceResetCooldownTimer().start();
                }
                timesEnemyChangedDirection++;
                killEnemyIfStuck(rat);
            }
        }
    }

    private void killEnemyIfStuck(EnemyRat rat){
        if (timesEnemyChangedDirection >= 2) {
            rat.setCurrentHealth(0);
            Robot robot = null;
            try {
                robot = new Robot();
                robot.keyPress(KeyEvent.VK_F12);
                robot.keyRelease(KeyEvent.VK_F12);
            } catch (AWTException e) {
                throw new RuntimeException(e);
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
            isMoving = true;
            firstUpdate = false;
        }
        if(inAir == false){
            isInteractableInAir(levelData);
        }
        if (horizontalSpeed != 0.0f) {
            isMoving = true;
        }
        else {
            isMoving = false;
        }
        moveInteractable(levelData);
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
        if(hitbox.x > player.getHitbox().x){
            movingRight = true;
            movingLeft = false;
            horizontalSpeed += moveSpeed;
        } else if (hitbox.x < player.getHitbox().x){
            movingLeft = true;
            movingRight = true;
            horizontalSpeed -= moveSpeed;
        }
        player.setStandingOnInteractable(false);
    }


    /**
     * This method resets the variables changed in player from this class
     */
    private void resetChangedPlayerVariables(){
        player.setHorizontalSpeed(0);
        player.setStandingOnInteractable(false);
        player.setPushing(false);
    }

    /**
     * this method checks if the player is above the box and calls the appropiate methods
     * for standing on the box or pushing
     */
    private void checkIfPlayerIsAboveBox(){
        if(hitbox.y > (player.getHitbox().y+57.8f)){
            player.setPlayerStandingOnInteractable();
            player.setPushing(false);
            player.setInAir(false);
            player.setAirSpeed(0);
        }
        else {
            player.setPushing(true);
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

    /**
     * Draws the hitbox on the graphics object with an offset based on the level.
     * Mainly for debugging purposes.
     * @param g the graphics object to draw on
     * @param levelOffset the offset value for the level
     */
    public void drawHitbox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x - levelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void setEnemyManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }




    private class BounceResetCooldownTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            timesEnemyChangedDirection = 0;
            coolingDown = false;
        }
    }
}
