package main;

import input.KeyBoardInputs;
import input.MouseInputs;
import javax.swing.*;
import java.awt.*;
import static utils.Constants.GameConstants.*;

public class GamePanel extends JPanel {
    private Game game;
    private MouseInputs mouseInputs = new MouseInputs(this);
    public static JLabel LBL_FPS_COUNTER = null;
    public static JLabel LBL_PLAYER_HP = null;
    public static JLabel LBL_INFO = null;

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

    private void initialiseInfo() {
        LBL_INFO = new JLabel();
        LBL_INFO.setFont(new Font("DialogInput", Font.BOLD, 25));
        LBL_INFO.setForeground(Color.yellow);
        LBL_INFO.setText("Press R to reset");
        add(LBL_INFO);
    }

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

    public void setPanelSize(){
        Dimension panelSize = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(panelSize);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.draw(g);
    }

    public Game getGame(){
        return game;
    }
}
