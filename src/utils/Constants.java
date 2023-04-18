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

        public static class SoundButtons{
            public static final int SOUNDBTN_SIZE_DEFAULT = 42;
            public static final int SOUNDBTN_SIZE = (int) (SOUNDBTN_SIZE_DEFAULT * Game.SCALE);
        }

        public static class OptionButtons{
            public static final int OPTIONBTN_DEFAULT_SIZE = 56;
            public static final int OPTIONBTN_SIZE = (int) (OPTIONBTN_DEFAULT_SIZE * Game.SCALE);
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
        public static final int HIT = 3;

        public static int GetSpriteAmount(int player_action){
            switch (player_action){
                case RUNNING:
                    return 8;
                case IDLE:
                    return 4;
                case JUMP:
                    return 5;
                case HIT:
                    return 4;
                default:
                    return 1;
            }
        }


    }

    public static class ObjectConstants{

        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;
        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static int GetSpriteAmount(int objectType){
            switch (objectType) {
                case RED_POTION, BLUE_POTION:
                    return 7;

                case BARREL, BOX:
                    return 8;
            }
            return 1;
        }
    }
}

