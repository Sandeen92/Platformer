/**
 * This class is responsible for the deathscreen
 * @author Linus Magnusson
 */
package gamestates;

import main.Game;
import userinterface.MenuButton;
import utils.LoadSave;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static main.Game.setPreviousGamestate;
import static utils.Constants.GameConstants.GAME_WIDTH;
import static utils.Constants.GameConstants.SCALE;

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
    int replayBtnXPos;
    int replayBtnYPos;
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

    private void loadReplayButton() {
        replayBtnXPos = GAME_WIDTH / 2;
        replayBtnYPos = 240;
        replayButton = new MenuButton(replayBtnXPos,replayBtnYPos,Gamestate.PLAYING);
    }

    /**
     * This method loads the deathscreenBackground
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

    private void loadYouDiedText(){
        youDiedText = LoadSave.GetSpriteAtlas(LoadSave.DEATHSCREEN_YOUDIED);
        xPosYouDiedText = 520;
        yPosYouDiedText = 80;
        youDiedTextHeight = (int) (youDiedText.getHeight() * SCALE);
        youDiedTextWidth = (int) (youDiedText.getWidth() * SCALE);
    }

    public void playDeathScreenMusic(){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

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
        handleAnimation();
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


    private void handleAnimation(){
        animationTick++;
        if (animationTick>=50){
            animationIndex++;
            animationTick = 0;
        }
        if (animationIndex>=11){
            animationIndex = 1;
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isUserInsideButtonBounds(e,replayButton)){
            replayButton.setMousePressed(true);
        }
    }

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

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void resetButton(){
        replayButton.resetBtnBooleans();
    }
}
