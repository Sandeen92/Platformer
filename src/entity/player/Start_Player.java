/**
 * This class is responsible for the players attributes and functions
 * @author Linus Magnusson
 */

package entity.player;

import entity.enemy.Enemy;
import entity.enemy.EnemyManager;

import static utils.Constants.GameConstants.*;
import static utils.Constants.StartPlayerConstants.START_PLAYER_SPRITES;

public class Start_Player extends Player {
    /**
     * Constructor for Player
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Start_Player(float x, float y, int width, int height, int maxHealth, int attackDamage, EnemyManager enemyManager) {
        super(x, y, width, height, maxHealth, attackDamage, enemyManager);
        loadPlayerAnimations(START_PLAYER_SPRITES);
        initialiseHitbox(x,y, 22 * SCALE, 30 * SCALE);
        initialiseAttackBox(x,y,20 * SCALE, 27 * SCALE);
        initialiseBoxAttackBox(x, y, 90 * SCALE, 30 * SCALE);
    }

    /**
     * This method is responsible for updating the player
     */
    public void update() {
        updateEntityPosition(levelData);
        updateAnimationTick();
        updateAttackBox(30, facingDirection);
        updateBoxAttackBox(30, facingDirection);
        setEntityAnimation();
    }

    /**
     * This method makes the player attack
     */
    public void attack() {
        if (canAttack == true) {
            startAttackCooldown();
        }
    }
}
