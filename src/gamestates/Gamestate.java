package gamestates;


/**
 * This class is a container of constants for different gamestates.
 * Also keeps track of current and previous state.
 *
 * @author Simon Sandén
 */
public enum Gamestate {

    PLAYING,
    STARTMENU,
    OPTIONS,
    DEATHSCREEN,
    PAUSEMENU,
    QUIT;


    public static Gamestate state = STARTMENU;
    public static Gamestate previousState = STARTMENU;

}
