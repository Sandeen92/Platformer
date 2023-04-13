package levels;

import entity.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.AssistanceMethods.*;

public class Level {
    private int [][] levelData;
    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private int levelTilesWidth;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
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
        maxTilesOffset = levelTilesWidth - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
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

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
