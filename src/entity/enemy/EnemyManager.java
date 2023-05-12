/**
 * This class is resposible for creating, keeping track of and updating the enemies in the game
 * @author Linus Magnusson
 * @author Simon Sandén
 */

package entity.enemy;

import entity.player.Start_Player;
import entity.projectiles.Bullet;
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

        //Alla råttor har 0 hp på banan trots att 6 är satt i konstruktor
        //ta inte bort!
        for(EnemyRat rat : rats){
            rat.setCurrentHealth(6);
        }
    }

    /**
     * This method itterates through all the enemies and updates them
     * @param levelData
     */
    public void update(int[][] levelData){
        for(EnemyRat rat : rats){
            rat.update(levelData);
            rat.checkIfPlayerIsHit(rat, playing.getPlayer());
        }
    }

    /**
     * This method checks if an enemys hitbox is intersecting with the players attackbox
     * @param bullet
     */
    public boolean checkIfEnemyIsHit(Bullet bullet){
        for (EnemyRat rat : rats){
            if(bullet.getHitbox().intersects(rat.getHitbox()) == true && bullet.canDoDamage){
                bullet.canDoDamage = false;
                rat.enemyTakeDamage(2);
                checkIfEnemyIsDead(rat);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an enemy is hit by a box attack.
     *
     * @param boxAttackBox the box attack rectangle
     */
    public void checkIfEnemyIsHitByBox(Rectangle2D.Float boxAttackBox){
        for (EnemyRat rat : rats){
            if(boxAttackBox.intersects(rat.getHitbox()) == true){
                checkIfEnemyIsDead(rat);
                return;
            }
        }
    }

    /**
     * This method checks if the rat is dead and removes it
     * @param enemy to be checked
     */
    public void checkIfEnemyIsDead(Enemy enemy){
        if(enemy.isEntityDead() == true && enemy instanceof EnemyRat rat){
            killRat(rat);
        }
    }


    /**
     * This method is meant to kill rats only
     * @param enemy to be killed
     */
    public void killRat(Enemy enemy){
        rats.remove(enemy);
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
