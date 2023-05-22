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
 * This class represents the deathscreen
 * @author Linus Magnusson
 * @author Simon Sandén
 */
public class DeathScreen extends State implements StateMethods{
    private BufferedImage[] deathScreenGif;
    private BufferedImage youDiedText;
    private int animationTick = 0;
    private int animationIndex = 1;
    private int xPosDeathScreenGif;
    private int yPosDeathScreenGif;
    private int xPosYouDiedText;
    private int yPosYouDiedText;
    private int youDiedTextWidth;
    private int youDiedTextHeight;
    private int replayBtnXPos;
    private int replayBtnYPos;
    private File audioFile = new File(LoadSave.DEATHSCREEN_MUSIC);
    private AudioInputStream audioInputStream;
    private Clip clip;
    private MenuButton replayButton;

    /**
     * Constructor for Deathscreen
     * @param game
     */
    public DeathScreen(Game game){
        super(game);
        loadDeathScreenGif();
        loadYouDiedText();
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
     * This method loads the deathscreenGif from resources and assigns position variables
     */
    private void loadDeathScreenGif() {
        xPosDeathScreenGif = 680;
        yPosDeathScreenGif = 380;

        try {
            deathScreenGif = LoadSave.getDeathscreenGif();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and assigns variables for the "You Died" text.
     */
    private void loadYouDiedText(){
        youDiedText = LoadSave.GetSpriteAtlas(LoadSave.DEATHSCREEN_YOUDIED);
        xPosYouDiedText = 520;
        yPosYouDiedText = 80;
        youDiedTextHeight = (int) (youDiedText.getHeight() * SCALE);
        youDiedTextWidth = (int) (youDiedText.getWidth() * SCALE);
    }

    /**
     * Plays the death screen music. Also lowers the volume of the file a bit.
     */
    public void playDeathScreenMusic(){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl volumeController = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeController.setValue(-8.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Silences the audio by closing the audio input stream and stopping the clip.
     */
    public void silenceAudio(){
        try {
            audioInputStream.close();
            clip.stop();
            clip.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        audioInputStream = null;
        clip = null;
    }

    /**
     * This method updates the deathsceen
     */
    @Override
    public void update() {
        if (audioInputStream == null){
            playDeathScreenMusic();
        }
        updateAnimationTick();
        replayButton.updateButtons();
    }

    /**
     * This method draws the deathscreen
     * @param g
     */
    public void draw(Graphics g){
        game.getPlaying().draw(g);

        g.drawImage(deathScreenGif[animationIndex], xPosDeathScreenGif, yPosDeathScreenGif,null);
        g.drawImage(youDiedText, xPosYouDiedText, yPosYouDiedText, youDiedTextWidth, youDiedTextHeight, null);
        replayButton.drawButtons(g);
    }

    /**
     * Updates the animation tick and index.
     */
    private void updateAnimationTick(){
        animationTick++;
        if (animationTick>=50){
            animationIndex++;
            animationTick = 0;
        }
        if (animationIndex>=11){
            animationIndex = 1;
        }
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
     * and if so, it sets the previous gamestate, applies the new gamestate, and silences the audio.
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
                silenceAudio();
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
     * This method checks for keys pressed and calls methods based on which key is pressed
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_R:
                game.restartGame();
                silenceAudio();
                Gamestate.state = Gamestate.PLAYING;
                break;
        }
    }

    /**
     * This method resets the booleans for replayButton.
     */
    private void resetButton(){
        replayButton.resetBtnBooleans();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
