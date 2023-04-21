package levels;

import entity.Box;
import entity.EnemyRat;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.AssistanceMethods.*;
import static utils.Constants.GameConstants.*;

public class Level {
    private int [][] levelData;
    private BufferedImage levelDataImage;
    private ArrayList<EnemyRat> rats;
    private ArrayList<Box> boxes;
    private int levelTilesWidth;
    private int maxTilesOffset;
    private int maxLevelXOffset;
    private Point playerSpawn;

    public Level(BufferedImage levelDataImage){
        this.levelDataImage = levelDataImage;
        initialiseEverything();
    }

    private void initialiseEverything(){
        createLevelData();
        createEnemies();
        createBoxes();
        calculateLevelOffset();
        calculatePlayerSpawn();
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(levelDataImage);
    }

    private void createLevelData() {
        levelData = GetLevelData(levelDataImage);
    }

    private void calculateLevelOffset() {
        levelTilesWidth = levelDataImage.getWidth();
        maxTilesOffset = levelTilesWidth - TILES_IN_WIDTH;
        maxLevelXOffset = TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        rats = GetRats(levelDataImage);
    }
    private void createBoxes(){
        boxes = GetBoxes(levelDataImage);
    }
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

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
