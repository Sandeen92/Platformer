package gamestates;

import main.Game;
import userinterface.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods{


    private MenuButton[] menuButtons = new MenuButton[3];
    private BufferedImage menuBackground;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadMenuButtons();
        loadMenuBackground();
    }

    private void loadMenuBackground(){
        menuBackground = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (menuBackground.getWidth() - Game.SCALE);
        menuHeight = (int) (menuBackground.getHeight() - Game.SCALE);
        menuX = Game.GAME_WIDTH/2 - menuWidth/2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadMenuButtons(){
        menuButtons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150*Game.SCALE), 0, Gamestate.PLAYING);
        menuButtons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220*Game.SCALE), 1, Gamestate.QUIT);
        menuButtons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290*Game.SCALE), 2, Gamestate.OPTIONS);
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
                    mb.applyGamestate();
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
        if (e.getKeyCode()== KeyEvent.VK_ESCAPE){
            Gamestate.state = Gamestate.PLAYING;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}


    private void resetButtons(){
        for (MenuButton mb : menuButtons){
            mb.resetBtnBooleans();
        }
    }

}
