package items;

import main.Game;

import static utils.Constants.ObjectConstants.*;
import static utils.Constants.GameConstants.*;



/**
 *
 *
 * ------------------------------- GRANSKA EJ DENNA KLASS! --------------------------------------
 *
 *
 */


public class Container extends GameItem{
    public Container(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }


    private void createHitbox(){

        if(objectType == BOX){
            initialiseHitbox(25, 18);

            xDrawOffset = (int) (7 * SCALE);
            yDrawOffset = (int) (12 * SCALE);
        }
        else if(objectType == BARREL){
            initialiseHitbox(23, 25);

            xDrawOffset = (int) (8 * SCALE);
            yDrawOffset = (int) (5 * SCALE);
        }
    }

    public void update(){
        if(doAnimation == true){
            updateAnimationTick();
        }
    }
}
