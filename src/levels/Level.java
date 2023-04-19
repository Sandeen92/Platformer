package levels;

import entity.Box;
import entity.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.AssistanceMethods.*;
import static utils.Constants.GameConstants.*;

public class Level {
    private int [][] levelData;
    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private ArrayList<Box> boxes;
    private int levelTilesWidth;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createBoxes();
        calculateLevelOffset();
        calculatePlayerSpawn();
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void createLevelData() {
        levelData = GetLevelData(img);
    }

    private void calculateLevelOffset() {
        levelTilesWidth = img.getWidth();
        maxTilesOffset = levelTilesWidth - TILES_IN_WIDTH;
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }
    private void createBoxes(){
        boxes = GetBoxes(img);
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

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public ArrayList<Box> getBoxes(){
        return boxes;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
