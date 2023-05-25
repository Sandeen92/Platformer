package entity.projectiles;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constants.ProjectileConstants.*;

public abstract class Projectile {
    private ProjectileManager projectileManager;
    private BufferedImage projectileImage;
    private Rectangle2D.Float hitbox;
    private int [][] levelData;
    private int direction;
    public boolean canDoDamage;
    private int flipW = 0;


    public Projectile(float x, float y, int width, int heigth, int[][] levelData, int direction, ProjectileManager projectileManager){
        this.levelData = levelData;
        this.direction = direction;
        this.projectileManager = projectileManager;
        canDoDamage = true;
        initializeHitbox(x,y,width,heigth, direction);
        loadProjectileImage();
    }

    public void update(){
        moveProjectile();
    }

    public void moveProjectile(){
        setMoveDirection();
    }

    public void setMoveDirection(){
        if(direction == 1){
            this.hitbox.x += BULLET_RIGTHSPEED;
            flipW = 1;
        } else {
            this.hitbox.x += BULLET_LEFTSPEED;
            flipW = -1;
        }
    }


    public void initializeHitbox(float x, float y, int width, int heigth, int direction){
        if(direction == 1){
            this.hitbox = new Rectangle2D.Float(x + 45,y,width,heigth);
        } else {
            this.hitbox = new Rectangle2D.Float(x,y,width,heigth);
        }

    }

    public void loadProjectileImage(){
        InputStream inputStream = getClass().getResourceAsStream("/BulletImage.png");
        try{
            projectileImage = ImageIO.read(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public BufferedImage getProjectileImage(){
        return projectileImage;
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getFlipW(){
        return flipW;
    }
}
