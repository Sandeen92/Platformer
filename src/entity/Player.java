package entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.PlayerConstants.*;
import static utils.AssistanceMethods.canMoveHere;

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
    private boolean isMoving = false;
    private float playerSpeed = 2.0f;

    public Player(float x, float y, int width, int heigth) {
        super(x, y, width, heigth);
        loadPlayerAnimations();
    }

    public void updatePlayer() {
        updatePlayerPosition();
        updateHitbox();
        updateAnimationTick();
        setPlayerAnimation();
    }

    public void renderPlayer(Graphics g) {
        g.drawImage(playerAnimations[playerAction][animationIndex], (int)x, (int)y,width,heigth, null);
        drawHitbox(g);
    }

    private void updatePlayerPosition() {
        isMoving = false;
        if(!movingLeft && !movingRight){
            return;
        }
        float xSpeed = 0;
        float ySpeed = 0;

        if(movingLeft && !movingRight){
            xSpeed = -playerSpeed;

        } else if (movingRight && !movingLeft){
            xSpeed = playerSpeed;

        }

        //ADD FOR UP AND DOWN

        //TODO FIX LATER
        if(canMoveHere(x+xSpeed, y+ySpeed, width,heigth)){
            this.x += xSpeed;
            this.y += ySpeed;
            isMoving =true;
        }
    }

    private void setPlayerAnimation() {
        if (isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
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

    public void allMovingBooleansFalse() {
        setMovingRight(false);
        setMovingLeft(false);
        isMoving = false;
    }
}
