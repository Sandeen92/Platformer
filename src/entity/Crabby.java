package entity;

import main.Game;

import static utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{

    public Crabby(float x, float y) {
        super(x, y, RAT_WIDTH, RAT_HEIGHT, CRABBY, 6, 2);
        initialiseHitbox(x, y, (int) (23 * Game.SCALE),(int) (15 * Game.SCALE));
        initialiseAttackBox(x, y, (int) (22 * Game.SCALE),(int) (19 * Game.SCALE));
    }


}
