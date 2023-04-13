package userinterface;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UserInterface.OptionButtons.*;

public class OptionButton extends Button {

    private int rowIndex, index;
    private boolean mouseOver, mousePressed;
    private BufferedImage[] images;

    public OptionButton(int x, int y, int btnWidth, int btnHeight, int rowIndex){
        super(x, y, btnWidth,btnHeight);
        this.rowIndex = rowIndex;
        loadImages();
    }

    private void loadImages(){
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BUTTONS);

        for (int i = 0; i < images.length; i++){
            images[i] = temp.getSubimage(
                    i * OPTIONBTN_DEFAULT_SIZE,
                    rowIndex * (OPTIONBTN_DEFAULT_SIZE - 30),
                    OPTIONBTN_DEFAULT_SIZE,
                    OPTIONBTN_DEFAULT_SIZE);
        }
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

    public void drawButtons(Graphics g){
        g.drawImage(images[index], x, y, OPTIONBTN_SIZE, OPTIONBTN_SIZE, null);
    }

    public void resetBtnBooleans(){
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {return mouseOver;}
    public void setMouseOver(boolean mouseOver) {this.mouseOver = mouseOver;}
    public boolean isMousePressed() {return mousePressed;}
    public void setMousePressed(boolean mousePressed) {this.mousePressed = mousePressed;}

}
