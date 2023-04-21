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
//Imports of static variables and methods
import static utils.Constants.GameConstants.*;

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
    private int currentLevelXOffset;
    private int cameraLeftBorder = (int) (0.3 * GAME_WIDTH);
    private int cameraRightBorder = (int) (0.7 * GAME_WIDTH);
    private int maxLevelXOffset;

    /**
     * Constructor for the Playing class.
     * Initializes the class variables, calculates the level offset, and loads the starting level.
     * @param game the Game object that represents the whole game.
     */
    public Playing(Game game) {
        super(game);
        initialiseClasses();
        calculateLevelOffset();
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
    private void calculateLevelOffset() {
        maxLevelXOffset = levelManager.getCurrentLevel().getMaxLevelXOffset();
    }

    /**
     * This method initializes the player, level, enemy, item, and interactable managers.
     * Also loads the leveldata and spawnpoint into Player object.
     */
    public void initialiseClasses() {
        enemyManager = new EnemyManager(this);
        player = new Player(200,200, (int) (70 * SCALE),(int)(45 * SCALE), 10, 2, enemyManager);
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
        initialiseClasses();
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
        int currentPlayerPositionX = playerPositionX - currentLevelXOffset;
        findBorderClosestToPlayer(currentPlayerPositionX);
        changeCurrentLevelXOffset();
    }

    /**
     * This method gets the current closest border to the player
     * and sets the offset to that border
     * @param currentPlayerXPosition
     */
    private void findBorderClosestToPlayer(int currentPlayerXPosition){
        if (currentPlayerXPosition > cameraRightBorder) {
            setCurrentLevelXOffset(currentPlayerXPosition,cameraRightBorder);
        } else if (currentPlayerXPosition < cameraLeftBorder) {
            setCurrentLevelXOffset(currentPlayerXPosition,cameraLeftBorder);
        }
    }

    /**
     * This method changes the current levelOffset
     */
    private void changeCurrentLevelXOffset(){
        if (currentLevelXOffset > maxLevelXOffset) {
            currentLevelXOffset = maxLevelXOffset;
        } else if (currentLevelXOffset < 0) {
            currentLevelXOffset = 0;
        }
    }

    /**
     * this method sets the currentlevelOffsetX
     * @param currentPlayerXPosition
     * @param cameraBorder
     */
    private void setCurrentLevelXOffset(int currentPlayerXPosition, int cameraBorder){
        currentLevelXOffset += currentPlayerXPosition - cameraBorder;
    }

    /**
     * This method draws the images of all the objects within the PLAYING state.
     * @param g the Graphics object to use for drawing
     */
    @Override
    public void draw(Graphics g) {
        levelManager.drawLevel(g, currentLevelXOffset);
        player.renderPlayer(g, currentLevelXOffset);
        enemyManager.draw(g, currentLevelXOffset);
        itemManager.draw(g, currentLevelXOffset);
        interactablesManager.draw(g, currentLevelXOffset);
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
                player.resetBooleanJumpOnce();
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
     * @param maxLevelXOffset the maximum level offset in the x direction
     */
    public void setMaxLevelXOffset(int maxLevelXOffset) {
        this.maxLevelXOffset = maxLevelXOffset;
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
