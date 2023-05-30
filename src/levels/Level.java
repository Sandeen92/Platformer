package levels;
//Imports from within project
import entity.enemy.Seagull;
import entity.interactable.Box;
import entity.enemy.EnemyRat;
//Imports from Javas library
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//Imports of static variables and methods
import static utils.AssistanceMethods.*;
import static utils.Constants.GameConstants.*;

/**
 * This class is responsible for storing all the data in a level.
 * @author Casper Johannesson
 */
public class Level {
    private int [][] levelData;
    private BufferedImage levelDataImage;
    private ArrayList<EnemyRat> rats;
    private ArrayList<Seagull> seagulls;
    private ArrayList<Box> boxes;
    private int levelTilesWidth;
    private int maxTilesOffset;
    private int maxLevelXOffset;
    private Point playerSpawn;

    public Level(BufferedImage levelDataImage){
        this.levelDataImage = levelDataImage;
        initialiseEverything();
    }

    /**
     * Initializes everything required for the level.
     */
    private void initialiseEverything(){
        createLevelData();
        createEnemies();
        createBoxes();
        calculateLevelOffset();
        calculatePlayerSpawn();
    }

    /**
     * Places player spawnpoint from image
     */
    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(levelDataImage);
    }

    /**
     * Loads level data from image
     */
    private void createLevelData() {
        levelData = GetLevelData(levelDataImage);
    }

    /**
     * Calculates the level offset.
     */
    private void calculateLevelOffset() {
        levelTilesWidth = levelDataImage.getWidth();
        maxTilesOffset = levelTilesWidth - TILES_IN_WIDTH;
        maxLevelXOffset = TILES_SIZE * maxTilesOffset;
    }

    /**
     * Creates enemy Rat from image
     */
    private void createEnemies() {
        rats = GetRats(levelDataImage);
        seagulls = GetSeagulls(levelDataImage);
    }

    /**
     * Creates interactable Box from image
     */
    private void createBoxes(){
        boxes = GetBoxes(levelDataImage);
    }

    /**
     * Retrieves the sprite index at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The sprite index at the given coordinates.
     */
    public int getSpriteIndex(int x, int y){
            return levelData[y][x];
    }

    public int[][] getLevelData(){
        return levelData;
    }

    public int getMaxLevelXOffset() {
        return maxLevelXOffset;
    }

    public ArrayList<EnemyRat> getRats() {
        return rats;
    }

    public ArrayList<Box> getBoxes(){
        return boxes;
    }

    public ArrayList<Seagull> getSeagulls(){
        return seagulls;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
