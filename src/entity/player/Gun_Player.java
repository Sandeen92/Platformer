package entity.player;

import entity.enemy.EnemyManager;
import entity.projectiles.ProjectileManager;

public class Gun_Player extends Player {

    private ProjectileManager projectileManager;

    public Gun_Player(float x, float y, int width, int height, int maxHealth, int attackDamage, EnemyManager enemyManager) {
        super(x, y, width, height, maxHealth, attackDamage,enemyManager);
    }

    @Override
    protected void updateEntityPosition(int[][] levelData) {

    }

    /**
     * This method makes the player attack
     */
    public void attack(){
        if(canAttack == true){
            projectileManager.addBullet(hitbox.x, hitbox.y + 32,levelData , facingDirection);
            startAttackCooldown();
        }
    }

}
