/**
 * This class is responsible for keeping track of the levels and drawing them
 * @author Simon Sandén
 */

package levels;

import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;
    private BufferedImage levelBackground;
    private BufferedImage levelBackgroundHouses;

    /**
     * Constructor for LevelManager
     * @param game
     */
    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
        levelBackground = LoadSave.GetLevelBackground("background_test.png");
        levelBackgroundHouses = LoadSave.GetLevelBackground("background_houses.png");
    }

    /**
     * This method is used to import the Level atlas and saving it in an array
     */
    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
    }
    /**
     * This method draws the level
     */
    public void drawLevel(Graphics g, int levelOffset) {
        g.drawImage(levelBackground,0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(levelBackgroundHouses,0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levelOne.getLvlData()[0].length; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - levelOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public void updateLevel() {

    }

    public Level getCurrentLevel(){
        return levelOne;
    }
}
