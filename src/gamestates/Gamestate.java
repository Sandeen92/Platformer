package gamestates;

public enum Gamestate {

    PLAYING,
    MENU,
    OPTIONS,
    DEATHSCREEN,
    PAUSE,
    RESET,
    QUIT;


    public static Gamestate state = MENU;

}
