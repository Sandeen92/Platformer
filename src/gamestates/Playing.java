package gamestates;

import entity.Player;
import levels.LevelManager;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods{

    private Player player;
    private LevelManager levelManager;
    private boolean paused;


    public Playing(Game game){
        super(game);
        initClasses();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200,200, (int) (64 * Game.SCALE),(int)(40 * Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
    }


    @Override
    public void update() {
        levelManager.updateLevel();
        player.updatePlayer();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.drawLevel(g);
        player.renderPlayer(g);
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

            case KeyEvent.VK_A:
                player.setMovingLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setMovingRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(true);
                break;
            case KeyEvent.VK_SHIFT:
                player.setMovingDown(true);
                break;
            case KeyEvent.VK_ESCAPE:
                Gamestate.state = Gamestate.MENU;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setMovingLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setMovingRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(false);
                break;
            case KeyEvent.VK_SHIFT:
                player.setMovingDown(false);
                break;
        }
    }


    public void windowFocusLost(){
        player.allMovingBooleansFalse();
    }
    public Player getPlayer(){
        return player;
    }
}
