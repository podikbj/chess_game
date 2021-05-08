package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class ChessTimer implements Runnable {

    private BoardPanel boardPanel;
    private Clock clock;
    private JLabel wbTime;
    private static Logger logger = Logger.getLogger(ChessTimer.class.getName());

    public ChessTimer(BoardPanel boardPanel, Clock clock, JLabel wbTime) {

        this.boardPanel = boardPanel;
        this.clock = clock;
        this.wbTime = wbTime;

    }

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
    }
}
