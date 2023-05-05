package entity.interactable;

import entity.player.Player;
import entity.player.Start_Player;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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

    public Interactable(float x, float y, int width, int height){
        initialiseVariables(x,y,width,height);
    }

    private void initialiseVariables(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initialiseHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y,width,height);
    }

    protected void moveInteractable(int[][] levelData) {
        if (inAir == true) {
            checkIfInteractableCanMoveInAir(levelData);
        } else {
            updateInteractableXPosition(horizontalSpeed, levelData);
        }
        horizontalSpeed = 0;
    }

    protected void updateInteractableXPosition(float horizontalSpeed, int [][] levelData) {
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
            hitbox.x += horizontalSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, horizontalSpeed);
        }
    }

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
    }

    private void checkIfInteractableCanMoveInAir(int[][] levelData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData) == true) {
            hitbox.y += airSpeed;
            changeAirSpeed();
            updateInteractableXPosition(horizontalSpeed, levelData);
        } else {
            updateInteractableXPosition(horizontalSpeed, levelData);
        }
    }

    private void changeAirSpeed(){
        if(airSpeed < MAX_AIR_SPEED){
            airSpeed += gravity;
        }
    }

    protected void isInteractableInAir(int[][] levelData){
        if(IsEntityOnFloor(hitbox, levelData) == false){
            inAir = true;
        }
    }

    public Rectangle2D.Float getHitbox() {return hitbox;}
}
