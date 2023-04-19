/**
 * This class is responsible for the deathscreen
 * @author Linus Magnusson
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

    /**
     * Constructor for Deathscreen
     * @param game
     */
    public DeathScreen(Game game){
        super(game);
        loadDeathScreenBackground();
    }

    /**
     * This method loads the deathscreenBackground
     */
    private void loadDeathScreenBackground() {
        deathScreenBackground = LoadSave.GetSpriteAtlas(LoadSave.DEATHSCREEN);
    }

    /**
     * This method updates the deathsceen
     */
    @Override
    public void update() {
    }

    /**
     * This method draws the deathscreen
     * @param g
     */
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

    /**
     * This method checks for keys pressed and calls methods based on which key is pressed
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_R:
                game.restartGame();
                Gamestate.state = Gamestate.PLAYING;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
