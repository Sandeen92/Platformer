package gamestates;
//Imports from within project
import entity.enemy.EnemyManager;
import entity.interactable.InteractablesManager;
import entity.player.Gun_Player;
import entity.player.Player;
import entity.player.Start_Player;
import entity.projectiles.ProjectileManager;
import items.ItemManager;
import levels.LevelManager;
import main.Game;
//Imports from Javas library
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
//Imports of static variables and methods
import static utils.Constants.GameConstants.*;
import static utils.Constants.StartPlayerConstants.PLAYER_HEIGTH;
import static utils.Constants.StartPlayerConstants.PLAYER_WIDTH;

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
    private ProjectileManager projectileManager;
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
        projectileManager = new ProjectileManager(this);
        player = new Start_Player(200,200, (PLAYER_WIDTH),(PLAYER_HEIGTH), 10, 2, enemyManager);
        levelManager = new LevelManager(game);
        itemManager = new ItemManager(this);
        interactablesManager = new InteractablesManager(this, enemyManager);
        loadLevelDataToPlayer();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    /**
     * This method restarts the game.
     */
    public void restartGame(){
        initialiseClasses();
        loadStartLevel();
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the level data to the player.
     */
    public void loadLevelDataToPlayer(){
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
    }

    /**
     * Changes the player class and initializes a new player instance based on the provided class name, position, and parameters.
     *
     * @param className the name of the player class to change to
     * @param x the X position of the new player
     * @param y the Y position of the new player
     */
    public void changeClass(String className, float x, float y){
        switch (className){
            case "StartPlayer":
                player = new Start_Player(x,y, (PLAYER_WIDTH),(PLAYER_HEIGTH), 10, 2, enemyManager);
                loadLevelDataToPlayer();
                interactablesManager.loadBoxes(levelManager.getCurrentLevel());
                break;
            case "GunMan":
                player = new Gun_Player(x,y, (PLAYER_WIDTH),(PLAYER_HEIGTH), 10, 2, enemyManager, projectileManager);
                loadLevelDataToPlayer();
                interactablesManager.loadBoxes(levelManager.getCurrentLevel());
                break;
        }
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
            player.update();
            interactablesManager.update();
            projectileManager.update();
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
        projectileManager.draw(g, currentLevelXOffset);
        interactablesManager.draw(g, currentLevelXOffset);
        itemManager.draw(g,currentLevelXOffset);

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
                player.attackWithBox();
                break;
            case KeyEvent.VK_D:
                player.setMovingRight(true);
                player.attackWithBox();
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
                player.allMovingBooleansFalse();
                Gamestate.state = Gamestate.PAUSEMENU;
                break;

            case KeyEvent.VK_F1:
                //Developer function
                player.setCurrentHealth(0);
                break;
            case KeyEvent.VK_G:
                //Developer function
                changeClass("GunMan", getPlayer().getHitbox().x, getPlayer().getHitbox().y);
                break;
            case KeyEvent.VK_H:
                //Developer function
                changeClass("StartPlayer", getPlayer().getHitbox().x, getPlayer().getHitbox().y);
                break;

            case KeyEvent.VK_F2:
                //Developer function - FLYING
                player.setAirSpeed(-2.5f);
                break;

            case KeyEvent.VK_F12:
                //Robot will input this when rat gets crushed. Has to be dealt like this at the moment for concurrency reasons
                player.attackWithBox();
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
                player.setHorizontalSpeed(0);
                player.attackWithBox();
                break;
            case KeyEvent.VK_D:
                player.setMovingRight(false);
                player.setHorizontalSpeed(0);
                player.attackWithBox();
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
