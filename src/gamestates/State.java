package gamestates;
//Imports from within project
import main.Game;
import userinterface.MenuButton;
//Imports from Javas library
import java.awt.event.MouseEvent;

/**
 * The State class represents a state in the game, such as the startmenu, options or when playing the game itself.
 *
 */
public class State {

    protected Game game;

    /**
     * Constructor for the State class. Superclass for every gamestate.
     * @param game the Game object that represents the whole game.
     */
    public State(Game game){
        this.game = game;
    }

    /**
     * Checks whether the user's mouse is inside a menu button's bounds.
     * @param e The MouseEvent representing the user's mouse action.
     * @param mb The MenuButton to check.
     * @return True if the user's mouse is inside the button's bounds, false otherwise.
     */
    public boolean isUserInsideBtnBounds(MouseEvent e, MenuButton mb){
        return mb.getBtnBounds().contains(e.getX(), e.getY());
    }

    /**
     * Gets the Game object.
     * @return the Game object.
     */
    public Game getGame(){
        return game;
    }

}
