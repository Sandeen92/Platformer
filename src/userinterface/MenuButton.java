package userinterface;

//Imports from within the project
import gamestates.Gamestate;
import utils.LoadSave;

//Imports from Javas library
import java.awt.*;
import java.awt.image.BufferedImage;

//Imports of static variables and methods
import static utils.Constants.UserInterface.Buttons.*;

/**
 * A class representing a menu button that can be interacted with by the user.
 *
 * @author Simon Sandén
 */
public class MenuButton {

    private int xPosition;
    private int yPosition;
    private int rowIndex;
    private int index;
    private int xOffsetCenter = BTN_WIDTH / 2;
    private boolean mouseOver;
    private boolean mousePressed;
    private Gamestate state;
    private BufferedImage[] images;
    private Rectangle btnBounds;



    public MenuButton(int xPosition, int yPosition, Gamestate state){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.state = state;
        loadReplayButtonImage();
        initialiseBounds();
    }

    /**
     * Constructor for MenuButton. Initializes the position, row index, game state,
     * loads the button images and initializes the button bounds.
     *
     * @param xPosition The x-coordinate of the button on the screen.
     * @param yPosition The y-coordinate of the button on the screen.
     * @param rowIndex The index of the button's row in the button sprite atlas.
     * @param state The game state to apply when the button is clicked.
     */
    public MenuButton(int xPosition, int yPosition, int rowIndex, Gamestate state){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initialiseBounds();
    }


    /**
     * Initializes the button bounds using the button's position and dimensions.
     */
    private void initialiseBounds(){
        btnBounds = new Rectangle(xPosition - xOffsetCenter, yPosition, BTN_WIDTH, BTN_HEIGHT);
    }

    /**
     * Loads the button images from the button sprite atlas based on rowIndex given when instantiating MenuButton object.
     */
    private void loadImages(){
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.STARTMENU_BUTTONS);

        for (int i = 0; i < images.length; i++){
            images[i] = temp.getSubimage(
                    i * BTN_WIDTH_DEFAULT,
                    rowIndex * BTN_HEIGHT_DEFAULT,
                    BTN_WIDTH_DEFAULT,
                    BTN_HEIGHT_DEFAULT);
        }
    }

    /**
     * Loads the replay button images.
     */
    private void loadReplayButtonImage(){
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.DEATHSCREEN_REPLAY_BUTTON);

        for (int i = 0; i < images.length; i++){
            images[i] = temp.getSubimage(
                    i * BTN_WIDTH_DEFAULT,
                    rowIndex * BTN_HEIGHT_DEFAULT,
                    BTN_WIDTH_DEFAULT,
                    BTN_HEIGHT_DEFAULT);
        }
    }

    /**
     * Draws the button image onto the screen.
     *
     * @param g the graphics object to use for drawing.
     */
    public void drawButtons(Graphics g){
        g.drawImage(images[index], xPosition - xOffsetCenter, yPosition, BTN_WIDTH, BTN_HEIGHT, null);
    }

    /**
     * Updates the button's current index based on whether the mouse is over or pressed on the button.
     */
    public void updateButtons(){
        index = 0;
        if (mouseOver == true){
            index = 1;
        }
        if (mousePressed == true){
            index = 2;
        }
    }

    /**
     * Returns whether the mouse is currently over the button.
     * @return True if the mouse is over the button, false otherwise.
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

    /**
     * Returns the bounds of the button.
     * @return The button's bounding rectangle.
     */
    public Rectangle getBtnBounds(){
        return btnBounds;
    }

    /**
     * Applies the button's associated game state when clicked.
     */
    public void applyGamestate(){
        Gamestate.state = state;
    }

    /**
     * Resets the booleans after reacting to users interaction
     */
    public void resetBtnBooleans(){
        mouseOver = false;
        mousePressed = false;
    }
}
