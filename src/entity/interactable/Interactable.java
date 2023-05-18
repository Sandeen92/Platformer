package entity.interactable;
/**
 * This class is the superclass for every object and item that's meant to be interacted with.
 * @author Linus Magnusson
 * @author Simon Sandén
 */
//Imports from within project
import entity.player.Player;
import entity.player.Start_Player;
import gamestates.Playing;
//Imports from Javas library
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
//Imports of static variables and methods
import static utils.AssistanceMethods.*;
import static utils.Constants.EntityConstants.MAX_AIR_SPEED;

public abstract class Interactable {

    protected float x;
    protected float y;
    protected int width;
    protected int height;

    protected Rectangle2D.Float hitbox;
    protected BufferedImage interactableImage;
    protected Player player;
    protected boolean firstUpdate;
    protected boolean inAir;
    protected float airSpeed;
    protected float gravity;
    protected float horizontalSpeed;
    protected boolean isMoving;
    protected boolean movingLeft;
    protected boolean movingRight;

    public Interactable(float x, float y, int width, int height){
        initialiseVariables(x,y,width,height);
    }

    /**
     * Initializes the variables of the object with the specified values.
     *
     * @param x the x-coordinate of the object
     * @param y the y-coordinate of the object
     * @param width the width of the object
     * @param height the height of the object
     */
    private void initialiseVariables(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    /**
     * Initializes the hitbox of the object with the specified coordinates, width, and height.
     *
     * @param x the x-coordinate of the hitbox
     * @param y the y-coordinate of the hitbox
     * @param width the width of the hitbox
     * @param height the height of the hitbox
     */
    protected void initialiseHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y,width,height);
    }

    /**
     * Moves the interactable object based on its current state and level data.
     *
     * @param levelData the level data array
     */
    protected void moveInteractable(int[][] levelData) {
        if (inAir == true) {
            checkIfInteractableCanMoveInAir(levelData);
        } else {
            updateInteractableXPosition(horizontalSpeed, levelData);
        }
        horizontalSpeed = 0;
    }

    /**
     * Updates the X position of the interactable object based on the horizontal speed and level data.
     *
     * @param horizontalSpeed the horizontal speed of the object
     * @param levelData the level data array
     */
    protected void updateInteractableXPosition(float horizontalSpeed, int [][] levelData) {
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
            hitbox.x += horizontalSpeed;
        } else {
            if (movingLeft) {
                player.setStuckInBoxLeft(true);
                player.setStuckInBoxRight(false);
                hitbox.x = GetEntityXPosNextToWall(hitbox) + 2; //Lägger till 2 på X-koordinater här så lådan inte hoppar igenom solid tiles
            }                                                   //Måste göras här eftersom karaktär och objekt har olika storlekar
            else {
                player.setStuckInBoxLeft(false);
                player.setStuckInBoxRight(true);
                hitbox.x = GetEntityXPosNextToWall(hitbox) + 15;   //15 är antalet X-koordinater den måste lägga till för att inte studsa tillbaka
            }
        }
    }

  /* TODO Denna används inte, dubbelkolla om den behövs!
  private void updateInteractablePosition(int[][] levelData) {
        if(firstUpdate == true){
            isInteractableInAir(levelData);
            firstUpdate = false;
        }
        if(inAir == false){
            isInteractableInAir(levelData);
        }
        moveInteractable(levelData);
        isMoving = true;
    } */

    /**
     * Checks if the interactable object can move in the air and performs the necessary actions.
     *
     * @param levelData the level data array
     */
    private void checkIfInteractableCanMoveInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData) == true) {
            hitbox.y += airSpeed;
            changeAirSpeed();
            updateInteractableXPosition(horizontalSpeed, levelData);
        } else {
            updateInteractableXPosition(horizontalSpeed, levelData);
        }
    }

    /**
     * Changes the air speed of the interactable object if it is below the maximum air speed.
     */
    private void changeAirSpeed(){
        if(airSpeed < MAX_AIR_SPEED){
            airSpeed += gravity;
        }
    }

    /**
     * Checks if the interactable object is in the air based on the level data.
     *
     * @param levelData the level data array
     */
    protected void isInteractableInAir(int[][] levelData){
        if(IsEntityOnFloor(hitbox, levelData) == false){
            inAir = true;
        }
    }

    public Rectangle2D.Float getHitbox() {return hitbox;}

    public void setHorizontalSpeed(float horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }
}
