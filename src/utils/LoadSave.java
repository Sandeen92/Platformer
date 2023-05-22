package utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is responsible for reading all resources such as images and audio-files.
 * @author Simon Sandén
 * @author Linus Magnusson
 * @author Casper Johannesson
 */
public class LoadSave {

    public static final String LEVEL_ATLAS = "LEVEL_ONE_TILESET_SPRITES.png";
    public static final String RAT_ENEMY = "ENEMY_RAT_BROWN.png";
    public static final String STARTMENU_BACKGROUND = "STARTMENU_BACKGROUND.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String DEATHSCREEN = "src/resources/DEATHSCREEN.gif";
    public static final String DEATHSCREEN_YOUDIED = "youded.png";

    public static final String POTION_ATLAS = "TEMP_POTION_SPRITES.png";
    public static final String CONTAINER_ATLAS = "CONTAINER_SPRITES.png";
    public static final String STARTMENU_BUTTONS = "STARTMENU_BUTTONS.png";
    public static final String DEATHSCREEN_REPLAY_BUTTON = "DEATHSCREEN_BUTTON.png";
    public static final String PAUSE_BACKGROUND = "MENU_PAUSED.png";
    public static final String OPTIONS_BACKGROUND = "MENU_OPTIONS_BG.png";
    public static final String OPTIONS_BUTTONS = "MENU_OPTIONS_BUTTONS.png";
    public static final String SOUND_BUTTONS = "soundbuttons.png";
    public static final String STARTMENU_MUSIC = System.getProperty("user.dir") + "\\src\\resources\\ohboy.wav";
    public static final String DEATHSCREEN_MUSIC = System.getProperty("user.dir") + "\\src\\resources\\coffindance.wav";

    public static final String LEVELCOMPLETED_TEXT = "LEVEL_COMPLETED.png";

    /**
     * Loads and retrieves a sprite atlas image from the specified file.
     *
     * @param fileName the name of the sprite atlas file
     * @return the loaded sprite atlas image as a BufferedImage
     */
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/resources/" + fileName);
        try {
            image = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    /**
     * Loads and retrieves all level images from the specified file paths.
     *
     * @return an array of loaded level images as BufferedImages
     */
    public static BufferedImage[] GetAllLevels(){

            ArrayList<String> levelNames = new ArrayList<>();
            levelNames.add("1.png");

            BufferedImage[] levels = new BufferedImage[levelNames.size()];

            for(int i = 0; i < levels.length; i++) {

                try {
                    levels[i] = ImageIO.read(LoadSave.class.getResourceAsStream("/resources/leveldata/" + levelNames.get(i)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return levels;
    }

    /**
     * Sorts the given array of level data files based on their names.
     *
     * @param levelDataList the array of level data files to be sorted
     * @return the sorted array of level data files
     */
    public static File[] sortLevelDataList(File[] levelDataList){
        File[] levelDataListSorted = new File[levelDataList.length];

        for(int i = 0; i < levelDataListSorted.length; i++){
            for(int j = 0; j < levelDataList.length; j++){
                if(levelDataList[j].getName().equals((i + 1) + ".png")){
                    levelDataListSorted[i] = levelDataList[j];
                }
            }
        }
        return levelDataListSorted;
    }

    /**
     * Loads and retrieves the frames of a GIF for the deathscreen.
     *
     * @return an array of loaded GIF frames as BufferedImages
     * @throws IOException if an error occurs during file reading
     */
    public static BufferedImage[] getDeathscreenGif() throws IOException {
        BufferedImage[] frames;
        File gifFile = new File(DEATHSCREEN);
        try (ImageInputStream in = ImageIO.createImageInputStream(gifFile)) {
            ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
            reader.setInput(in);
            int numFrames = reader.getNumImages(true);
            frames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                frames[i] = resizeGif(frame);
            }
            reader.dispose();
        }
        return frames;
    }

    /**
     * Resizes a given image to the specified width and height.
     *
     * @param originalImage the original image to resize
     * @return the resized image
     */
    private static BufferedImage resizeGif(BufferedImage originalImage){
        BufferedImage resizedImage = new BufferedImage(300,300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage,0,0,300,300,null);
        g2d.dispose();
        return resizedImage;
    }

}

