package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;


public class KeyBoardInputs implements KeyListener {
    private GamePanel gamePanel;
    private final int PLAYER_SPEED = 5;

    public KeyBoardInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state){

            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;

            case STARTMENU:
                gamePanel.getGame().getStartmenu().keyPressed(e);
                break;

            case PAUSEMENU:
                gamePanel.getGame().getPausemenu().keyPressed(e);
                break;

            case DEATHSCREEN:
                gamePanel.getGame().getDeathScreen().keyPressed(e);

            case OPTIONS:
                gamePanel.getGame().getOptions().keyPressed(e);
                break;

            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state){

            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;

            case STARTMENU:
                gamePanel.getGame().getStartmenu().keyReleased(e);
                break;

            case PAUSEMENU:
                gamePanel.getGame().getPausemenu().keyReleased(e);
                break;

            case OPTIONS:
                gamePanel.getGame().getOptions().keyReleased(e);
                break;

            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
