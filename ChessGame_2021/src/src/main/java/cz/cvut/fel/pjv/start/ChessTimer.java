package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.view.GameWindowGameAuto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Represents chess timer that manages the clock
 *
 * @author kira
 */
public class ChessTimer implements Runnable {

    private BoardPanel boardPanel;
    private Clock clock;
    private JLabel wbTime;
    private final GameWindowGameAuto gameAuto;
    private static Logger logger = Logger.getLogger(ChessTimer.class.getName());

    /**
     * Constructor for chess timer.
     *
     * @param boardPanel checc board
     * @param clock chess clock
     * @param wbTime remaining game time
     * @param gameAuto timed game status
     */
    public ChessTimer(BoardPanel boardPanel, Clock clock, JLabel wbTime, GameWindowGameAuto gameAuto) {

        this.boardPanel = boardPanel;
        this.clock = clock;
        this.wbTime = wbTime;
        this.gameAuto = gameAuto;

    }

    /**
     * Independenly decreases the time on each of the timers at the end of the
     * game time , determines the winner by time
     *
     */
    @Override
    public void run() {

        while (!clock.outOfTime()) {
            if (Thread.currentThread().getName().equals("White") && boardPanel.isWhiteIsActive()) {
                clock.decrementClock();
                wbTime.setText(clock.getTime());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (Thread.currentThread().getName().equals("Black") && !boardPanel.isWhiteIsActive()) {
                clock.decrementClock();
                wbTime.setText(clock.getTime());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);

                }
            }

        }

        String winner = (clock.getColor() == 1)
                ? "0 - 1" : "1 - 0";

        if (gameAuto.getGameIsOver() == 1) {
            gameAuto.decrementGameIsOver();
            gameAuto.endGame(winner, true);
        }

    }
}
