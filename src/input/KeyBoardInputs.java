package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

import static utils.Constants.Directions.*;


public class KeyBoardInputs implements KeyListener {
    private GamePanel gamePanel;
    private final int PLAYER_SPEED = 5;

    public KeyBoardInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){

            case KeyEvent.VK_LEFT:
                gamePanel.getGame().getPlayer().setMovingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                gamePanel.getGame().getPlayer().setMovingRight(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                gamePanel.getGame().getPlayer().setMovingLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                gamePanel.getGame().getPlayer().setMovingRight(false);
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
