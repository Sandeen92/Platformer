/**
 * This abstract class is used to keep the general functionality of all entities
 * @author Linus Magnusson
 */

package entity;

import main.Game;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import static utils.AssistanceMethods.*;
import static utils.Constants.PlayerConstants.*;

public abstract class Entity {
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected int maxHealth;
    protected int currentHealth;
    protected int attackDamage;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected int animationIndex;
    protected int animationTick;
    protected int animationSpeed = 30;
    protected int entityState = IDLE;
    protected float airSpeed; // the airspeed of the entity
    protected float gravity; // the gravity of the entity, this is multiplied with game.Scale
    protected float jumpSpeed; // The speed of the entity jump, this is multiplied with game.Scale
    protected float fallSpeedAfterCollision; // The speed the enity is falling in after a collision, this is multiplied with game.Scale
    protected boolean   inAir = false; // check if in air
    protected boolean jumping = false; // check if jumping
    protected boolean isMoving = false; // check if moving
    protected float xSpeed; // speed of the entity to the left or right
    protected boolean movingLeft; // check if entity moving left
    protected boolean movingRight; // check if entity moving right

    /**
     * Constructor for the entity-class initializes all the important variables
     */
    public Entity(float x, float y,int width, int height, int maxHealth, int attackDamage){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        airSpeed = 0f;
        gravity = 0.03f * Game.SCALE;
        jumpSpeed = -2.25f * Game.SCALE;
        fallSpeedAfterCollision = 0.5f * Game.SCALE;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackDamage = attackDamage;
    }

    /**
     * This method initializes the hitbox of the entity
     * @param x
     * @param y
     * @param width
     * @param heigth
     */
    protected void initialiseHitbox(float x, float y, float width, float heigth) {
        hitbox = new Rectangle2D.Float(x, y,width,heigth);
    }

    protected void initialiseAttackBox(float x, float y, float width, float heigth){
        attackBox = new Rectangle2D.Float(x, y, width, heigth);
    }

    //For Debugging hitbox
    protected void drawHitbox(Graphics g, int levelOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x - levelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    //For Debugging attackBox
    protected void drawAttackBox(Graphics g,int levelOffset){
        g.setColor(Color.GREEN);
        g.drawRect((int)(attackBox.x - levelOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Abstract method that is used to handle logic of the movement of different
     * entities therefore it should be declared in each class
     * @param lvldata
     */
    protected abstract void updateEntityPos(int[][] lvldata);

    /**
     * This method sets the boolean jump to true
     */
    protected void jump() {
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    /**
     * This method updates the x position of the entity by taking in the speed and leveldata to check
     * if the move is valid
     * @param xSpeed
     * @param lvlData
     */
    protected void updateXPosition(float xSpeed, int [][] lvlData) {
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void entityTakeDamage(int damage){
        currentHealth -= damage;
    }

    public boolean isEntityDead(){
        if(currentHealth <= 0){
            return true;
        }
        return false;
    }

    /**
     * This method is used to set the inAir variable to false when
     * the entity is no longer in the air
     */
    protected void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * This method checks if the entity is in air and changes the boolean to true or false
     * @param lvlData
     */
    protected void isEntityInAir(int[][] lvlData){
        if(!IsEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }

    /**
     * This method is used to move the entity with all the checks that is needed to be done
     * Makes the physics and collisions work
     * @param lvlData
     */
    protected void moveEntity(int[][] lvlData){
        isEntityInAir(lvlData);
        if(inAir){
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData )){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPosition(xSpeed, lvlData);
            } else {
                hitbox.y = GetEntityYPosUnderOrAboveTile(hitbox, airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPosition(xSpeed, lvlData);
            }
        } else {
            updateXPosition(xSpeed, lvlData);
        }
    }

    /**
     * This method resets the variables for the animations
     */
    protected void resetAnimationTick(){
        animationTick = 0;
        animationIndex = 0;
    }

    /**
     * This method updates the animationtick to keep track of which stage of the animation
     * is the next
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(entityState)) {
                animationIndex = 0;
            }
        }
    }

    /**
     * This method sets the player animation based on current state
     */
    protected void setEntityAnimation() {

        int startAnimation = entityState;

        if (isMoving) {
            entityState = RUNNING;
        } else {
            entityState = IDLE;
        }
        if(inAir) {
            entityState = JUMP;
        }

        if (startAnimation != entityState){
            resetAnimationTick();
        }
    }

    /**
     * This method returns the hitbox of the entity
     * @return
     */
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * This method sets all moving booleans to false
     */
    public void allMovingBooleansFalse() {
        setMovingRight(false);
        setMovingLeft(false);
        isMoving = false;
    }

}
