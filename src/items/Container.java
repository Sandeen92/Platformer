package items;

import main.Game;

import static utils.Constants.ObjectConstants.*;

public class Container extends GameItem{
    public Container(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

    private void createHitbox(){

        if(objectType == BOX){
            initialiseHitbox(25, 18);

            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);
        }
        else if(objectType == BARREL){
            initialiseHitbox(23, 25);

            xDrawOffset = (int) (8 * Game.SCALE);
            yDrawOffset = (int) (5 * Game.SCALE);
        }
    }

    public void update(){
        if(doAnimation == true){
            updateAnimationTick();
        }
    }
}
