/**
 * This class is responsible for keeping track of the interactable objects
 * @author Linus Magnusson
 */

package entity;

import gamestates.Playing;
import levels.Level;
import java.awt.*;
import java.util.ArrayList;


public class InteractablesManager {
    private ArrayList<Box> boxes;
    private Playing playing;

    /**
     * Constructor for interactablesManager
     * @param playing
     */
    public InteractablesManager(Playing playing){
        this.playing = playing;
        initialiseVariables();
    }

    /**
     * This method initialises the variables
     */
    private void initialiseVariables(){
        boxes = new ArrayList<>();
    }

    /**
     * This method draws the interactables
     * @param g
     * @param xOffset
     */
    public void draw(Graphics g, int xOffset){
        for(Box b : boxes){
            b.draw(g, xOffset);
        }
    }

    /**
     * This method updates all the interactables in the arraylists
     */
    public void update(){
        for(Box box : boxes){
            box.update(playing.getLevelManager().getCurrentLevel().getLevelData(), box, playing);
        }
    }

    /**
     * This method loads the boxes and initalises player in to the boxes
     * @param level
     */
    public void loadBoxes(Level level){
        boxes = level.getBoxes();
        for(Box b : boxes){
            b.initialisePlayerToBox(playing.getPlayer());
        }
    }
}
