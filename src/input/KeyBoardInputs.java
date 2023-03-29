package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

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
                gamePanel.changeXDelta(-PLAYER_SPEED);
                break;
            case KeyEvent.VK_RIGHT:
                gamePanel.changeXDelta(PLAYER_SPEED);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
