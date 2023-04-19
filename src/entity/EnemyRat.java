/**
 * This class is a Rat enemy which extends the Enemy Class
 * @author Linus Magnusson
 */

package entity;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.*;

public class EnemyRat extends Enemy{

    public EnemyRat(float x, float y) {
        super(x, y, RAT_WIDTH, RAT_HEIGHT, CRABBY, RAT_HEALTHPOINTS, RAT_ATTACKDAMAGE);
        initialiseHitbox(x, y, (int) (23 * SCALE),(int) (15 * SCALE));
        initialiseAttackBox(x, y, (int) (23 * SCALE),(int) (15 * SCALE));
    }


}
