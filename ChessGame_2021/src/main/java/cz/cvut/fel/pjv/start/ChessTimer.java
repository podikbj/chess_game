package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import javax.swing.JLabel;

public class ChessTimer extends Thread {

    BoardPanel boardPanel;
    boolean colorThread;
    Clock clock;
    JLabel wbTime;

    public ChessTimer(BoardPanel boardPanel, boolean colorThread, Clock clock, JLabel wbTime) {
        this.boardPanel = boardPanel;
        this.colorThread = colorThread;
        this.clock = clock;
        this.wbTime = wbTime;
    }

    @Override
    public void run() {
        super.run();
        while (!clock.outOfTime()) {
            if (colorThread == boardPanel.isWhiteIsActive()) {
                clock.decrementClock();
                wbTime.setText(clock.getTime());
                //System.out.println("colorThread: " + colorThread);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            } else {
                //int a = 2;
                //System.out.println("colorThread: " + colorThread + "; " + "boardPanel.isWhiteIsActive(): " + boardPanel.isWhiteIsActive());
            }
        }
    }
}
