package gamestates;

import main.Game;
import userinterface.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Pausemenu extends State implements StateMethods {

    private BufferedImage pauseMenuImage;
    private int pauseMenuXPos;
    private int pauseMenuYPos;
    private int pauseMenuWidth;
    private int pauseMenuHeight;
    private MenuButton[] pauseMenuButtons = new MenuButton[3];
    private Playing playing;

    public Pausemenu(Game game, Playing playing) {
        super(game);
        this.playing = playing;
        loadPauseMenuImage();
        loadMenuButtons();
    }

    private void loadMenuButtons(){
        pauseMenuButtons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150*Game.SCALE), 0, Gamestate.PLAYING);
        pauseMenuButtons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220*Game.SCALE), 1, Gamestate.OPTIONS);
        pauseMenuButtons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290*Game.SCALE), 2, Gamestate.QUIT);
    }

    private void loadPauseMenuImage() {
        pauseMenuImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        pauseMenuWidth = (int) (pauseMenuImage.getWidth() * Game.SCALE);
        pauseMenuHeight = (int) (pauseMenuImage.getHeight() * Game.SCALE);
        pauseMenuXPos = Game.GAME_WIDTH / 2 - pauseMenuWidth / 2;
        pauseMenuYPos = 50;
    }


    @Override
    public void update() {
        for (MenuButton mb : pauseMenuButtons){
            mb.updateButtons();
        }
    }

    @Override
    public void draw(Graphics g) {
        playing.draw(g);
        g.drawImage(pauseMenuImage, pauseMenuXPos, pauseMenuYPos, pauseMenuWidth, pauseMenuHeight, null);

        for (MenuButton mb : pauseMenuButtons){
            mb.drawButtons(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : pauseMenuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : pauseMenuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                if (mb.isMousePressed()){
                    mb.applyGamestate();
                    if (Gamestate.state == Gamestate.PLAYING) {
                        playing.setPaused(false);
                    }
                    break;
                }
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : pauseMenuButtons){
            mb.setMouseOver(false);
        }

        for (MenuButton mb : pauseMenuButtons){
            if (isUserInsideBtnBounds(e, mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                playing.setPaused(false);
                Gamestate.state = Gamestate.PLAYING;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isUserInsideBtnBounds(MouseEvent e, MenuButton mb){
        return mb.getBtnBounds().contains(e.getX(), e.getY());
    }

    private void resetButtons(){
        for (MenuButton mb : pauseMenuButtons){
            mb.resetBtnBooleans();
        }
    }
}
