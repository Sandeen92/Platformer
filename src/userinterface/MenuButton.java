package userinterface;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UserInterface.Buttons.*;

public class MenuButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = BTN_WIDTH / 2;
    private boolean mouseOver, mousePressed;
    private Gamestate state;
    private BufferedImage[] images;
    private Rectangle btnBounds;

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initBounds();
    }

    private void initBounds(){
        btnBounds = new Rectangle(xPos - xOffsetCenter, yPos, BTN_WIDTH, BTN_HEIGHT);
    }

    private void loadImages(){
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);

        for (int i = 0; i < images.length; i++){
            images[i] = temp.getSubimage(
                    i * BTN_WIDTH_DEFAULT,
                    rowIndex * BTN_HEIGHT_DEFAULT,
                    BTN_WIDTH_DEFAULT,
                    BTN_HEIGHT_DEFAULT);
        }
    }

    public void drawButtons(Graphics g){
        g.drawImage(images[index], xPos - xOffsetCenter, yPos, BTN_WIDTH, BTN_HEIGHT, null);
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

    public void applyGamestate(){
        Gamestate.state = state;
    }

    public void resetBtnBooleans(){
        mouseOver = false;
        mousePressed = false;
    }


}
