/**
 * This class is an interactable box that the player can move and jump on
 * @author Linus Magnusson
 */

package entity;

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
    private BufferedImage boxImage;
    private Player player;
    private boolean firstUpdate;

    /**
     * Constructor for Box and calls methods to instantiate the variables
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Box(int x, int y, int width, int height){
        super(x,y,width,height);
        initialiseVariables();
    }

    /**
     * This method initialises the boxes variables
     */
    public void initialiseVariables(){
        airSpeed = 0f;
        gravity = 0.03f * SCALE;
        jumpSpeed = -2.25f * SCALE;
        fallSpeedAfterCollision = 0.5f * SCALE;
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
        horizontalSpeed = 0;
        if(box.hitbox.intersects(player.hitbox) == true){
            checkIfPlayerIsAboveBox();
        } else if (player.getPlayerSpeed() != 1.2 || player.getStandingOnInteractable() == true){
            resetPlayerVariables();
        }
    }

    /**
     * This method checks if the enemies collides with the box
     * @param crabbies
     */
    public void checkIfEnemyIsCollidingWithBox(ArrayList<EnemyRat> crabbies) {
        for (EnemyRat c : crabbies) {
            if (hitbox.intersects(c.hitbox) == true) {
                changeEnemyWalkDirection(c);
            }
        }
    }

    /**
     * This method takes in an enemy and changes its walkdirektion and sets an offset that helps the enemy not getting
     * stuck
     * @param rat
     */
    private void changeEnemyWalkDirection(EnemyRat rat){
        if(rat.getWalkDir() == LEFT) {
            rat.hitbox.x += 3;
        } else {
            rat.hitbox.x -= 3;
        }
        rat.changeWalkDirection();
    }

    /**
     * This method takes in the leveldata and updates the position of the entity
     * @param lvldata
     */
    @Override
    protected void updateEntityPos(int[][] lvldata) {
        if(firstUpdate == true){
            isEntityInAir(lvldata);
            firstUpdate = false;
        }
        if(inAir == false){
            isEntityInAir(lvldata);
        }
        moveEntity(lvldata);
        isMoving = true;
    }

    /**
     * This method is responsible for running all the update methods in the class
     * @param lvlData
     */
    public void update(int[][] lvlData){
        updateEntityPos(lvlData);
    }

    /**
     * This method checks which direction the box is pushed from and assigns the speed according to that
     */
    private void checkPushdirection(){
        if(hitbox.x > player.hitbox.x){
            horizontalSpeed += moveSpeed;
        } else if (hitbox.x < player.hitbox.x){
            horizontalSpeed -= moveSpeed;
        }
        player.setSpeed(moveSpeed);
        player.setPushing(true);
    }

    /**
     * This method sets the variables for the player if the player is standing on an interactabel object
     */
    private void setPlayerStandingOnInteractable (){
        if(player.getStandingOnInteractable() == false){
            player.setStandingOnInteractable(true);
        }
        player.resetInAir();
    }

    /**
     * This method resets the variables changed in player from this class
     */
    private void resetPlayerVariables(){
        player.setSpeed(1.2f);
        player.setStandingOnInteractable(false);
        player.setPushing(false);
    }

    /**
     * this method checks if the player is above the box and calls the appropiate methods
     * for standing on the box or pushing
     */
    private void checkIfPlayerIsAboveBox(){
        if((hitbox.y < player.hitbox.y+height)){
            checkPushdirection();
        } else if(hitbox.y > player.hitbox.y+height){
            setPlayerStandingOnInteractable();
        }
    }

    /**
     * This method loads the image of the box
     */
    private void loadBoxImage() {
        InputStream is = getClass().getResourceAsStream("/BOX_DARK_SPRITE.png");
        try{
            boxImage = ImageIO.read(is);
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
        g.drawImage(boxImage, (int) hitbox.x- xOffset, (int) hitbox.y+1, (int) width, (int) height, null);
        //drawHitbox(g,xOffset);
    }
}
