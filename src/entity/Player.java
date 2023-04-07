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
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private float playerSpeed = 1.2f;
    private int flipX = 0;
    private int flipW = 1;


    public Player(float x, float y, int width, int heigth) {
        super(x, y, width, heigth);
        loadPlayerAnimations();
        initialiseHitbox(x,y, 20 * Game.SCALE, 27 * Game.SCALE);
    }

    // Gets called to update the players pos and animation
    public void updatePlayer() {
        updateEntityPos(lvlData);
        updateAnimationTick();
        setPlayerAnimation();
    }

    // Renders the player
    public void renderPlayer(Graphics g) {
        g.drawImage(playerAnimations[playerAction][animationIndex],
                (int) (hitbox.x - xDrawOffset) + flipX,
                (int) (hitbox.y - yDrawOffset),
                width * flipW,
                height, null);
    }

    // Updates the players position and checks for collisions
    protected void updateEntityPos(int [][] lvlData) {
        isMoving = false;
        if(jumping){
            jump();
        }
        if(!movingLeft && !movingRight && ! inAir){
            return;
        }

        xSpeed = 0;

        if(movingLeft){
            xSpeed -=playerSpeed;
            flipX = width;
            flipW = -1;
        } else if (movingRight){
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
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

    // Sets the player animation based on current state
    private void setPlayerAnimation() {

        int startAnimation = playerAction;

        if (isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if(inAir) {
            playerAction = JUMP;
        }

        if (startAnimation != playerAction){
            resetAnimationTick();
        }
    }


    private void resetAnimationTick(){
        animationTick = 0;
        animationIndex = 0;
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

    //loads the players animations
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

    public void allMovingBooleansFalse() {
        setMovingRight(false);
        setMovingLeft(false);
        isMoving = false;
    }
}
