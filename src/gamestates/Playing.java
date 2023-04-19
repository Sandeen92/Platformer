package gamestates;
//Imports from within project
import entity.EnemyManager;
import entity.InteractablesManager;
import entity.Player;
import items.ItemManager;
import levels.LevelManager;
import main.Game;
//Imports from Javas library
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * The Playing class represents the state of the game when the player is actively playing.
 * It extends the abstract class State and implements the StateMethods interface.
 * It manages the player, level, enemy, item, and interactable managers, as well as the camera offset and pause state.
 *
 * @author Simon Sandén
 * @author Linus Magnusson
 * @author Casper Johannesson
 */
public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ItemManager itemManager;
    private InteractablesManager interactablesManager;
    private boolean paused;
    private int currentLevelOffsetX;
    private int cameraLeftBorder = (int) (0.3 * Game.GAME_WIDTH);
    private int cameraRightBorder = (int) (0.7 * Game.GAME_WIDTH);
    private int maxLevelOffsetX;

    /**
     * Constructor for the Playing class.
     * Initializes the class variables, calculates the level offset, and loads the starting level.
     * @param game the Game object that represents the whole game.
     */
    public Playing(Game game) {
        super(game);
        initClasses();
        calculatingLevelOffset();
        loadStartLevel();
    }

    /**
     * This method loads the next level in the game.
     */
    public void loadNextLevel(){
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    /**
     * This method loads the starting level in the game.
     */
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        interactablesManager.loadBoxes(levelManager.getCurrentLevel());
    }

    /**
     * This method calculates the maximum level offset.
     */
    private void calculatingLevelOffset() {
        maxLevelOffsetX = levelManager.getCurrentLevel().getMaxLevelOffsetX();
    }

    /**
     * This method initializes the player, level, enemy, item, and interactable managers.
     * Also loads the leveldata and spawnpoint into Player object.
     */
    public void initClasses() {
        enemyManager = new EnemyManager(this);
        player = new Player(200,200, (int) (70 * Game.SCALE),(int)(45 * Game.SCALE), 10, 2, enemyManager);
        levelManager = new LevelManager(game);
        itemManager = new ItemManager(this);
        interactablesManager = new InteractablesManager(this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    /**
     * This method restarts the game.
     */
    public void restartGame(){
        initClasses();
        loadStartLevel();
    }

    /**
     * This method updates the level, item, player, interactable, and enemy managers.
     * Also updates cameraview if player is close to the set border.
     * If game is paused, no updates will be done.
     */
    @Override
    public void update() {
        if (paused == false) {
            levelManager.updateLevel();
            itemManager.update();
            player.updatePlayer();
            interactablesManager.update();
            checkIfPlayerIsCloseToCameraBorder();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData());
        }
    }

    /**
     * This method checks if the player is close to the edge of the screen.
     */
    private void checkIfPlayerIsCloseToCameraBorder() {
        int playerPositionX = (int) player.getHitbox().x;
        int currentPlayerPositionX = playerPositionX - currentLevelOffsetX;

        if (currentPlayerPositionX > cameraRightBorder) {
            currentLevelOffsetX += currentPlayerPositionX - cameraRightBorder;
        } else if (currentPlayerPositionX < cameraLeftBorder) {
            currentLevelOffsetX += currentPlayerPositionX - cameraLeftBorder;
        }

        if (currentLevelOffsetX > maxLevelOffsetX) {
            currentLevelOffsetX = maxLevelOffsetX;
        } else if (currentLevelOffsetX < 0) {
            currentLevelOffsetX = 0;
        }
    }

    /**
     * This method draws the images of all the objects within the PLAYING state.
     * @param g the Graphics object to use for drawing
     */
    @Override
    public void draw(Graphics g) {
        levelManager.drawLevel(g, currentLevelOffsetX);
        player.renderPlayer(g, currentLevelOffsetX);
        enemyManager.draw(g, currentLevelOffsetX);
        itemManager.draw(g, currentLevelOffsetX);
        interactablesManager.draw(g, currentLevelOffsetX);
    }

    /**
     * Responds to the key being pressed down by the user and sets appropriate player movement,
     * attack, and game state based on the input.
     *
     * @param e the KeyEvent object that is generated when a key is pressed down
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            case KeyEvent.VK_A:
                player.setMovingLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setMovingRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(true);
                break;
            case KeyEvent.VK_F:
                player.attack();
                break;
            case KeyEvent.VK_R:
                restartGame();
                break;
            case KeyEvent.VK_ESCAPE:
                paused = true;
                player.setJumping(false);
                Gamestate.state = Gamestate.PAUSEMENU;
                break;
        }
    }


    /**
     * Responds to the key being released by the user and sets appropriate player movement based on the input.
     *
     * @param e the KeyEvent object that is generated when a key is released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
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

    /**
     * Returns a boolean indicating if the game is currently paused.
     * @return boolean representing if the game is paused or not
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the game to paused or unpaused state based on the input boolean value.
     * @param paused a boolean representing whether to pause the game or not
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Called when the game window loses focus. Sets all player movement booleans to false.
     */
    public void windowFocusLost() {
        player.allMovingBooleansFalse();
    }

    /**
     * Returns the player object of the game.
     * @return the Player object of the game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the ItemManager object of the game.
     * @return the ItemManager object of the game
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Returns the EnemyManager object of the game.
     * @return the EnemyManager object of the game
     */
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    /**
     * Returns the LevelManager object of the game.
     * @return the LevelManager object of the game
     */
    public LevelManager getLevelManager(){return levelManager;}

    public InteractablesManager getInteractablesManager(){return interactablesManager;}

    /**
     * Sets the maximum level offset in the x direction.
     * @param maxLevelOffsetX the maximum level offset in the x direction
     */
    public void setMaxLevelOffsetX(int maxLevelOffsetX) {
        this.maxLevelOffsetX = maxLevelOffsetX;
    }



    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
