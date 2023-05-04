/**
 * This class is resposible for creating, keeping track of and updating the enemies in the game
 * @author Linus Magnusson
 */

package entity.enemy;

import entity.enemy.EnemyRat;
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
    private BufferedImage[][] ratImageArray;
    private ArrayList<EnemyRat> rats = new ArrayList<>();

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
        rats = level.getRats();
        setEnemyManagerForAllRats();
    }

    public void setEnemyManagerForAllRats(){
        for (EnemyRat rat : rats){
            rat.setEnemyManager(this);
        }
    }

    /**
     * This method itterates through all the enemies and updates them
     * @param levelData
     */
    public void update(int[][] levelData){
        for(EnemyRat rat : rats){
            rat.update(levelData);
            rat.checkIfPlayerIsHit(rat,playing.getPlayer());
        }
    }

    /**
     * This method checks if an enemys hitbox is intersecting with the players attackbox
     * @param attackBox
     */
    public void checkIfEnemyIsHit(Rectangle2D.Float attackBox){
        for (EnemyRat rat : rats){
            if(attackBox.intersects(rat.getHitbox()) == true){
                rat.enemyTakeDamage(2);
                checkIfEnemyIsDead(rat);
                return;
            }
        }
    }

    /**
     * This method checks if the rat is dead and removes it
     * @param
     */
    public void checkIfEnemyIsDead(Enemy enemy){
        if(enemy.isEntityDead() == true && enemy instanceof EnemyRat rat){
            killRat(rat);
        }
    }

    public void killRat(Enemy enemy){
        rats.remove(enemy);
    }

    public void checkIfRatIsDead(){
        for (int i = 0; i < rats.size(); i++){
            if (rats.get(i).getCurrentHealth() >= 0){
                killRat(rats.get(i));
            }
        }
    }

    /**
     * This method is called to draw the enemies
     * @param g
     */
    public void draw(Graphics g, int levelOffset){
        drawEnemies(g, levelOffset);
    }

    /**
     * This method iterates through the enemy list and draws the enemy
     * @param g
     */
    private void drawEnemies(Graphics g, int levelOffset) {
        for(EnemyRat c : rats){
            g.drawImage(ratImageArray[c.getEnemyState()][c.getAnimationIndex()], (int) c.getHitbox().x - RAT_DRAW_OFFSET_X - levelOffset + c.getFlipX(), (int) c.getHitbox().y - RAT_DRAW_OFFSET_Y, RAT_WIDTH * c.getFlipW(), RAT_HEIGHT, null );
            //c.drawHitbox(g, levelOffset);
        }
    }

    /**
     * This method loads the sprite atlas for the enemy and saves it as a 2d array of bufferedImages
     */
    private void loadEnemyImg() {
        ratImageArray = new BufferedImage[3][5];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.RAT_ENEMY);
        for(int i = 0; i < ratImageArray.length; i++)  {
            for (int j = 0; j < ratImageArray[i].length; j++){
                ratImageArray[i][j] = temp.getSubimage(j * RAT_WIDTH_DEFAULT, i * RAT_HEIGHT_DEFAULT, RAT_WIDTH_DEFAULT, RAT_HEIGHT_DEFAULT);
            }
        }
    }

    public ArrayList<EnemyRat> getRats() {
        return rats;
    }

    public void setRats(ArrayList<EnemyRat> rats) {
        this.rats = rats;
    }
}
