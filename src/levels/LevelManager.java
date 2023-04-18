/**
 * This class is responsible for keeping track of the levels and drawing them
 * @author Simon Sandén
 */

package levels;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;
    private BufferedImage levelBackground;
    private BufferedImage levelBackgroundTree;

    /**
     * Constructor for LevelManager
     * @param game
     */
    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
        levelBackground = LoadSave.GetSpriteAtlas("background_test2.png");
        levelBackgroundTree = LoadSave.GetSpriteAtlas("background_sprites.png");
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevels){
            levels.add(new Level(img));
        }
    }

    public void loadNextLevel() {
        levelIndex++;
        if(levelIndex >= levels.size()){
            levelIndex = 0;
            Gamestate.state = Gamestate.STARTMENU;
        }

        Level newLevel = levels.get(levelIndex);
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
        g.drawImage(levelBackgroundTree,(int) (1 * Game.SCALE) - levelOffset, (int) (0 * Game.SCALE) , (int) (1664 * Game.SCALE), (int) (448 * Game.SCALE), null);
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(levelIndex).getLevelData()[0].length; i++) {
                int index = levels.get(levelIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - levelOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void updateLevel() {

    }

    public Level getCurrentLevel(){
        return levels.get(levelIndex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }


}
