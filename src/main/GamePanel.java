package main;
//Imports from within project
import input.KeyBoardInputs;
import input.MouseInputs;
import utils.LoadSave;
//Imports from Javas library
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
//Imports of static variables and methods
import static utils.Constants.GameConstants.*;

/**
 * This class is the main panel where every gamestate draws their individual Graphics component.
 * @author Linus Magnusson
 * @author Casper Johannesson
 */
public class GamePanel extends JPanel {
    private Game game;
    private MouseInputs mouseInputs = new MouseInputs(this);
    public static JLabel LBL_TIMER_TEXT = null;

    public GamePanel(Game game){
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        setFocusable(true);
        requestFocus();
        setPanelSize();
        timerTextLabel();
        this.game = game;
    }



    /**
     * Sets the size of the GamePanel
     */
    public void setPanelSize(){
        Dimension panelSize = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(panelSize);
    }

    /**
     * Paints the graphics component.
     *
     * @param g the graphics context
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.draw(g);
    }


    /**
     * Initializes the text label for the timer. It imports a font from the resources-folder.
     */
    private void timerTextLabel() {

        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, LoadSave.class.getResourceAsStream("/Press_Start_2P.ttf"));
            customFont = customFont.deriveFont(28f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
        LBL_TIMER_TEXT = new JLabel();
        LBL_TIMER_TEXT.setFont(customFont);
        LBL_TIMER_TEXT.setForeground(new Color(253, 179, 0));
        LBL_TIMER_TEXT.setLocation(GAME_WIDTH/2, 400);
        add(LBL_TIMER_TEXT);
        validate();
    }

    public Game getGame(){
        return game;
    }
}
