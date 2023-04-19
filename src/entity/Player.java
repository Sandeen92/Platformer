/**
 * This class is responsible for the players attributes and functions
 * @author Linus Magnusson
 */

package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utils.AssistanceMethods.IsEntityOnFloor;
import static utils.Constants.GameConstants.*;
import static utils.AssistanceMethods.canMoveHere;
import static utils.Constants.PlayerConstants.*;

public class Player extends Entity {
    private BufferedImage[][] playerAnimations;
    private EnemyManager enemyManager;
    private AttackTimer attackTimer;
    private float xDrawOffset = PLAYER_X_DRAW_OFFSET;
    private float yDrawOffset = PLAYER_Y_DRAW_OFFSET;
    private int[][] levelData;
    private float playerSpeed = PLAYER_SPEED;
    private int flipX = 0;
    private int flipW = 1;
    private boolean jumpOnce;
    private boolean canAttack;
    private int facing;
    private Enemy attackingEnemy;
    private boolean standingOnInteractable;
    private boolean isPushing;
    private float knockbackSpeed;


    /**
     * Constructor for Player
     * @param x
     * @param y
     * @param width
     * @param heigth
     */
    public Player(float x, float y, int width, int heigth, int maxHealth, int attackDamage, EnemyManager enemyManager) {
        super(x, y, width, heigth, maxHealth, attackDamage);
        loadPlayerAnimations();
        initialiseHitbox(x,y, 22 * SCALE, 30 * SCALE);
        initialiseAttackBox(x,y,20 * SCALE, 27 * SCALE);
        this.enemyManager = enemyManager;
        initialiseVariables();
    }

    /**
     * This method sets the spawnpoint for the player
     * @param spawn
     */
    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }
    /**
     * This method initialises the variables of the player
     */
    private void initialiseVariables(){
        jumpOnce = true;
        canAttack = true;
        isHit = false;
        facing = 1;
        standingOnInteractable = false;
        isPushing = false;
        knockbackSpeed = 0.4f * 1.2f;
    }

    /**
     * This method is responsible for updating the player
     */
    public void updatePlayer() {
        updateEntityPos(levelData);
        updateAnimationTick();
        updateAttackBox(30, facing);
        setEntityAnimation();
    }

    /**
     * this method makes the player jump
     */
    protected void jump() {
        if(inAir == true){
            return;
        } else if (jumpOnce == true){
            checkIfPlayerIsPushing();
        }
    }

    /**
     * This method checks if the player is pushing something, if not
     * makes the player jump
     */
    private void checkIfPlayerIsPushing(){
        if(isPushing == false){
            inAir = true;
            airSpeed = jumpSpeed;
            jumpOnce = false;
        }
    }

    /**
     * This method sets the variables for when the player is hit
     * @param attackingEnemy
     */
    public void playerHit(Enemy attackingEnemy){
        this.attackingEnemy = attackingEnemy;
        isHit = true;
        HitTimer ht = new HitTimer();
        ht.start();
    }

    /**
     * This method makes the player attack
     */
    public void attack(){
        //TODO add animation for attacking
        if(canAttack == true){
            enemyManager.checkIfEnemyIsHit(attackBox);
            startAttackCooldown();
        }
    }

    /**
     * This method starts the attack cooldown
     */
    private void startAttackCooldown(){
        canAttack = false;
        attackTimer = new AttackTimer();
        attackTimer.start();
    }

    /**
     * This method resets the variable jumponce to make the player able to jump again
     */
    public void resetJumpOnce(){
        this.jumpOnce = true;
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

        //drawHitbox(g,levelOffset);
        //drawAttackBox(g, levelOffset); //TODO Remove later just for debugging
    }

    /**
     * This method knocks the player back
     * @param enemy
     */
    public void knockbackPlayer(Enemy enemy){
        horizontalSpeed = 0;
        getKnockbackDirection(enemy);
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData) == true){
                hitbox.x += horizontalSpeed;
        }
    }

    /**
     * This method gets the knockback direction for the player
     * @param enemy
     */
    private void getKnockbackDirection(Enemy enemy){
        if(hitbox.x < enemy.hitbox.x){
            setHorizontalKnockbackSpeed(PLAYER_KNOCKBACK_LEFT);
        } else if (hitbox.x > enemy.hitbox.x){
            setHorizontalKnockbackSpeed(PLAYER_KNOCKBACK_RIGHT);
        }
    }

    /**
     *  This method updates the players position and checks for collisions
     */
    protected void updateEntityPos(int [][] lvlData) {
        isMoving = false;

        checkIfPlayerIsJumping();
        if(checkIfPlayerIsMoving() == true){
            return;
        }

        horizontalSpeed = 0;
        changeMovingDirection();
        knockbackPlayerIfHit();
        checkIfPlayerIsStandingOnInteractable(levelData);
        moveEntity(levelData);
        isMoving = true;
    }

    /**
     * This method calls the method for knocking the player back if
     * the player is hit
     */
    private void knockbackPlayerIfHit(){
        if(isHit == true){
            knockbackPlayer(attackingEnemy);
        }
    }

    /**
     * This method checks if the player is moving in any way
     * @return
     */
    private boolean checkIfPlayerIsMoving() {
        return movingLeft == false && movingRight == false && inAir == false && isHit == false;
    }

    /**
     * This method checks if the player is currently jumping
     */
    private void checkIfPlayerIsJumping(){
        if(jumping == true){
            jump();
        }
    }

    /**
     * This method flips the player to the left
     */
    private void flipPlayerLeft(){
        horizontalSpeed -=playerSpeed;
        flipX = width;
        flipW = -1;
        facing = 0;
    }

    /**
     * This method flips the player to the rigth
     */
    private void flipPlayerRight(){
        horizontalSpeed += playerSpeed;
        flipX = 0;
        flipW = 1;
        facing = 1;
    }

    /**
     * This method changes the moving direction for the player and calls the methods
     * to flip the player the same way the player is moving
     */
    private void changeMovingDirection(){
        if(movingLeft == true){
            flipPlayerLeft();
        } else if (movingRight == true) {
            flipPlayerRight();
        }
    }

    /**
     * This method checks if the player is standing on an interactable
     * @param lvlData
     */
    private void checkIfPlayerIsStandingOnInteractable(int[][] lvlData){
        if(standingOnInteractable == false){
            checkIfPlayerIsInAirWhileNotJumping(lvlData);
        }
    }

    /**
     * This method checks if the player is in the air if the player is not jumping
     * @param lvlData
     */
    private void checkIfPlayerIsInAirWhileNotJumping(int[][] lvlData){
        if(inAir == false ){
            isEntityInAir(lvlData);
        }
    }

    /**
     * This method loads the players animations into a 2d array of bufferedimages
     */
    public void loadPlayerAnimations() {

        InputStream is = getClass().getResourceAsStream("/PLAYER_SPRITES.png");
        try {
            BufferedImage player = ImageIO.read(is);
            playerAnimations = new BufferedImage[4][8];
            for (int i = 0; i < playerAnimations.length; i++) {
                for (int j = 0; j < playerAnimations[i].length; j++) {
                    playerAnimations[i][j] = player.getSubimage(j * 64, i * 64, 64,64 );
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
        if(IsEntityOnFloor(hitbox,lvlData) == false){
            inAir = true;
        }
    }

    /**
     * This method sets the horixontal speed of the player
     * @param knockbackSpeed
     */
    private void setHorizontalKnockbackSpeed(float knockbackSpeed) {
        horizontalSpeed = knockbackSpeed;
        hitbox.x += horizontalSpeed;
    }

    public void setHorizontalSpeed(float speed){
        playerSpeed = speed;
    }

    public float getPlayerSpeed(){
        return playerSpeed;
    }

    public void setStandingOnInteractable(boolean b){
        this.standingOnInteractable = b;
    }

    public boolean getStandingOnInteractable(){
        return standingOnInteractable;
    }

    public boolean isPushing() {
        return isPushing;
    }

    public void setPushing(boolean pushing) {
        isPushing = pushing;
    }

    private class AttackTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                canAttack = true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class HitTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                isHit = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
