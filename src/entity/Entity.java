package entity;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.AssistanceMethods.*;

public abstract class Entity {
    protected   float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;
    protected int[][] lvlData; //Stores the leveldata in each Entity
    protected float airSpeed; // the airspeed of the entity
    protected float gravity; // the gravity of the entity, this is multiplied with game.Scale
    protected float jumpSpeed; // The speed of the entity jump, this is multiplied with game.Scale
    protected float fallSpeedAfterCollision; // The speed the enity is falling in after a collision, this is multiplied with game.Scale
    protected boolean inAir = false; // check if in air
    protected boolean jumping = false; // check if jumping
    protected boolean isMoving = false; // check if moving
    protected float xSpeed; // speed of the entity to the left or right
    protected boolean movingLeft; // check if entity moving left
    protected boolean movingRight; // check if entity moving right

    public Entity(float x, float y,int width, int height){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        airSpeed = 0f;
        gravity = 0.03f * Game.SCALE;
        jumpSpeed = -2.25f * Game.SCALE;
        fallSpeedAfterCollision = 0.5f * Game.SCALE;
    }

    protected void initialiseHitbox(float x, float y, float width, float heigth) {
        hitbox = new Rectangle2D.Float(x, y,width,heigth);
    }

    protected void drawHitbox(Graphics g){
        //For Debugging hitbox
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected abstract void updateEntityPos(int[][] lvldata);

    protected void jump() {
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    protected void updateXPosition(float xSpeed) {
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    protected void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void loadLvlData(int [][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox,lvlData)){
            inAir = true;
        }
    }


    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }


}
