package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Players;
import cz.cvut.fel.pjv.start.GameManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author kira
 */
public class GameWindowBasic {

    private JFrame gameWindowFrame;
    private GameWindowGameAuto auto;
    private GameWindowGameView view;
    private GameWindowGameComp comp;
    private GameManager gameManager;
    private String gameView = "";
    private GameStateEnum gameState;

    private int hh = 0;
    private int mm = 0;
    private int ss = 0;

    private static Logger logger = Logger.getLogger(GameWindowBasic.class.getName());

    /**
     *
     * @param hh
     * @param mm
     * @param ss
     * @param gameState
     */
    public GameWindowBasic(int hh, int mm, int ss, GameStateEnum gameState) {

        this.gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        gameManager.initializePieces();
        gameManager.initializeCheckMatePositionControl();

        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
        this.gameState = gameState;

    }

    /**
     *
     * @param bp
     */
    public void createGameModes(BoardPanel bp) {
        switch (gameState) {
            case GENERAL:
            case INITIAL_MANUAL_SETTING:
                auto = new GameWindowGameAuto(this, bp);
                break;
            case LOAD_GAME:
            case LOAD_VIEW:
                view = new GameWindowGameView(this, bp, gameState);
                break;
            case AI_MODEL:
                comp = new GameWindowGameComp(this, bp);
                break;
        }

    }

    /**
     *
     * @param winner
     * @param byTime
     */
    public void endGame(String winner, boolean byTime) {

        switch (gameState) {
            case GENERAL:
            case INITIAL_MANUAL_SETTING:
                //case INITIAL_MANUAL_SETTING:
                auto.endGame(winner, byTime);
                break;
            case LOAD_GAME:
                //case LOAD_VIEW:
                view.endGame(winner, byTime);
                break;
            case AI_MODEL:
                comp.endGame(winner, byTime);
                break;

        }
    }

    /**
     *
     * @return
     */
    public String getGameView() {
        return gameView;
    }

    /**
     *
     * @param gameView
     */
    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    /**
     *
     * @return
     */
    public GameWindowGameAuto getAuto() {
        return auto;
    }

    /**
     *
     * @return
     */
    public GameWindowGameView getView() {
        return view;
    }

    /**
     *
     * @return
     */
    public GameWindowGameComp getComp() {
        return comp;
    }

    /**
     *
     * @return
     */
    public GameStateEnum getGameState() {
        return gameState;
    }

    /**
     *
     * @param gameState
     */
    public void setGameState(GameStateEnum gameState) {
        this.gameState = gameState;
    }

    /**
     *
     * @return
     */
    public int getHH() {
        return hh;
    }

    /**
     *
     * @return
     */
    public int getMM() {
        return mm;
    }

    /**
     *
     * @return
     */
    public int getSS() {
        return ss;
    }

}
