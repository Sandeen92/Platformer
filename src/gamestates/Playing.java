package gamestates;

import entity.EnemyManager;
import entity.Player;
import items.ItemManager;
import levels.LevelManager;
import main.Game;
import userinterface.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods{

    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private EnemyManager enemyManager;
    private ItemManager itemManager;
    private boolean paused;
    private int currentLevelOffsetX;
    private int cameraLeftBorder = (int) (0.3 * Game.GAME_WIDTH);
    private int cameraRightBorder = (int) (0.7 * Game.GAME_WIDTH);
    private int levelTilesWidth = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = levelTilesWidth - Game.TILES_IN_WIDTH;
    private int maxLevelOffsetX = maxTilesOffset * Game.TILES_SIZE;

    public Playing(Game game){
        super(game);
        initClasses();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        itemManager = new ItemManager(this);
        player = new Player(200,200, (int) (64 * Game.SCALE),(int)(40 * Game.SCALE), 10, 2);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        pauseOverlay = new PauseOverlay(this);
    }


    @Override
    public void update() {
        if (paused == false) {
            levelManager.updateLevel();
            itemManager.update();
            player.updatePlayer();
            checkIfPlayerIsCloseToCameraBorder();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData());
        }
        else {
            pauseOverlay.update();
        }
    }

    /**
     * This method checks if the player is close to the edge of the screen.
     */
    private void checkIfPlayerIsCloseToCameraBorder() {
        int playerPositionX = (int) player.getHitbox().x;
        int currentPlayerPositionX = playerPositionX - currentLevelOffsetX;

        if (currentPlayerPositionX > cameraRightBorder){
            currentLevelOffsetX += currentPlayerPositionX - cameraRightBorder;
        }
        else if (currentPlayerPositionX < cameraLeftBorder){
            currentLevelOffsetX += currentPlayerPositionX - cameraLeftBorder;
        }

        if (currentLevelOffsetX > maxLevelOffsetX){
            currentLevelOffsetX = maxLevelOffsetX;
        }
        else if (currentLevelOffsetX < 0){
            currentLevelOffsetX = 0;
        }
    }

    @Override
    public void draw(Graphics g) {

        levelManager.drawLevel(g, currentLevelOffsetX);
        player.renderPlayer(g, currentLevelOffsetX);
        enemyManager.draw(g, currentLevelOffsetX);
        itemManager.draw(g, currentLevelOffsetX);

        if (paused == true) {
            pauseOverlay.draw(g);
        }

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

            case KeyEvent.VK_ESCAPE:
                if (paused == false) {
                    paused = true;
                }
                else if (paused == true){
                    paused = false;
                }
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
                player.resetJumpOnce();
                break;
        }
    }


    public void windowFocusLost(){
        player.allMovingBooleansFalse();
    }
    public Player getPlayer(){
        return player;
    }

    public ItemManager getItemManager() {
        return itemManager;
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

}
