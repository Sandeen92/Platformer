package main;
//Imports from within project
import input.KeyBoardInputs;
import input.MouseInputs;
//Imports from Javas library
import javax.swing.*;
import java.awt.*;
import java.io.File;
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
    public static JLabel LBL_FPS_COUNTER = null;
    public static JLabel LBL_PLAYER_HP = null;
    public static JLabel LBL_INFO = null;
    public static JLabel LBL_TIMER_TEXT = null;

    public GamePanel(Game game){
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        setFocusable(true);
        requestFocus();
        setPanelSize();
        initialiseFpsCounter();
        initialisePlayerHP();
        initialiseInfo();
        this.game = game;
    }

    /**
     * Initializes the information label.
     */
    private void initialiseInfo() {
        LBL_INFO = new JLabel();
        LBL_INFO.setFont(new Font("DialogInput", Font.BOLD, 25));
        LBL_INFO.setForeground(Color.yellow);
        add(LBL_INFO);
    }

    /**
     * Initializes the player's HP label.
     */
    private void initialisePlayerHP() {
        LBL_PLAYER_HP = new JLabel();
        LBL_PLAYER_HP.setFont(new Font("DialogInput", Font.BOLD, 25));
        LBL_PLAYER_HP.setForeground(Color.yellow);
        add(LBL_PLAYER_HP);
    }

    /**
     * Temporary FPS counter
     */
    private void initialiseFpsCounter() {
        LBL_FPS_COUNTER = new JLabel();
        LBL_FPS_COUNTER.setFont(new Font("DialogInput", Font.BOLD, 25));
        LBL_FPS_COUNTER.setForeground(Color.yellow);
        add(LBL_FPS_COUNTER);
    }

    private void setTimerText(String timeString) {
        LBL_TIMER_TEXT = new JLabel(timeString);
        LBL_TIMER_TEXT.setFont(new Font("DialogInput", Font.BOLD, 25));
        LBL_TIMER_TEXT.setForeground(Color.yellow);
        add(LBL_TIMER_TEXT);
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

    public Game getGame(){
        return game;
    }
}
