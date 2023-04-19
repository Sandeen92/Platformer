/**
 * This class is re
 */
package gamestates;

import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DeathScreen extends State implements StateMethods{
    private BufferedImage deathScreenBackground;

    public DeathScreen(Game game){
        super(game);
        loadDeathScreenBackground();
    }

    private void loadDeathScreenBackground() {
        deathScreenBackground = LoadSave.GetSpriteAtlas(LoadSave.DEATHSCREEN);
    }

    @Override
    public void update() {

    }

    public void draw(Graphics g){
        g.drawImage(deathScreenBackground,0,0,null);
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
        switch (e.getKeyCode()){
            case KeyEvent.VK_R:
                game.restartGane();
                Gamestate.state = Gamestate.PLAYING;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
