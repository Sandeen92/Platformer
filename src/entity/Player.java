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
import static utils.AssistanceMethods.canMoveHere;

public class Player extends Entity {
    private BufferedImage[][] playerAnimations;
    private EnemyManager enemyManager;
    private AttackTimer attackTimer;
    private float xDrawOffset = 24 * Game.SCALE;
    private float yDrawOffset = 14 * Game.SCALE;
    private int[][] levelData;
    private float playerSpeed = 1.2f;
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
        initialiseHitbox(x,y, 22 * Game.SCALE, 30 * Game.SCALE);
        initialiseAttackBox(x,y,20 * Game.SCALE, 27 * Game.SCALE);
        this.enemyManager = enemyManager;
        initialiseVariables();

    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

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

    protected void jump() {
        if(inAir){
            return;
        } else if (jumpOnce){
            if(isPushing == false){
                inAir = true;
                airSpeed = jumpSpeed;
                jumpOnce = false;
            }
        }
    }

    public void playerHit(Enemy attackingEnemy){
        this.attackingEnemy = attackingEnemy;
        isHit = true;
        HitTimer ht = new HitTimer();
        ht.start();
    }

    public void attack(){
        //TODO add animation for attacking
        if(canAttack){
            enemyManager.checkIfEnemyIsHit(attackBox);
            canAttack = false;
            attackTimer = new AttackTimer();
            attackTimer.start();
            System.out.println("Attacked");
        }
    }

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

    //TODO få in spelarfacing och gör if-satser eller intersectsLine och gör ett entitystate hit som gör allt mer smooth
    public void knockbackPlayer(Enemy enemy){
        horizontalSpeed = 0;
        getKnockbackDirection(enemy);
        if(canMoveHere(hitbox.x + horizontalSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)){
                hitbox.x += horizontalSpeed;
        }
    }

    private void getKnockbackDirection(Enemy enemy){
        if(hitbox.x > enemy.hitbox.x){
            setHorizontalSpeed(knockbackSpeed);
        } else if (hitbox.x < enemy.hitbox.x){
            setHorizontalSpeed(knockbackSpeed);
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

        if(isHit){
            knockbackPlayer(attackingEnemy);
        }

        checkIfPlayerIsStandingOnInteractable(levelData);
        moveEntity(levelData);
        isMoving = true;
    }

    private boolean checkIfPlayerIsMoving() {
        return !movingLeft && !movingRight && !inAir && !isHit;
    }

    private void checkIfPlayerIsJumping(){
        if(jumping){
            jump();
        }
    }

    private void flipPlayerLeft(){
        horizontalSpeed -=playerSpeed;
        flipX = width;
        flipW = -1;
        facing = 0;
    }

    private void flipPlayerRight(){
        horizontalSpeed += playerSpeed;
        flipX = 0;
        flipW = 1;
        facing = 1;
    }

    private void changeMovingDirection(){
        if(movingLeft){
            flipPlayerLeft();
        } else if (movingRight) {
            flipPlayerRight();
        }
    }

    private void checkIfPlayerIsStandingOnInteractable(int[][] lvlData){
        if(standingOnInteractable == false){
            if(inAir == false ){
                isEntityInAir(lvlData);
            }
        }

    }

    /**
     * This method loads the players animations into a 2d array of bufferedimages
     */
    public void loadPlayerAnimations() {

        InputStream is = getClass().getResourceAsStream("/Gubbe_1_Test.png");
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
        if(!IsEntityOnFloor(hitbox,lvlData)){
            inAir = true;
        }
    }

    private void setHorizontalSpeed(float knockbackSpeed) {
        horizontalSpeed = knockbackSpeed;
        hitbox.x += horizontalSpeed;
    }

    public void setSpeed(float speed){
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
