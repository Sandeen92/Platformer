package userinterface;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UserInterface.Buttons.BTN_WIDTH;
import static utils.Constants.UserInterface.OptionButtons.OPTIONBTN_DEFAULT_SIZE;
import static utils.Constants.UserInterface.OptionButtons.OPTIONBTN_SIZE;

public class SoundButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = BTN_WIDTH / 2;
    private boolean mouseOver, mousePressed;
    private BufferedImage[] images;
    private Rectangle btnBounds;

    public SoundButton(int xPos, int yPos, int rowIndex){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        loadImages();
        initBounds();
    }

    private void initBounds(){
        btnBounds = new Rectangle(xPos, yPos, OPTIONBTN_SIZE, OPTIONBTN_SIZE);
    }

    private void loadImages(){
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BUTTONS);

        for (int i = 0; i < images.length; i++){
            images[i] = temp.getSubimage(
                    i * OPTIONBTN_DEFAULT_SIZE,
                    rowIndex * OPTIONBTN_DEFAULT_SIZE,
                    OPTIONBTN_DEFAULT_SIZE,
                    OPTIONBTN_DEFAULT_SIZE);
        }
    }

    public void drawButtons(Graphics g){
        g.drawImage(images[index], xPos, yPos, OPTIONBTN_SIZE, OPTIONBTN_SIZE, null);
    }

    public void updateButtons(){
        index = 0;
        if (mouseOver){
            index = 1;
        }
        if (mousePressed){
            index = 2;
        }
    }

    public boolean isMouseOver() {return mouseOver;}
    public void setMouseOver(boolean mouseOver) {this.mouseOver = mouseOver;}
    public boolean isMousePressed() {return mousePressed;}
    public void setMousePressed(boolean mousePressed) {this.mousePressed = mousePressed;}

    public Rectangle getBtnBounds(){
        return btnBounds;
    }


    public void resetBtnBooleans(){
        mouseOver = false;
        mousePressed = false;
    }
}
