package gamestates;

//Imports from within project
import main.Game;
import userinterface.MenuButton;
import utils.LoadSave;
//Imports from Javas library
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
//Imports of static variables and methods
import static main.Game.setPreviousGamestate;
import static utils.Constants.GameConstants.*;

/**
 * The Pausemenu class represents the game's pause menu screen.
 * It extends the State class and implements the StateMethods interface.
 * It contains a pause menu image, menu buttons, and methods for updating,
 * drawing, and handling mouse and keyboard input for the pausemenu.
 *
 * @author Simon Sandén
 */
public class Pausemenu extends State implements StateMethods {

    private BufferedImage pauseMenuImage;
    private int pauseMenuXPosition;
    private int pauseMenuYPosition;
    private int pauseMenuWidth;
    private int pauseMenuHeight;
    private MenuButton[] pauseMenuButtons = new MenuButton[3];
    private Playing playing;

    /**
     * Constructs a new Pausemenu object with the given Game and Playing objects.
     * Loads image for pausemenu and menubuttons.
     *
     * @param game the Game object that represents the whole game.
     * @param playing the Playing object for the game's playing state
     */
    public Pausemenu(Game game, Playing playing) {
        super(game);
        this.playing = playing;
        loadPauseMenuImage();
        loadMenuButtons();
    }

    /**
     * Loads and positions the MenuButton objects for the pause menu.
     */
    private void loadMenuButtons(){
        pauseMenuButtons[0] = new MenuButton(GAME_WIDTH / 2, (int) (150*SCALE), 0, Gamestate.PLAYING);
        pauseMenuButtons[1] = new MenuButton(GAME_WIDTH / 2, (int) (220*SCALE), 1, Gamestate.OPTIONS);
        pauseMenuButtons[2] = new MenuButton(GAME_WIDTH / 2, (int) (290*SCALE), 2, Gamestate.QUIT);
    }

    /**
     * This method gets the background image from resources-package
     * and calculates variables needed for image to be drawn in given position amd correct scale.
     */
    private void loadPauseMenuImage() {
        pauseMenuImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        pauseMenuWidth = (int) (pauseMenuImage.getWidth() * SCALE);
        pauseMenuHeight = (int) (pauseMenuImage.getHeight() * SCALE);
        pauseMenuXPosition = GAME_WIDTH / 2 - pauseMenuWidth / 2;
        pauseMenuYPosition = 50;
    }

    /**
     * This method calls each buttons update method which reacts to user interaction
     */
    @Override
    public void update() {
        for (MenuButton menuButton : pauseMenuButtons){
            menuButton.updateButtons();
        }
    }

    /**
     * This method draws the pausemenu image and menubuttons, as well as a static image of the playing state.
     *
     * @param g the Graphics object to use for drawing
     */
    @Override
    public void draw(Graphics g) {
        playing.draw(g);
        g.drawImage(pauseMenuImage, pauseMenuXPosition, pauseMenuYPosition, pauseMenuWidth, pauseMenuHeight, null);

        for (MenuButton menuButton : pauseMenuButtons){
            menuButton.drawButtons(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Responds to a mouse press event by setting the appropriate button's
     * "mouse pressed" state if the event occurred within that button's bounds.
     * @param e the MouseEvent representing the mouse press event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton menuButton : pauseMenuButtons){
            if (isUserInsideButtonBounds(e,menuButton)){
                menuButton.setMousePressed(true);
                break;
            }
        }
    }

    /**
     * This method is called when the mouse button is released. It checks if the user clicked on one of the
     * pausemenu buttons, and if so, performs the appropriate action based on the button's function. If the
     * applied gamestate is PLAYING, the game is unpaused and audio is muted. If the applied gamestate is STARTMENU,
     * the game is unpaused, audio is loaded for the menu, and the classes for the game are initialized. Finally, the
     * method resets the buttons.
     *
     * @param e the MouseEvent that triggered the method
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton menuButton : pauseMenuButtons){
            if (isUserInsideButtonBounds(e,menuButton)){
                if (menuButton.isMousePressed()){
                    setPreviousGamestate();
                    menuButton.applyGamestate();
                    checkStatesAndReactAccordingly();
                    break;
                }
            }
        }
        resetButtons();
    }

    /**
     * This method checks current state and reacts accordingly depending on state bound to menubutton
     */

    private void checkStatesAndReactAccordingly(){
        if (Gamestate.state == Gamestate.PLAYING) {
            playing.setPaused(false);
            //game.getStartmenu().silenceAudio();
        }
        else if (Gamestate.state == Gamestate.STARTMENU){
            game.getPlaying().setPaused(false);
            //game.getStartmenu().loadMenuAudio();
            game.getPlaying().restartGame();
        }
    }

    /**
     * This method is called when the mouse is moved. It sets all the pause menu buttons' mouse over value to false,
     * then checks if the mouse is inside any of the pause menu buttons. If it is, it sets that button's mouse over
     * value to true.
     *
     * @param e the MouseEvent that triggered the method
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton menuButton : pauseMenuButtons){
            menuButton.setMouseOver(false);
        }

        for (MenuButton menuButton : pauseMenuButtons){
            if (isUserInsideButtonBounds(e, menuButton)){
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    /**
     * This method is called when a key is pressed. It checks if the key pressed was the escape key, and if so,
     * unpauses the game and sets the gamestate to PLAYING.
     *
     * @param e the KeyEvent that triggered the method
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                playing.setPaused(false);
                Gamestate.state = Gamestate.PLAYING;
                resetButtons();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * This method checks if the user clicked inside a given button's boundaries.
     *
     * @param e the MouseEvent to check
     * @param mb the MenuButton to check against
     * @return true if the user clicked inside the button, false otherwise
     */
    public boolean isUserInsideButtonBounds(MouseEvent e, MenuButton mb){
        return mb.getBtnBounds().contains(e.getX(), e.getY());
    }

    /**
     * This method resets all the pausemenu buttons' booleans to their default values.
     */
    private void resetButtons(){
        for (MenuButton menuButton : pauseMenuButtons){
            menuButton.resetBtnBooleans();
        }
    }
}
