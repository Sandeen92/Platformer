package entity;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utils.Constants.PlayerConstants.*;
import static utils.AssistanceMethods.*;

public class Player extends Entity {
    private BufferedImage[][] playerAnimations;
    private int animationTick;
    private int animationIndex;
    private int animationSpeed = 30;
    private int playerAction = IDLE;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean movingUp;
    private boolean movingDown;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private boolean isMoving = false;
    private boolean jumping;
    private float playerSpeed = 1.2f;
    //Kan detta flyttas till en physics clas??
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;


    public Player(float x, float y, int width, int heigth) {
        super(x, y, width, heigth);
        loadPlayerAnimations();
        initialiseHitbox(x,y, 20 * Game.SCALE, 27 * Game.SCALE);
    }

    public void updatePlayer() {
        updatePlayerPosition();
        updateAnimationTick();
        setPlayerAnimation();
    }

    public void renderPlayer(Graphics g) {
        g.drawImage(playerAnimations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset), (int)(hitbox.y - yDrawOffset), width,heigth, null);
        //drawHitbox(g);
    }

    private void updatePlayerPosition() {
        isMoving = false;
        if(jumping){
            jump();
        }
        if(!movingLeft && !movingRight && ! inAir){
            return;
        }

        float xSpeed = 0;

        if(movingLeft){
            xSpeed -=playerSpeed;
        } else if (movingRight){
            xSpeed += playerSpeed;
        }

        if(!inAir){
            if(!IsEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
        }

        if(inAir){
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData )){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPosition(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderOrAboveTile(hitbox, airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPosition(xSpeed);
            }
        } else {
            updateXPosition(xSpeed);
        }
        isMoving = true;
    }

    private void jump() {
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPosition(float xSpeed) {
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void setPlayerAnimation() {
        if (isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if(inAir) {
            playerAction = JUMP;
        }
    }


    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }
    }


    public void loadPlayerAnimations() {

        InputStream is = getClass().getResourceAsStream("/Test.png");
        try {
            BufferedImage player = ImageIO.read(is);
            playerAnimations = new BufferedImage[9][6];
            for (int i = 0; i < playerAnimations.length; i++) {
                for (int j = 0; j < playerAnimations[i].length; j++) {
                    playerAnimations[i][j] = player.getSubimage(j * 64, i * 40, 64, 40);
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

    public void loadLvlData(int [][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox,lvlData)){
            inAir = true;
        }
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void allMovingBooleansFalse() {
        setMovingRight(false);
        setMovingLeft(false);
        setMovingUp(false);
        setMovingDown(false);
        isMoving = false;
    }
}
