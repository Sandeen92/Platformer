package items;

import main.Game;
import static utils.Constants.GameConstants.*;

public class Consumable extends GameItem{


    /**
     *
     *
     * ------------------------------- GRANSKA EJ DENNA KLASS! --------------------------------------
     *
     *
     */


    public Consumable(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initialiseHitbox(7, 14);
        xDrawOffset = (int) (3 * SCALE);
        yDrawOffset = (int) (2 * SCALE);
    }

    public void update(){
        updateAnimationTick();
    }
}
