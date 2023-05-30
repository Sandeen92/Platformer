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
    private ArrayList<Seagull> seagulls = new ArrayList<>();
    private ArrayList<StaticEnemy> staticEnemies = new ArrayList<>();

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
        seagulls = level.getSeagulls();
        staticEnemies = level.getStaticEnemies();


        //Alla råttor har 0 hp på banan trots att 6 är satt i konstruktor
        //ta inte bort!
        for(EnemyRat rat : rats){
            rat.setCurrentHealth(6);
            rat.setMoving(true);
        }
        initlalisePlayerToseagulls();
        for(Seagull seagull: seagulls){
            seagull.setCurrentHealth(SEAGULL_HEALTHPOINTS);
        }
    }

    public void initlalisePlayerToseagulls(){
        for(Seagull seag: seagulls){
            seag.setPlayer(playing.getPlayer());
        }
    }

    /**
     * This method iterates through all the enemies and updates them
     * @param levelData
     */
    public void update(int[][] levelData){
        for(EnemyRat rat : rats){
            rat.update(levelData);
            rat.checkIfPlayerIsHit(rat, playing.getPlayer());
        }
        for (Seagull seagull: seagulls){
            seagull.update(levelData);
            seagull.checkIfPlayerIsHit(seagull, playing.getPlayer());
        }

        for(StaticEnemy staticEnemy : staticEnemies){
            staticEnemy.update();
            if(staticEnemy.isActive == true){
                staticEnemy.checkIfPlayerIsHit(staticEnemy, playing.getPlayer());
            }

        }

    }

    /**
     * This method checks if an enemys hitbox is intersecting with the players attackbox
     * @param bullet
     */
    public boolean checkIfEnemyIsHit(Bullet bullet){
        for (EnemyRat rat : rats){
            if(bullet.getHitbox().intersects(rat.getHitbox()) == true && bullet.canDoDamage && rat.getEntityState() != DEAD){
                bullet.canDoDamage = false;
                rat.enemyTakeDamage(2);
                checkIfEnemyIsDead(rat);
                return true;
            }
        }
        for(Seagull seagull: seagulls){
            if(bullet.getHitbox().intersects(seagull.hitbox) == true && bullet.canDoDamage && seagull.getEntityState() != DEAD){
                bullet.canDoDamage = false;
                seagull.enemyTakeDamage(2);
                checkIfEnemyIsDead(seagull);
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
            rat.setMoving(false);
        }
        if(enemy.isEntityDead() == true && enemy instanceof Seagull seagull){
            seagulls.remove(seagull);
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
        for(Seagull seagull: seagulls){
            g.drawImage(seagull.seagullAnimation[seagull.animationIndex], (int) (seagull.getHitbox().x - levelOffset + seagull.getFlipX()), (int) seagull.getHitbox().y, SEAGULL_WIDTH * seagull.getFlipW(), SEAGULL_HEIGHT, null);
            seagull.drawHitbox(g, levelOffset);
            seagull.drawVisionBox(g, levelOffset);
            seagull.drawAttackBox(g,levelOffset);
        }
        for(StaticEnemy staticEnemy : staticEnemies){
            if(staticEnemy.isActive == true){
                g.drawImage(staticEnemy.getStaticEnemyImages()[staticEnemy.getAnimationIndex()], (int) staticEnemy.getHitbox().x - levelOffset, (int) staticEnemy.getHitbox().y, STEAM_WIDTH, STEAM_HEIGHT, null);
                staticEnemy.drawHitbox(g,levelOffset);
            }

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
