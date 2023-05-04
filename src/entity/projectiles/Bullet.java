package entity.projectiles;

public class Bullet extends Projectile{
    public Bullet(float x, float y, int width, int heigth, int[][] levelData, int direction, ProjectileManager projectileManager) {
        super(x, y, width, heigth, levelData, direction, projectileManager);
    }
}
