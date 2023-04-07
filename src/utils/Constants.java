package utils;

import main.Game;

public class Constants {


    public static class UserInterface{
        public static class Buttons{
            public static final int BTN_WIDTH_DEFAULT = 140;
            public static final int BTN_HEIGHT_DEFAULT = 56;
            public static final int BTN_WIDTH = (int) (BTN_WIDTH_DEFAULT * Game.SCALE);
            public static final int BTN_HEIGHT = (int) (BTN_HEIGHT_DEFAULT * Game.SCALE);
        }
    }
    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;

        public static int GetSpriteAmount(int player_action){
            switch (player_action){
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case JUMP:
                    return 3;
                default:
                    return 1;
            }
        }


    }
}

