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

public class Startmenu extends State implements StateMethods{


    private MenuButton[] menuButtons = new MenuButton[3];
    private BufferedImage menuBackground;
    private int menuX, menuY, menuWidth, menuHeight;
    private File audioFile;
    private AudioInputStream audioInputStream;
    private Clip clip;

    public Startmenu(Game game) {
        super(game);
        loadMenuButtons();
        loadMenuBackground();
        loadMenuAudio();
    }

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

    private void loadMenuBackground(){
        menuBackground = LoadSave.GetSpriteAtlas(LoadSave.STARTMENU_BACKGROUND);
        menuWidth = (int) (menuBackground.getWidth() - Game.SCALE);
        menuHeight = (int) (menuBackground.getHeight() - Game.SCALE);
        menuX =  30; //Game.GAME_WIDTH/2 - menuWidth/2;
        menuY =  0; //(int) (45 * Game.SCALE);
    }

    private void loadMenuButtons(){
        menuButtons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (230*Game.SCALE), 0, Gamestate.PLAYING);
        menuButtons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (300*Game.SCALE), 1, Gamestate.OPTIONS);
        menuButtons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (370*Game.SCALE), 2, Gamestate.QUIT);
    }

    public void silenceAudio(){
        try {
            audioInputStream.close();
            clip.stop();
            clip.close();
            //clip = null;
            //audioInputStream = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        for (MenuButton mb : menuButtons){
            mb.updateButtons();
        }
    }
    @Override
    public void draw(Graphics g) {

        g.drawImage(menuBackground, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : menuButtons){
            mb.drawButtons(g);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : menuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }
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


    private void resetButtons(){
        for (MenuButton mb : menuButtons){
            mb.resetBtnBooleans();
        }
    }

}
