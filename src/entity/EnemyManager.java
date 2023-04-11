/**
 * This class is resposible for creating, keeping track of and updating the enemies in the game
 * @author Linus Magnusson
 */

package entity;

import gamestates.Playing;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    /**
     * Constructor for EnemyManager
     * @param playing
     */
    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImg();
        addEnemies();
    }

    /**
     * This method calls a method in loadsave that reads in the enemies and adds them to the enemylist
     */
    private void addEnemies() {
        crabbies = LoadSave.getCrabbs();
    }

    /**
     * This method itterates through all the enemies and updates them
     * @param lvldata
     */
    public void update(int[][] lvldata){
        for(Crabby c : crabbies){
            c.update(lvldata);
        }
    }

    /**
     * This method is called to draw the enemies
     * @param g
     */
    public void draw(Graphics g, int levelOffset){
        drawCrabs(g, levelOffset);
    }

    /**
     * This method iterates through the enemy list and draws the enemy
     * @param g
     */
    private void drawCrabs(Graphics g, int levelOffset) {
        for(Crabby c : crabbies){
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAnimationIndex()], (int) c.getHitbox().x - CRABBY_DRAW_OFFSET_X - levelOffset, (int) c.getHitbox().y - CRABBY_DRAW_OFFSET_Y, CRABBY_WIDTH, CRABBY_HEIGHT, null );
            c.drawHitbox(g, levelOffset);
        }
    }

    /**
     * This method loads the sprite atlas for the enemy and saves it as a 2d array of bufferedImages
     */
    private void loadEnemyImg() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.TESTENEMY);
        for(int i = 0; i < crabbyArr.length; i++)  {
            for (int j = 0; j < crabbyArr[i].length; j++){
                crabbyArr[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }


}
