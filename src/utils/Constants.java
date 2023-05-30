package utils;

import static utils.Constants.GameConstants.SCALE;

/**
 * This class is a container that holds constants.
 * @author Casper Johannesson
 * @author Linus Magnusson
 * @author Simon Sandén
 */
public class Constants {

    //****************************
    //***********GAME*************
    //****************************

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

    //****************************
    //************ENTITY**********
    //****************************

    public static class Directions{
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
    }

    public static class EntityConstants{
        public static final float MAX_AIR_SPEED = 5.6f;
        public static final float AIR_SPEED_OFFSET = 2;
        public static final float BOX_OFFSET_FOR_PLAYER = 57.8f;

    }

    public static class ProjectileConstants{
        public static final float BULLET_RIGTHSPEED = 4f;
        public static final float BULLET_LEFTSPEED = -4f;
        public static final int BULLET_HEIGTH = (int) (9 * SCALE);
        public static final int BULLET_WIDTH = (int) (9*SCALE);
    }

    public static class EnemyConstants{
        public static final String SEAGULL_IMAGE = "Seagull.png";
        public static final int SEAGULL = 1;
        public static final float SEAGULL_X_SPEED = 0.8f;
        public static final float SEAGULL_Y_SPEED = 0.3f;
        public static final float SEAGULL_HITBOX_X_OFFSET = 600;
        public static final float SEAGULL_HITBOX_Y_OFFSET = 250;

        public static final int SEAGULL_WIDTH_DEFAULT = 60;
        public static final int SEAGULL_HEIGHT_DEFAULT = 20;
        public static final int SEAGULL_WIDTH = (int) (SEAGULL_WIDTH_DEFAULT * SCALE);
        public static final int SEAGULL_HEIGHT = (int) (SEAGULL_HEIGHT_DEFAULT * SCALE);
        public static final int SEAGULL_HEALTHPOINTS = 10;
        public static final int SEAGULL_ATTACKDAMAGE = 2;


        public static final int STEAM = 2;
        public static final int STEAM_HEALTHPOINTS = 10;
        public static final int STEAM_ATTACKDAMAGE = 4;
        public static final int STEAM_WIDTH_DEFAULT = 40;
        public static final int STEAM_HEIGHT_DEFAULT = 32;
        public static final int STEAM_WIDTH = (int) (STEAM_WIDTH_DEFAULT * SCALE);
        public static final int STEAM_HEIGHT = (int) (STEAM_HEIGHT_DEFAULT * SCALE);
        public static final String STEAM_IMAGE = "/StaticTest.png";


        public static final int FIRE = 3;
        public static final int FIRE_HEALTHPOINTS = 10;
        public static final int FIRE_ATTACKDAMAGE = 4;
        public static final int FIRE_WIDTH_DEFAULT = 40;
        public static final int FIRE_HEIGHT_DEFAULT = 32;
        public static final int FIRE_WIDTH = (int) (FIRE_WIDTH_DEFAULT * SCALE);
        public static final int FIRE_HEIGHT = (int) (FIRE_HEIGHT_DEFAULT * SCALE);
        public static final String FIRE_IMAGE = "/StaticTest2.png";


        public static final int WATER = 4;
        public static final int WATER_HEALTHPOINTS = 10;
        public static final int WATER_ATTACKDAMAGE = 4;
        public static final int WATER_WIDTH_DEFAULT = 40;
        public static final int WATER_HEIGHT_DEFAULT = 32;
        public static final int WATER_WIDTH = (int) (WATER_WIDTH_DEFAULT * SCALE);
        public static final int WATER_HEIGHT = (int) (WATER_HEIGHT_DEFAULT * SCALE);
        public static final String WATER_IMAGE = "/StaricTest3.png";


        public static final int RAT = 0;
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        //public static final int ATTACK = 2;   Eftersom dessa två inte existerar och skapade problem med att hantera deathanimation
        //public static final int HIT = 3;      låt de vara tills funktioner till detta implementeras
        public static final int DEAD = 2;
        public static final int RAT_WIDTH_DEFAULT = 60;
        public static final int RAT_HEIGHT_DEFAULT = 20;
        public static final int RAT_WIDTH = (int) (RAT_WIDTH_DEFAULT * SCALE);
        public static final int RAT_HEIGHT = (int) (RAT_HEIGHT_DEFAULT * SCALE);
        public static final int RAT_DRAW_OFFSET_X = (int) (23 * SCALE);
        public static final int RAT_DRAW_OFFSET_Y = (int) (4 * SCALE);
        public static final int RAT_HEALTHPOINTS = 6;
        public static final int RAT_ATTACKDAMAGE = 2;
        public static final float RAT_PATROL_SPEED = 0.33f * SCALE;

        public static int GetSpriteAmount(int enemyType, int enemyState){
            switch (enemyType){
                case RAT:
                    switch (enemyState){
                        case IDLE:
                            return 1;
                        case RUNNING:
                            return 5;
                       // case ATTACK:
                       //     return 7;
                        case DEAD:
                            return 4;
                    }
            }
            return 0;
        }
    }

    public static class StartPlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int HIT = 3;
        public static final int PUSHING = 4;
        public static final float PLAYER_X_DRAW_OFFSET = 24 * SCALE;
        public static final float PLAYER_Y_DRAW_OFFSET = 14 * SCALE;
        public static final float PLAYER_SPEED = 1.2f;
        public static final float PLAYER_KNOCKBACK_LEFT = -0.4f;
        public static final float PLAYER_KNOCKBACK_RIGHT = 0.4f;
        public static final int PLAYER_WIDTH = (int) (70 * SCALE);
        public static final int PLAYER_HEIGTH = (int) (45 * SCALE);
        public static final String START_PLAYER_SPRITES = "/PLAYER_SPRITES.png";

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
                case PUSHING:
                    return 2;
                default:
                    return 1;
            }
        }
    }

    public static class GunManConstants{
        public static final String GUN_MAN_CHANGER = "/GUN_MAN_CHANGER.png";
        public static final String GUN_MAN_SPRITES = "/GUN_MAN.png";
    }

    public static class InteractableConstants {
        public static final int BOX = 55;
        public static final float BOX_WIDTH = 25* SCALE;
        public static final float BOX_HEIGHT = 25* SCALE;
        public static final float BOX_MOVESPEED = 0.6f;

    }

}

