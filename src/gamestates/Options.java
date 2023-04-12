package gamestates;

import main.Game;
import userinterface.OptionButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Options extends State implements StateMethods {

    private BufferedImage optionsBackgroundImage;
    private int optionsMenuXPos;
    private int optionsMenuYPos;
    private int optionsMenuWidth;
    private int optionsMenuHeight;
    private OptionButton[] optionButtons;


    public Options(Game game){
        super(game);
        loadBackgroundImage();
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

    }

    public void draw(Graphics g){
        if (game.getPlaying().isPaused() == true){
            game.getPlaying().draw(g);
        }
        g.drawImage(optionsBackgroundImage, optionsMenuXPos, optionsMenuYPos, optionsMenuWidth, optionsMenuHeight, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
