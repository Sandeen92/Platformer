package entity;

import gamestates.Playing;
import levels.Level;
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

    public void loadBoxes(Level level){
        boxes = level.getBoxes();
        for(Box b : boxes){
            b.initPlayerToBox(playing.getPlayer());
        }
    }


}
