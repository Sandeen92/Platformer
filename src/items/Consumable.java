package items;

import main.Game;

public class Consumable extends GameItem{

    public Consumable(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initialiseHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
    }

    public void update(){
        updateAnimationTick();
    }
}
