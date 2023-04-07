package gamestates;

import main.Game;
import userinterface.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;

    public State(Game game){
        this.game = game;
    }

    public boolean isUserInsideBtnBounds(MouseEvent e, MenuButton mb){
        return mb.getBtnBounds().contains(e.getX(), e.getY());
    }


    public Game getGame(){
        return game;
    }
}
