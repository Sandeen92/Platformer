package gamestates;

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
