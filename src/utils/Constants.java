package utils;

import static utils.Constants.GameConstants.SCALE;

public class Constants {

    public static class UserInterface{
        public static class Buttons{
            public static final int BTN_WIDTH_DEFAULT = 140;
            public static final int BTN_HEIGHT_DEFAULT = 56;
            public static final int BTN_WIDTH = (int) (BTN_WIDTH_DEFAULT * SCALE);
            public static final int BTN_HEIGHT = (int) (BTN_HEIGHT_DEFAULT * SCALE);
        }

        public static class SoundButtons{
            public static final int SOUNDBTN_SIZE_DEFAULT = 42;
            public static final int SOUNDBTN_SIZE = (int) (SOUNDBTN_SIZE_DEFAULT * SCALE);
        }

        public static class OptionButtons{
            public static final int OPTIONBTN_DEFAULT_SIZE = 56;
            public static final int OPTIONBTN_SIZE = (int) (OPTIONBTN_DEFAULT_SIZE * SCALE);
        }
    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
    }

    public static class EntityConstants{
        public static final float MAX_AIR_SPEED = 5.6f;
        public static final float AIR_SPEED_OFFSET = 2;

    }
    public static class EnemyConstants{
        public static final int RAT = 0;
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;
        public static final int RAT_WIDTH_DEFAULT = 60;
        public static final int RAT_HEIGHT_DEFAULT = 20;
        public static final int RAT_WIDTH = (int) (RAT_WIDTH_DEFAULT * SCALE);
        public static final int RAT_HEIGHT = (int) (RAT_HEIGHT_DEFAULT * SCALE);
        public static final int RAT_DRAW_OFFSET_X = (int) (23 * SCALE);
        public static final int RAT_DRAW_OFFSET_Y = (int) (4 * SCALE);
        public static final int RAT_HEALTHPOINTS = 6;
        public static final int RAT_ATTACKDAMAGE = 2;
        public static final float RAT_PATROL_SPEED = 0.3f * SCALE;

        public static int GetSpriteAmount(int enemyType, int enemyState){
            switch (enemyType){
                case RAT:
                    switch (enemyState){
                        case IDLE:
                            return 1;
                        case RUNNING:
                            return 5;
                        case ATTACK:
                            return 7;
                        case HIT:
                        case DEAD:
                            return 4;
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
        public static final float PLAYER_X_DRAW_OFFSET = 24 * SCALE;
        public static final float PLAYER_Y_DRAW_OFFSET = 14 * SCALE;
        public static final float PLAYER_SPEED = 1.2f;
        public static final float PLAYER_KNOCKBACK_LEFT = -0.4f;
        public static final float PLAYER_KNOCKBACK_RIGHT = 0.4f;

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

    public static class InteractableConstants {
        public static final int BOX = 55;
        public static final float BOX_WIDTH = 25* SCALE;
        public static final float BOX_HEIGHT = 25* SCALE;
        public static final float BOX_MOVESPEED = 0.6f;

    }

    public static class GameConstants{
        public final static float SCALE = 2f;
        public final static int TILES_DEFAULT_SIZE = 16;
        public final static int TILES_IN_WIDTH = 52;
        public final static int TILES_IN_HEIGHT = 28;
        public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
        public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
        public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
        public final static int FPS_SET = 120;
        public final static int UPS_SET = 200;
        public final static double NANO_SECOND = 1000000000.0;

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
        public static final int CONTAINER_WIDTH = (int) (SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (SCALE * POTION_HEIGHT_DEFAULT);

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

