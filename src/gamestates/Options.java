package gamestates;

import main.Game;
import userinterface.MenuButton;
import userinterface.OptionButton;
import userinterface.SoundButton;
import utils.Constants;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constants.UserInterface.OptionButtons.OPTIONBTN_SIZE;

public class Options extends State implements StateMethods {

    private BufferedImage optionsBackgroundImage;
    private int optionsMenuXPos;
    private int optionsMenuYPos;
    private int optionsMenuWidth;
    private int optionsMenuHeight;
    private OptionButton optionButton;
    private SoundButton[] soundButtons = new SoundButton[2];


    public Options(Game game){
        super(game);
        loadBackgroundImage();
        loadOptionButtons();
    }

    private void loadOptionButtons(){
        int optionBtnX = ((Game.GAME_WIDTH / 2) + 50);
        int optionBtnY = (int) (340 * Game.SCALE);

        optionButton = new OptionButton(optionBtnX,optionBtnY, OPTIONBTN_SIZE, OPTIONBTN_SIZE, 2);
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
        optionButton.updateButtons();
    }

    public void draw(Graphics g){
        if (game.getPlaying().isPaused() == true){
            game.getPlaying().draw(g);
        }
        g.drawImage(optionsBackgroundImage, optionsMenuXPos, optionsMenuYPos, optionsMenuWidth, optionsMenuHeight, null);
        optionButton.drawButtons(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isUserInsideBtnBounds(e, optionButton)){
            optionButton.setMousePressed(true);
        }
        else {

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isUserInsideBtnBounds(e, optionButton)){
            if (optionButton.isMousePressed()){
                Gamestate.state = Gamestate.PAUSE;
            }
        }
        optionButton.resetBtnBooleans();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        optionButton.setMouseOver(false);

        if (isUserInsideBtnBounds(e, optionButton)){
            optionButton.setMouseOver(true);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isUserInsideBtnBounds(MouseEvent e, OptionButton ob){
        return ob.getBtnBounds().contains(e.getX(), e.getY());
    }
}
