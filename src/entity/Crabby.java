package entity;

import main.Game;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GameConstants.*;

public class Crabby extends Enemy{

    public Crabby(float x, float y) {
        super(x, y, RAT_WIDTH, RAT_HEIGHT, CRABBY, 6, 2);
        initialiseHitbox(x, y, (int) (23 * SCALE),(int) (15 * SCALE));
        initialiseAttackBox(x, y, (int) (22 * SCALE),(int) (19 * SCALE));
    }


}
