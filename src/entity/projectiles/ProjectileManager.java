package entity.projectiles;

import entity.enemy.EnemyRat;
import gamestates.Playing;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import static utils.AssistanceMethods.canMoveHere;
import static utils.Constants.ProjectileConstants.BULLET_HEIGTH;
import static utils.Constants.ProjectileConstants.BULLET_WIDTH;

public class ProjectileManager {

    private Playing playing;
    private ArrayList<Bullet> bullets;

    public ProjectileManager(Playing playing){
        bullets = new ArrayList<>();
        this.playing = playing;
    }

    public void update(){
        for(Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext();){
            Bullet bullet = iterator.next();
            bullet.update();
            if(checkIfBulletCollidesWithTile(bullet) == false){
                iterator.remove();
            }
            if(checkIfBulletCollidesWithEnemy(bullet)){
                iterator.remove();
            }

        }
    }

    private boolean checkIfBulletCollidesWithTile(Bullet bullet) {
        if(canMoveHere(bullet.getHitbox().x, bullet.getHitbox().y, bullet.getHitbox().width, bullet.getHitbox().height, playing.getLevelManager().getCurrentLevel().getLevelData()) == false){
            return false;
        }
        return true;
    }

    private boolean checkIfBulletCollidesWithEnemy(Bullet bullet){
        if(playing.getEnemyManager().checkIfEnemyIsHit(bullet)){
            return true;
        }
        return false;
    }

    public void draw(Graphics g, int currentLevelXOffset){
        drawBullets(g, currentLevelXOffset);
    }

    public void drawBullets(Graphics g, int currentLevelXOffset){
        for(Bullet bullet: bullets){
            g.drawImage(bullet.getProjectileImage(), (int) bullet.getHitbox().x - currentLevelXOffset, (int) bullet.getHitbox().y, (int) bullet.getHitbox().width * bullet.getFlipW(), (int) bullet.getHitbox().height,  null);
        }
    }

    public void addBullet(float x, float y,int[][] levelData, int direction){
        bullets.add(new Bullet(x,y,BULLET_WIDTH, BULLET_HEIGTH, levelData, direction, this));

    }

    public void removeBullet(Bullet bullet){
        bullets.remove(bullet);
    }
}
