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

    public static class EnemyConstants{
        public static final int CRABBY = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;

        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);

        public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * Game.SCALE);
        public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * Game.SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState){
            switch (enemyType){
                case CRABBY:
                    switch (enemyState){
                        case IDLE:
                            return 9;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 4;
                        case DEAD:
                            return 5;
                    }
            }
            return 0;
        }
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

