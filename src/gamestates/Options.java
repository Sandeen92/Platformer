package gamestates;
//Imports from within project
import main.Game;
import userinterface.OptionButton;
import userinterface.Button;
import utils.LoadSave;
//Imports from Javas library
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
//Imports of static variables and methods
import static main.Game.setPreviousGamestate;
import static utils.Constants.GameConstants.*;
import static utils.Constants.UserInterface.OptionButtons.OPTIONBTN_SIZE;

/**
 * The Options class represents the game's options screen.
 * It extends the State class and implements the StateMethods interface.
 * It contains an options image, options buttons, and methods for updating,
 * drawing, and handling mouse and keyboard input for the options.
 *
 * @author Simon Sandén
 */
public class Options extends State implements StateMethods {

    private BufferedImage optionsBackgroundImage;
    private int optionsMenuXPosition;
    private int optionsMenuYPosition;
    private int optionsMenuWidth;
    private int optionsMenuHeight;
    private OptionButton returnButton;
    private OptionButton homeButton;

    /**
     * Constructor for gamestate Options, initializes background and buttons for menu
     * @param game the Game object that represents the whole game.
     */
    public Options(Game game){
        super(game);
        loadBackgroundImage();
        loadOptionButtons();
    }

    /**
     * This method creates and places buttons for Options within borders of background image
     */
    private void loadOptionButtons(){
        int optionButtonX = ((GAME_WIDTH / 3));
        int optionButtonY = (int) (340 * SCALE);

        returnButton = new OptionButton(optionButtonX, optionButtonY, OPTIONBTN_SIZE, OPTIONBTN_SIZE, 0);
        homeButton = new OptionButton(optionButtonX + 430, optionButtonY, OPTIONBTN_SIZE, OPTIONBTN_SIZE, 1);
    }

    /**
     * This method gets the background image from resources-package and calculates variables
     * needed for image to be drawn in the middle of the frame
     */
    private void loadBackgroundImage(){
        optionsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND);
        optionsMenuWidth = (int) (optionsBackgroundImage.getWidth() * SCALE);
        optionsMenuHeight = (int) (optionsBackgroundImage.getHeight() * SCALE);
        optionsMenuXPosition = GAME_WIDTH / 2 - optionsMenuWidth / 2;
        optionsMenuYPosition = 50;
    }

    /**
     * This method calls each buttons update method which reacts to user interaction
     */
    @Override
    public void update() {
        returnButton.updateButtons();
        homeButton.updateButtons();
    }

    /**
     * This method draws backdrop of either the paused game or the startmenu depending on if game was paused.
     * Also draws background image for menu in options aswell as the buttons depending on previous gamestate.
     * @param g the graphics object to use for drawing
     */
    public void draw(Graphics g){
        if (game.getPlaying().isPaused() == true){
            game.getPlaying().draw(g);
        }
        else {
            game.getStartmenu().draw(g);
        }

        g.drawImage(optionsBackgroundImage, optionsMenuXPosition, optionsMenuYPosition, optionsMenuWidth, optionsMenuHeight, null);
        returnButton.drawButtons(g);
        if (Gamestate.previousState != Gamestate.STARTMENU) {
            homeButton.drawButtons(g);
        }
    }

    /**
     * Responds to a mouse press event by setting the appropriate button's
     * "mouse pressed" state if the event occurred within that button's bounds.
     * @param e the MouseEvent representing the mouse press event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, returnButton) == true){
            returnButton.setMousePressed(true);
        }
        else if (isIn(e, homeButton) == true) {
            homeButton.setMousePressed(true);
        }
    }

    /**
     * Responds to a mouse release event by performing an action based on whether it was previously
     * in the "mouse pressed" state. Resets the "mouse pressed" state for all buttons.
     *
     * @param e the MouseEvent representing the mouse release event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, returnButton) == true){
            if (returnButton.isMousePressed() == true){
                returnButtonPressed();
            }
        }
        else if (isIn(e, homeButton) == true){
            if (homeButton.isMousePressed() == true){
                homeButtonPressed();
            }
        }
        returnButton.resetBtnBooleans();
        homeButton.resetBtnBooleans();
    }

    /**
     * This method executes different actions depending on previous gamestate when the returnButton is pressed by user
     */

    private void returnButtonPressed(){
        if (Gamestate.previousState == Gamestate.PAUSEMENU) {
            setPreviousGamestate();
            Gamestate.state = Gamestate.PAUSEMENU;
        }
        else if (Gamestate.previousState == Gamestate.STARTMENU){
            setPreviousGamestate();
            Gamestate.state = Gamestate.STARTMENU;
            game.getPlaying().setPaused(false);
            game.getPlaying().restartGame();
        }
    }

    /**
     * This method executes different actions depending on previous gamestate when the homeButton is pressed by user
     */

    private void homeButtonPressed(){
        if (Gamestate.previousState != Gamestate.STARTMENU) {
            Gamestate.state = Gamestate.STARTMENU;
            game.getPlaying().setPaused(false);
            game.getStartmenu().replayStartmenuAudio();
            game.getPlaying().restartGame();
        }
    }


    /**
     * Responds to a mouse move event by setting the "mouse over" state for the
     * return button if the mouse is currently over that button's bounds.
     *
     * @param e the MouseEvent representing the mouse move event
     */

    @Override
    public void mouseMoved(MouseEvent e) {
        returnButton.setMouseOver(false);
        homeButton.setMouseOver(false);

        if (isIn(e, returnButton) == true){
            returnButton.setMouseOver(true);
        }
        else if (isIn(e,homeButton) == true){
            homeButton.setMouseOver(true);
        }
    }

    /**
     * Responds to a key press event by changing the gamestate to the pausemenu
     * state if the escape key is pressed and the game was previously in the pause
     * menustate.
     *
     * @param e the KeyEvent representing the key press event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if (Gamestate.previousState == Gamestate.PAUSEMENU){
                Gamestate.state = Gamestate.PAUSEMENU;
            }
        }
    }

    /**
     * Responds to a key release event by changing the gamestate to the pausemenu
     * state if the escape key is released and the game was previously in the pausemenu state.
     *
     * @param e the KeyEvent representing the key release event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if (Gamestate.previousState == Gamestate.PAUSEMENU){
                Gamestate.state = Gamestate.PAUSEMENU;
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * This method returns a boolean value indicating whether the mouse event
     * occurred within the bounds of a given button.
     *
     * @param e the MouseEvent representing the mouse event to check
     * @param b the Button to check against
     * @return true if the mouse event occurred within the bounds of the button,
     *         false otherwise
     */
    public boolean isIn(MouseEvent e, Button b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
