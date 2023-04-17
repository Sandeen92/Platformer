package gamestates;

import main.Game;
import userinterface.OptionButton;
import userinterface.Button;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.setPreviousGamestate;
import static utils.Constants.UserInterface.OptionButtons.OPTIONBTN_SIZE;

public class Options extends State implements StateMethods {

    private BufferedImage optionsBackgroundImage;
    private int optionsMenuXPos;
    private int optionsMenuYPos;
    private int optionsMenuWidth;
    private int optionsMenuHeight;
    private OptionButton returnButton, homeButton;


    public Options(Game game){
        super(game);
        loadBackgroundImage();
        loadOptionButtons();
    }

    private void loadOptionButtons(){
        int optionBtnX = ((Game.GAME_WIDTH / 3));
        int optionBtnY = (int) (340 * Game.SCALE);

        returnButton = new OptionButton(optionBtnX,optionBtnY, OPTIONBTN_SIZE, OPTIONBTN_SIZE, 0);
        homeButton = new OptionButton(optionBtnX + 430, optionBtnY, OPTIONBTN_SIZE, OPTIONBTN_SIZE, 1);
    }

    private void loadBackgroundImage(){
        optionsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND);
        optionsMenuWidth = (int) (optionsBackgroundImage.getWidth() * Game.SCALE);
        optionsMenuHeight = (int) (optionsBackgroundImage.getHeight() * Game.SCALE);
        optionsMenuXPos = Game.GAME_WIDTH / 2 - optionsMenuWidth / 2;
        optionsMenuYPos = 50;
    }

    @Override
    public void update() {
        returnButton.updateButtons();
        homeButton.updateButtons();
    }

    public void draw(Graphics g){
        if (game.getPlaying().isPaused() == true){
            game.getPlaying().draw(g);
        }
        else {
            game.getMenu().draw(g);
        }

        g.drawImage(optionsBackgroundImage, optionsMenuXPos, optionsMenuYPos, optionsMenuWidth, optionsMenuHeight, null);
        returnButton.drawButtons(g);
        if (Gamestate.previousState != Gamestate.STARTMENU) {
            homeButton.drawButtons(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, returnButton)){
            returnButton.setMousePressed(true);
        }
        else if (isIn(e, homeButton)) {
            homeButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, returnButton)){
            if (returnButton.isMousePressed()){
                if (Gamestate.previousState == Gamestate.PAUSEMENU) {
                    setPreviousGamestate();
                    Gamestate.state = Gamestate.PAUSEMENU;
                }
                else if (Gamestate.previousState == Gamestate.STARTMENU){
                    setPreviousGamestate();
                    Gamestate.state = Gamestate.STARTMENU;
                    game.getPlaying().setPaused(false);
                    game.getMenu().loadMenuAudio();
                    game.getPlaying().initClasses();
                }
            }
        }
        else if (isIn(e, homeButton)){
            if (homeButton.isMousePressed()){
                if (Gamestate.previousState != Gamestate.STARTMENU) {
                    Gamestate.state = Gamestate.STARTMENU;
                    game.getPlaying().setPaused(false);
                    game.getMenu().replayStartmenuAudio();
                    game.getPlaying().initClasses();
                }
            }
        }
        returnButton.resetBtnBooleans();
        homeButton.resetBtnBooleans();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        returnButton.setMouseOver(false);

        if (isIn(e, returnButton)){
            returnButton.setMouseOver(true);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            Gamestate.state = Gamestate.PAUSEMENU;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            Gamestate.state = Gamestate.PAUSEMENU;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {}


    public boolean isIn(MouseEvent e, Button b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
