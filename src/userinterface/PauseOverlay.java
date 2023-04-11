package userinterface;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.State;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay {

    private BufferedImage pauseMenuImage;
    private int pauseMenuXPos;
    private int pauseMenuYPos;
    private int pauseMenuWidth;
    private int pauseMenuHeight;
    private MenuButton[] menuButtons = new MenuButton[3];
    private Playing playing;

    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadPauseMenuImage();
        loadMenuButtons();
    }

    private void loadMenuButtons(){
        menuButtons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (210*Game.SCALE), 0, Gamestate.PLAYING);
        menuButtons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (260*Game.SCALE), 1, Gamestate.QUIT);
        menuButtons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (310*Game.SCALE), 2, Gamestate.OPTIONS);
    }

    private void loadPauseMenuImage() {
        pauseMenuImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        pauseMenuWidth = (int) (pauseMenuImage.getWidth() * Game.SCALE);
        pauseMenuHeight = (int) (pauseMenuImage.getHeight() * Game.SCALE);
        pauseMenuXPos = Game.GAME_WIDTH / 2 - pauseMenuWidth / 2;
        pauseMenuYPos = 0;
    }

    public void update(){
        for (MenuButton mb : menuButtons){
            mb.updateButtons();
        }
    }

    public void draw(Graphics g){
        g.drawImage(pauseMenuImage, pauseMenuXPos, pauseMenuYPos, pauseMenuWidth, pauseMenuHeight, null);

        for (MenuButton mb : menuButtons){
            mb.drawButtons(g);
        }
    }

    public void mouseDragged(MouseEvent e){
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : menuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : menuButtons){
            if (isUserInsideBtnBounds(e,mb)){
                if (mb.isMousePressed()){
                    mb.applyGamestate();
                    break;
                }
            }
        }
        resetButtons();
    }
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


    public boolean isUserInsideBtnBounds(MouseEvent e, MenuButton mb){
        return mb.getBtnBounds().contains(e.getX(), e.getY());
    }

    private void resetButtons(){
        for (MenuButton mb : menuButtons){
            mb.resetBtnBooleans();
        }
    }
}
