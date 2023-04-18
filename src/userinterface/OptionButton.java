package userinterface;

//Imports from within project
import utils.LoadSave;

//Imports from Javas library
import java.awt.*;
import java.awt.image.BufferedImage;

//Imports of static variables and methods
import static utils.Constants.UserInterface.OptionButtons.*;

/**
 * This class represents a button meant for options, which is a button with three different states: default, mouse over, and mouse pressed.
 * The button loads its images and updates its state when the mouse hovers over it or is pressed down on it.
 *
 * @author Simon Sandén
 */

public class OptionButton extends Button {

    private int rowIndex;
    private int index;
    private boolean mouseOver;
    private boolean mousePressed;
    private BufferedImage[] images;

    /**
     * Constructs an option button with the specified position, size, and row index.
     * @param x the x-coordinate of the top-left corner of the button
     * @param y the y-coordinate of the top-left corner of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param rowIndex the row index of the button in the sprite atlas
     */
    public OptionButton(int x, int y, int width, int height, int rowIndex){
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    /**
     * Loads the images for the button from the sprite atlas, based on its row index.
     */
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

    /**
     * Updates the button's state based on whether the mouse is over it or pressed down on it.
     */
    public void updateButtons(){
        index = 0;
        if (mouseOver){
            index = 1;
        }
        if (mousePressed){
            index = 2;
        }
    }

    /**
     * Draws the button in the frame, using its current image and position.
     * @param g the graphics object to use for drawing.
     */
    public void drawButtons(Graphics g){
        g.drawImage(images[index], x, y, OPTIONBTN_SIZE, OPTIONBTN_SIZE, null);
    }

    /**
     * Resets the button's mouse over and mouse pressed booleans to false.
     */
    public void resetBtnBooleans(){
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Returns whether the mouse is currently over the button.
     * @return true if the mouse is over the button, false otherwise
     */
    public boolean isMouseOver() {return mouseOver;}

    /**
     * Sets whether the mouse is currently over the button.
     * @param mouseOver true if the mouse is over the button, false otherwise
     */
    public void setMouseOver(boolean mouseOver) {this.mouseOver = mouseOver;}

    /**
     * Returns whether the mouse is currently pressed down on the button.
     * @return true if the mouse is pressed down on the button, false otherwise
     */
    public boolean isMousePressed() {return mousePressed;}

    /**
     * Sets whether the mouse is currently pressed down on the button.
     * @param mousePressed true if the mouse is pressed down on the button, false otherwise
     */
    public void setMousePressed(boolean mousePressed) {this.mousePressed = mousePressed;}

}
