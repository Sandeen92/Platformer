package utils;

import entity.Crabby;
import main.Game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {

    public static final String LEVEL_ATLAS = "level_one_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String TESTENEMY = "crabby_sprite.png";
    public static final String STARTMENU_BACKGROUND = "startmenu_background.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String STARTMENU_BUTTONS = "startmenubutton_atlas.png";

    public static final String PAUSE_BACKGROUND = "pauseimage.png";
    public static final String OPTIONS_BACKGROUND = "optionsmenu.png";


    public static final String STARTMENU_MUSIC = "resources/ohboy.wav";



    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static ArrayList<Crabby> getCrabbs() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Crabby> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY) {
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
                }

            }
        }
        return list;
    }

    public static int[][] GetLevelData() {

        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                lvlData[j][i] = value;
            }
        return lvlData;
    }


    public static BufferedImage GetLevelBackground(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
}

