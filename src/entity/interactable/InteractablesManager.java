/**
 * This class is responsible for keeping track of the interactable objects
 * @author Linus Magnusson
 */

package entity.interactable;

import entity.enemy.EnemyManager;
import entity.interactable.Box;
import gamestates.Playing;
import levels.Level;
import java.awt.*;
import java.util.ArrayList;


public class InteractablesManager {
    private ArrayList<Box> interactableBoxes;
    private Playing playing;
    private EnemyManager enemyManager;

    /**
     * Constructor for interactablesManager
     * @param playing
     */
    public InteractablesManager(Playing playing, EnemyManager enemyManager){
        this.playing = playing;
        this.enemyManager = enemyManager;
        initialiseVariables();
    }


    /**
     * This method initialises the variables
     */
    private void initialiseVariables(){
        interactableBoxes = new ArrayList<>();
    }

    /**
     * This method draws the interactables
     * @param g
     * @param xOffset
     */
    public void draw(Graphics g, int xOffset){
        for(Box box : interactableBoxes){
            box.draw(g, xOffset);
        }
    }

    /**
     * This method updates all the interactables in the arraylists
     */
    public void update(){
        for(Box box : interactableBoxes){
            box.update(playing.getLevelManager().getCurrentLevel().getLevelData(), box, playing);
        }
    }


    /**
     * This method loads the boxes and initalises player in to the boxes
     * @param level
     */
    public void loadBoxes(Level level){
        interactableBoxes = level.getBoxes();
        for(Box b : interactableBoxes){
            b.initialisePlayerToBox(playing.getPlayer());
            b.setEnemyManager(enemyManager);
        }
    }
}
