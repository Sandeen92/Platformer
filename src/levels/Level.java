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
    private ArrayList<EnemyRat> Rats;
    private ArrayList<Box> boxes;
    private int levelTilesWidth;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage levelDataImage){
        this.levelDataImage = levelDataImage;
        initClasses();
    }

    private void initClasses(){
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
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        Rats = GetRats(levelDataImage);
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

    public int getMaxLevelOffsetX() {
        return maxLevelOffsetX;
    }

    public ArrayList<EnemyRat> getRats() {
        return Rats;
    }

    public ArrayList<Box> getBoxes(){
        return boxes;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
