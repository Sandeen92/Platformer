package entity.player;

import entity.enemy.EnemyManager;
import entity.projectiles.ProjectileManager;

import static utils.Constants.GameConstants.SCALE;
import static utils.Constants.StartPlayerConstants.START_PLAYER_SPRITES;

public class Gun_Player extends Player {

    private ProjectileManager projectileManager;

    public Gun_Player(float x, float y, int width, int height, int maxHealth, int attackDamage, EnemyManager enemyManager, ProjectileManager projectileManager) {
        super(x, y, width, height, maxHealth, attackDamage, enemyManager);
        loadPlayerAnimations(START_PLAYER_SPRITES);
        initialiseHitbox(x,y, 22 * SCALE, 30 * SCALE);
        initialiseBoxAttackBox(x, y, 90 * SCALE, 30 * SCALE);
        this.projectileManager = projectileManager;
    }

    /**
     * This method is responsible for updating the player
     */
    public void updatePlayer() {
        updateEntityPosition(levelData);
        updateAnimationTick();
        updateBoxAttackBox(30, facingDirection);
        setEntityAnimation();
    }

    /**
     * This method is responsible for updating the player
     */
    public void update() {
        updateEntityPosition(levelData);
        updateAnimationTick();
        updateBoxAttackBox(30, facingDirection);
        setEntityAnimation();
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
