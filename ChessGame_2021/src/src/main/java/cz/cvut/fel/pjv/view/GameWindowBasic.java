package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.logging.Logger;
import javax.swing.JFrame;


/**
 * Represents basic game window
 * @author kira
 */
public class GameWindowBasic {

    private JFrame gameWindowFrame;
    private GameWindowGameAuto auto;
    private GameWindowGameView view;
    private GameWindowGameComp comp;
    private final GameManager gameManager;
    private String gameView = "";
    private GameStateEnum gameState;

    private int hh = 0;
    private int mm = 0;
    private int ss = 0;

    private static Logger logger = Logger.getLogger(GameWindowBasic.class.getName());

    /**
     * Constructor for basic game window.
     * @param hh hours
     * @param mm minutes
     * @param ss seconds
     * @param gameState current game mode
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
     * Creates current game mode window on user's choise 
     * @param bp chess board
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
     * Ends game
     * @param winner final game score
     * @param byTime true if game was played by time
     */
    public void endGame(String winner, boolean byTime) {

        switch (gameState) {
            case GENERAL:
            case INITIAL_MANUAL_SETTING:
                auto.endGame(winner, byTime);
                break;
            case LOAD_GAME:
                view.endGame(winner, byTime);
                break;
            case AI_MODEL:
                comp.endGame(winner, byTime);
                break;

        }
    }

    /**
     * Getter for gameView
     * @return string of moves in pgn format
     */
    public String getGameView() {
        return gameView;
    }

    /**
     * Setter for gameView
     * @param gameView
     */
    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    /**
     *
     * @return game window for general/manual game mode
     */
    public GameWindowGameAuto getAuto() {
        return auto;
    }

    /**
     *
     * @return game window for view game/load game  mode
     */
    public GameWindowGameView getView() {
        return view;
    }

    /**
     *
     * @return game window for ai game  mode
     */
    public GameWindowGameComp getComp() {
        return comp;
    }

    /**
     * Getter for game state
     * @return
     */
    public GameStateEnum getGameState() {
        return gameState;
    }

    /**
     * Setter for game state
     * @param gameState
     */
    public void setGameState(GameStateEnum gameState) {
        this.gameState = gameState;
    }

    /**
     *
     * @return how many hours remains until the eng of the game
     */
    public int getHH() {
        return hh;
    }

    /**
     *
     * @return how many minutes remains until the eng of the game
     */
    public int getMM() {
        return mm;
    }

    /**
     *
     * @return how many seconds remains until the eng of the game
     */
    public int getSS() {
        return ss;
    }

}
