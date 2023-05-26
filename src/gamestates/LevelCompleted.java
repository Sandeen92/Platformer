
package gamestates;
//Imports from within project
import main.Game;
import userinterface.MenuButton;
import utils.LoadSave;
//Imports from Javas library
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//Imports of static variables and methods
import static main.Game.setPreviousGamestate;
import static utils.Constants.GameConstants.GAME_WIDTH;
import static utils.Constants.GameConstants.SCALE;

/**
 * This class represents the Level Completed screen
 * @author Casper Johannesson
 * @author Simon Sandén
 */
public class LevelCompleted extends State implements StateMethods{

    private int replayBtnXPos;
    private int replayBtnYPos;
    private MenuButton replayButton;
    private BufferedImage levelCompletedText;
    private int xPosLevelCompletedText;
    private int yPosLevelCompletedText;
    private int levelCompletedTextWidth;
    private int levelCompletedTextHeight;
    private File audioFile;
    private AudioInputStream audioInputStream;
    private Clip clip;
    private boolean audioPlayedOnce;

    /**
     * Constructor for LevelCompleted
     * @param game
     */
    public LevelCompleted(Game game) {
        super(game);
        loadLevelCompletedText();
        loadReplayButton();
    }

    /**
     * Loads and positions the replay button for the game.
     */
    private void loadReplayButton() {
        replayBtnXPos = GAME_WIDTH / 2;
        replayBtnYPos = 240;
        replayButton = new MenuButton(replayBtnXPos,replayBtnYPos,Gamestate.PLAYING);
    }

    /**
     * Loads and assigns variables for the "Level Completed" text.
     */
    private void loadLevelCompletedText(){
        levelCompletedText = LoadSave.GetSpriteAtlas(LoadSave.LEVELCOMPLETED_TEXT);
        xPosLevelCompletedText = 520;
        yPosLevelCompletedText = 80;
        levelCompletedTextHeight = (int) (levelCompletedText.getHeight() * SCALE);
        levelCompletedTextWidth = (int) (levelCompletedText.getWidth() * SCALE);
    }


    public void loadLevelCompletedAudio(){
        audioFile = new File(LoadSave.LEVELCOMPLETED_AUDIO);
        try {
            if (audioFile != null) {
                audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(0);
                audioPlayedOnce = true;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates the Level Completed screen
     */
    @Override
    public void update() {
        replayButton.updateButtons();
        if (audioPlayedOnce == false){
            game.getPlaying().silenceAudio();
            loadLevelCompletedAudio();
        }
    }

    /**
     * This method draws the Level Completed screen
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        game.getPlaying().draw(g);

        g.drawImage(levelCompletedText, xPosLevelCompletedText, yPosLevelCompletedText, levelCompletedTextWidth, levelCompletedTextHeight, null);
        replayButton.drawButtons(g);
    }


    /**
     * Responds to a mouse press event by setting the appropriate button's
     * "mouse pressed" state if the event occurred within that button's bounds.
     * @param e the MouseEvent representing the mouse press event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (isUserInsideButtonBounds(e,replayButton)){
            replayButton.setMousePressed(true);
        }
    }

    /**
     * This method is called when the user releases a mouse button. It checks if the mouse was inside any of the buttons,
     * and if so, it sets the previous gamestate and applies the new gamestate.
     *
     * @param e the MouseEvent representing the event that occurred
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isUserInsideButtonBounds(e,replayButton)){
            if (replayButton.isMousePressed()){
                game.restartGame();
                setPreviousGamestate();
                replayButton.applyGamestate();
                audioPlayedOnce = false;
            }
        }
        resetButton();
    }

    /**
     * This method is called when the user moves the mouse. It sets all menu button's "mouseOver" booleans to false,
     * then checks if the mouse is inside any of the menu buttons, and if so, it sets the "mouseOver" boolean of that button to true.
     *
     * @param e the MouseEvent representing the event that occurred
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        replayButton.setMouseOver(false);

        if (isUserInsideButtonBounds(e,replayButton)){
            replayButton.setMouseOver(true);
        }
    }

    /**
     * This method resets the booleans for replayButton.
     */
    private void resetButton(){
        replayButton.resetBtnBooleans();
    }

    /**
     * This method checks for keys pressed and calls methods based on which key is pressed
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_R:
                game.restartGame();
                Gamestate.state = Gamestate.PLAYING;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


}
