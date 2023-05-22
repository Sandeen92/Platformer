package input;

import gamestates.Gamestate;
import main.GamePanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class is responsible for handling all the mouse inputs and motions done by User.
 * @author Linus Magnusson
 * @author Casper Johannesson
 * @author Simon Sandén
 */
public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }


    /**
     * Handles the mouse clicked event. Has different purposes depending on current gamestate.
     *
     * @param e the MouseEvent representing the mouse click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state){

            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;

            case STARTMENU:
                gamePanel.getGame().getStartmenu().mouseClicked(e);
                break;

            case PAUSEMENU:
                gamePanel.getGame().getPausemenu().mouseClicked(e);
                break;

            case OPTIONS:
                gamePanel.getGame().getOptions().mouseClicked(e);
                break;

            case DEATHSCREEN:
                gamePanel.getGame().getDeathScreen().mouseClicked(e);
                break;

            case LEVELCOMPLETED:
                gamePanel.getGame().getLevelCompleted().mouseClicked(e);
                break;

            default:
                break;
        }
    }

    /**
     * Handles the mouse moved event. Has different purposes depending on current gamestate.
     *
     * @param e the mouse event representing the mouse movement
     */
    public void mouseMoved(MouseEvent e){
        switch (Gamestate.state){

            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;

            case STARTMENU:
                gamePanel.getGame().getStartmenu().mouseMoved(e);
                break;

            case PAUSEMENU:
                gamePanel.getGame().getPausemenu().mouseMoved(e);
                break;

            case OPTIONS:
                gamePanel.getGame().getOptions().mouseMoved(e);
                break;

            case DEATHSCREEN:
                gamePanel.getGame().getDeathScreen().mouseMoved(e);
                break;

            case LEVELCOMPLETED:
                gamePanel.getGame().getLevelCompleted().mouseMoved(e);
                break;

            default:
                break;
        }
    }

    /**
     * Handles the mouse pressed event. Has different purposes depending on current gamestate.
     *
     * @param e the mouse event representing the mouse pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state){

            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;

            case STARTMENU:
                gamePanel.getGame().getStartmenu().mousePressed(e);
                break;

            case PAUSEMENU:
                gamePanel.getGame().getPausemenu().mousePressed(e);
                break;

            case OPTIONS:
                gamePanel.getGame().getOptions().mousePressed(e);
                break;

            case DEATHSCREEN:
                gamePanel.getGame().getDeathScreen().mousePressed(e);
                break;

            case LEVELCOMPLETED:
                gamePanel.getGame().getLevelCompleted().mousePressed(e);
                break;

            default:
                break;
        }
    }

    /**
     * Handles the mouse released event. Has different purposes depending on current gamestate.
     *
     * @param e the mouse event representing the mouse released
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state){

            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;

            case STARTMENU:
                gamePanel.getGame().getStartmenu().mouseReleased(e);
                break;

            case PAUSEMENU:
                gamePanel.getGame().getPausemenu().mouseReleased(e);
                break;

            case OPTIONS:
                gamePanel.getGame().getOptions().mouseReleased(e);
                break;

            case DEATHSCREEN:
                gamePanel.getGame().getDeathScreen().mouseReleased(e);
                break;

            case LEVELCOMPLETED:
                gamePanel.getGame().getLevelCompleted().mouseReleased(e);
                break;

            default:
                break;
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
