package entity;

import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.BOX;

public class InteractablesManager {
    private ArrayList<Box> boxes;
    private Playing playing;

    public InteractablesManager(Playing playing){
        this.playing = playing;
        boxes = new ArrayList<>();
        addBoxes();
    }

    public void draw(Graphics g, int xOffset){
        for(Box b : boxes){
            b.draw(g, xOffset);
        }
    }

    public void update(){
        for(Box box : boxes){
            box.update(playing.getLevelManager().getCurrentLevel().getLevelData());
            box.checkPlayerHit(box, playing.getPlayer());
            box.checkIfEnemyIsHit(playing.getEnemyManager().getCrabbies());
        }
    }

    public void addBoxes(){
        boxes.add(new Box(2400,700, (int)(25 * Game.SCALE), (int)(25 * Game.SCALE), playing.getPlayer()));
    }


}
