/**
 * This class is resposible for creating, keeping track of and updating the enemies in the game
 * @author Linus Magnusson
 */

package entity;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
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
    }

    /**
     * This method calls a method in loadsave that reads in the enemies and adds them to the enemylist
     */
    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    /**
     * This method itterates through all the enemies and updates them
     * @param lvldata
     */
    public void update(int[][] lvldata){
        for(Crabby c : crabbies){
            c.update(lvldata);
            c.checkPlayerHit(c,playing.getPlayer());
        }
    }

    //TODO add method to change animation
    public void checkIfEnemyIsHit(Rectangle2D.Float attackBox){
        for (Crabby c : crabbies){
            if(attackBox.intersects(c.getHitbox())){
                c.entityTakeDamage(2);
                System.out.println(c.getCurrentHealth());
                if(c.isEntityDead()){
                    crabbies.remove(c);
                }
                return;
            }
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
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAnimationIndex()], (int) c.getHitbox().x - RAT_DRAW_OFFSET_X - levelOffset + c.getFlipX(), (int) c.getHitbox().y - RAT_DRAW_OFFSET_Y, RAT_WIDTH * c.getFlipW(), RAT_HEIGHT, null );
            //c.drawHitbox(g, levelOffset);
        }
    }

    /**
     * This method loads the sprite atlas for the enemy and saves it as a 2d array of bufferedImages
     */
    private void loadEnemyImg() {
        crabbyArr = new BufferedImage[3][5];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.RATENEMY);
        for(int i = 0; i < crabbyArr.length; i++)  {
            for (int j = 0; j < crabbyArr[i].length; j++){
                crabbyArr[i][j] = temp.getSubimage(j * RAT_WIDTH_DEFAULT, i * RAT_HEIGHT_DEFAULT, RAT_WIDTH_DEFAULT, RAT_HEIGHT_DEFAULT);
            }
        }
    }

    public ArrayList<Crabby> getCrabbies() {
        return crabbies;
    }

    public void setCrabbies(ArrayList<Crabby> crabbies) {
        this.crabbies = crabbies;
    }
}
