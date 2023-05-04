/**
 * This class is responsible for the players attributes and functions
 * @author Linus Magnusson
 */

package entity.player;

import entity.enemy.Enemy;
import entity.enemy.EnemyManager;
import entity.projectiles.ProjectileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
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
    private ProjectileManager projectileManager;
    private AttackTimer attackTimer;
    private float xDrawOffset = PLAYER_X_DRAW_OFFSET;
    private float yDrawOffset = PLAYER_Y_DRAW_OFFSET;
    private int[][] levelData;
    private float rightPlayerSpeed = PLAYER_SPEED;
    private float leftPlayerSpeed = -PLAYER_SPEED;
    private int flipX = 0;
    private int flipW = 1;
    private boolean jumpOnce;
    private boolean canAttack;
    private int facingDirection;
    private Enemy attackingEnemy;
    private float knockbackSpeed;
    private final float rightPushSpeed = 0.6f;
    private final float leftPushSpeed = -0.6f;


    /**
     * Constructor for Player
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Player(float x, float y, int width, int height, int maxHealth, int attackDamage, EnemyManager enemyManager, ProjectileManager projectileManager) {
        super(x, y, width, height, maxHealth, attackDamage);
        loadPlayerAnimations();
        initialiseHitbox(x,y, 22 * SCALE, 30 * SCALE);
        initialiseAttackBox(x,y,20 * SCALE, 27 * SCALE);
        initialiseBoxAttackBox(x, y, 90 * SCALE, 30 * SCALE);
        this.enemyManager = enemyManager;
        this.projectileManager = projectileManager;
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
        facingDirection = 1;
        standingOnInteractable = false;
        isPushing = false;
        knockbackSpeed = 0.4f * 1.2f;
    }

    /**
     * This method is responsible for updating the player
     */
    public void updatePlayer() {
        updateEntityPosition(levelData);
        updateAnimationTick();
        updateAttackBox(30, facingDirection);
        updateBoxAttackBox(30, facingDirection);
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
     * it allows the player to jump
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
        if(canAttack == true){
            projectileManager.addBullet(hitbox.x, hitbox.y + 32,levelData , facingDirection);
            startAttackCooldown();
        }
    }

    public void attackWithBox(){
        enemyManager.checkIfEnemyIsHitByBox(boxAttackBox);
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
    public void resetBooleanJumpOnce(){
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
        //drawHitbox(g, levelOffset);
        //drawAttackBox(g, levelOffset);
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
        if(hitbox.x < enemy.getHitbox().x){
            setHorizontalKnockbackSpeed(PLAYER_KNOCKBACK_LEFT);
        } else if (hitbox.x > enemy.getHitbox().x){
            setHorizontalKnockbackSpeed(PLAYER_KNOCKBACK_RIGHT);
        }
    }

    /**
     *  This method updates the players position and checks for collisions
     */
    protected void updateEntityPosition(int [][] levelData) {
        isMoving = false;
        checkIfPlayerIsJumping();
        if(checkIfPlayerIsMoving() == true){
            return;
        }

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
        if (!isPushing) {
            horizontalSpeed = leftPlayerSpeed;
        }
        else {
            horizontalSpeed = leftPushSpeed;
        }
        flipX = width;
        flipW = -1;
        facingDirection = 0;
        isPushing = false;
    }

    /**
     * This method flips the player to the rigth
     */
    private void flipPlayerRight(){
        if (!isPushing) {
            horizontalSpeed = rightPlayerSpeed;
        }
        else {
            horizontalSpeed = rightPushSpeed;
        }
        flipX = 0;
        flipW = 1;
        facingDirection = 1;
        isPushing = false;
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
     * @param levelData
     */
    private void checkIfPlayerIsStandingOnInteractable(int[][] levelData){
        if(standingOnInteractable == false){
            checkIfPlayerIsInAirWhenNotJumping(levelData);
        }
    }

    /**
     * This method checks if the player is in the air if the player is not jumping
     * @param levelData
     */
    private void checkIfPlayerIsInAirWhenNotJumping(int[][] levelData){
        if(inAir == false ){
            isEntityInAir(levelData);
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
     * This method sets the variables for the player if the player is standing on an interactabel object
     */
    public void setPlayerStandingOnInteractable(){
        if(getStandingOnInteractable() == false){
            setStandingOnInteractable(true);
        }
        resetBooleanInAir();
    }

    /**
     * This method loads the leveldata into the player
     * @param levelData
     */
    public void loadLvlData(int [][] levelData){
        this.levelData = levelData;
        if(IsEntityOnFloor(hitbox,levelData) == false){
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


    public float getRightPlayerSpeed(){
        return rightPlayerSpeed;
    }

    public float getLeftPlayerSpeed(){
        return leftPlayerSpeed;
    }

    public void setRightPlayerSpeed(float speed){
        rightPlayerSpeed = speed;
    }

    public void setLeftPlayerSpeed(float speed){
        leftPlayerSpeed = speed;
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
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    private class AttackTimer extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(300);
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
