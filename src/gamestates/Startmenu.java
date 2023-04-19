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
import static utils.Constants.GameConstants.*;

/**
 * The Startemenu class represents the game's starting menu screen.
 * It extends the State class and implements the StateMethods interface.
 * It contains a start menu image, menu buttons, and methods for updating,
 * drawing, and handling mouse and keyboard input for the startmenu.
 *
 * @author Simon Sandén
 */
public class Startmenu extends State implements StateMethods{


    private MenuButton[] menuButtons = new MenuButton[3];
    private BufferedImage menuBackground;
    private int menuX;
    private int menuY;
    private int menuWidth;
    private int menuHeight;
    private File audioFile;
    private AudioInputStream audioInputStream;
    private Clip clip;

    /**
     * Constructs a new Startmenu object with the given Game object.
     * Loads image for startmenu, menubuttons and the audio to be played.
     *
     * @param game the Game object that represents the whole game.
     */
    public Startmenu(Game game) {
        super(game);
        loadMenuButtons();
        loadMenuBackground();
        loadMenuAudio();
    }

    /**
     * This method loads the menu audio file and loops it continuously in the background.
     * Contains necessary checks so audio won't overlap when entering different gamestates.
     */
    public void loadMenuAudio(){
        if (clip == null) {
            if (Gamestate.previousState != Gamestate.STARTMENU) {
                audioFile = null;
                audioInputStream = null;
                clip = null;
            }
            audioFile = new File(LoadSave.STARTMENU_MUSIC);
            try {
                if (audioFile != null) {
                    audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method replays the start menu audio file.
     */
    public void replayStartmenuAudio(){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * This method gets the background image from resources-package
     * and calculates variables needed for image to be drawn in given position amd correct scale.
     */
    private void loadMenuBackground(){
        menuBackground = LoadSave.GetSpriteAtlas(LoadSave.STARTMENU_BACKGROUND);
        menuWidth = (int) (menuBackground.getWidth() - SCALE);
        menuHeight = (int) (menuBackground.getHeight() - SCALE);
        menuX =  30;
        menuY =  0;
    }

    /**
     * Loads and positions the MenuButton objects for the pause menu.
     */
    private void loadMenuButtons(){
        menuButtons[0] = new MenuButton(GAME_WIDTH / 2, (int) (230*SCALE), 0, Gamestate.PLAYING);
        menuButtons[1] = new MenuButton(GAME_WIDTH / 2, (int) (300*SCALE), 1, Gamestate.OPTIONS);
        menuButtons[2] = new MenuButton(GAME_WIDTH / 2, (int) (370*SCALE), 2, Gamestate.QUIT);
    }

    /**
     * This method stops the audio playback.
     */
    public void silenceAudio(){
        try {
            audioInputStream.close();
            clip.stop();
            clip.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method calls each buttons update method which reacts to user interaction
     */
    @Override
    public void update() {
        for (MenuButton mb : menuButtons){
            mb.updateButtons();
        }
    }

    /**
     * This method draws the startmenu's background image and menubuttons.
     *
     * @param g the Graphics object to use for drawing
     */
    @Override
    public void draw(Graphics g) {

        g.drawImage(menuBackground, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : menuButtons){
            mb.drawButtons(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Responds to a mouse press event by setting the appropriate button's
     * "mouse pressed" state if the event occurred within that button's bounds.
     * @param e the MouseEvent representing the mouse press event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : menuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }

    /**
     * This method is called when the user releases a mouse button. It checks if the mouse was inside any of the menu buttons,
     * and if so, it sets the previous gamestate, applies the new gamestate, and silences the audio if the gamestate is set to "PLAYING".
     *
     * @param e the MouseEvent representing the event that occurred
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : menuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                if (mb.isMousePressed()){
                    setPreviousGamestate();
                    mb.applyGamestate();
                    if (Gamestate.state == Gamestate.PLAYING){
                        silenceAudio();
                    }
                    break;
                }
            }
        }
        resetButtons();
    }

    /**
     * This method is called when the user moves the mouse. It sets all menu button's "mouseOver" booleans to false,
     * then checks if the mouse is inside any of the menu buttons, and if so, it sets the "mouseOver" boolean of that button to true.
     *
     * @param e the MouseEvent representing the event that occurred
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : menuButtons){
            mb.setMouseOver(false);
        }

        for (MenuButton mb : menuButtons){
            if (isUserInsideBtnBounds(e, mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * This method resets all the startmenu buttons' booleans to their default values.
     */
    private void resetButtons(){
        for (MenuButton mb : menuButtons){
            mb.resetBtnBooleans();
        }
    }

}
