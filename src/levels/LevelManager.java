/**
 * This class is responsible for keeping track of the levels and drawing them
 * @author Simon Sandén
 * @author Casper Johannesson
 */

package levels;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.GameConstants.*;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> allLevels;
    private int levelIndex = 0;
    private BufferedImage levelBackground;
    private BufferedImage levelBackgroundSprites;

    /**
     * Constructor for LevelManager
     * @param game
     */
    public LevelManager(Game game) {
        this.game = game;
        initClasses();
    }

    private void initClasses(){
        importOutsideSprites();
        allLevels = new ArrayList<>();
        buildAllLevels();
        importLevelImages();
    }

    /**
     * Adds all levels to the levels array
     */
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage levelData : allLevels){
            this.allLevels.add(new Level(levelData));
        }
    }

    /**
     * Loads the next level in the game
     */
    public void loadNextLevel() {
        levelIndex++;
        if(levelIndex >= allLevels.size()){
            levelIndex = 0;
            Gamestate.state = Gamestate.STARTMENU;
        }

        Level newLevel = allLevels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getInteractablesManager().loadBoxes(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffsetX(newLevel.getMaxLevelOffsetX());
    }

    /**
     * This method is used to import the Level atlas and saving it in an array
     */
    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    /**
     * Imports the background images and sprites for the first level.
     */
    private void importLevelImages(){
        levelBackground = LoadSave.GetSpriteAtlas("LEVEL_ONE_BACKGROUND.png");
        levelBackgroundSprites = LoadSave.GetSpriteAtlas("LEVEL_ONE_SPRITES.png");
    }

    /**
     * This method draws the level
     */
    public void drawLevel(Graphics g, int levelOffset) {
        drawLevelBackground(g, levelOffset);
        for (int j = 0; j < TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < allLevels.get(levelIndex).getLevelData()[0].length; i++) {
                int index = allLevels.get(levelIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], calculateImageXPosition(i, levelOffset), calculateImageYPosition(j), TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    /**
     * This method draws the levels background image and background sprites.
     */
    public void drawLevelBackground(Graphics g, int levelOffset){
        g.drawImage(levelBackground,0,0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(levelBackgroundSprites,(int) (1 * SCALE) - levelOffset, (int) (0 * SCALE) , (int) (1664 * SCALE), (int) (448 * SCALE), null);
    }

    /**
     * This method calculates the X-position of an image.
     */
    public int calculateImageXPosition(int xValue, int levelOffset){
        return TILES_SIZE * xValue - levelOffset;
    }

    /**
     * This method calculates the Y-position of an image.
     */
    public int calculateImageYPosition(int yValue){
        return TILES_SIZE * yValue;
    }

    /**
     * This method returns the current level.
     */
    public Level getCurrentLevel(){
        return allLevels.get(levelIndex);
    }

    public int getAmountOfLevels(){
        return allLevels.size();
    }


}
