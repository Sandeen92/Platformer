package entity;

import gamestates.Playing;
import levels.Level;

import java.awt.*;
import java.util.ArrayList;


public class InteractablesManager {
    private ArrayList<Box> boxes;
    private Playing playing;


    public InteractablesManager(Playing playing){
        this.playing = playing;
        boxes = new ArrayList<>();
    }

    public void draw(Graphics g, int xOffset){
        for(Box b : boxes){
            b.draw(g, xOffset);
        }
    }

    public void update(){
        for(Box box : boxes){
            box.update(playing.getLevelManager().getCurrentLevel().getLevelData());
            box.checkIfPlayerCollidesWithBox(box, playing.getPlayer());
            box.checkIfEnemyIsCollidingWithBox(playing.getEnemyManager().getCrabbies());
        }
    }

    public void loadBoxes(Level level){
        boxes = level.getBoxes();
        for(Box b : boxes){
            b.initialisePlayerToBox(playing.getPlayer());
        }
    }


}
