package cz.cvut.fel.pjv.view;


import cz.cvut.fel.pjv.start.GameManager;
import java.util.logging.Logger;
import javax.swing.JFrame;


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

    private static Logger logger = Logger.getLogger(GameForm.class.getName());

    public GameWindowBasic(int hh, int mm, int ss, GameStateEnum gameState) {

        this.gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        gameManager.initializePieces();
        gameManager.initializeCheckMatePositionControl();

        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
        this.gameState = gameState;

//        JPanel gameData = clockJPanel();
//        gameData.setSize(gameData.getPreferredSize());
//        gameWindowFrame.add(gameData, BorderLayout.NORTH);
//
//        JPanel textPanel = textJPanel();
//        textPanel.setSize(textPanel.getPreferredSize());
//        gameWindowFrame.add(textPanel, BorderLayout.EAST);
//
//        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
//        gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
//        gameWindowFrame.setSize(FRAME_DIM);
//        gameWindowFrame.setResizable(false);
//        gameWindowFrame.pack();
//        gameWindowFrame.setVisible(true);
//        this.gameWindowFrame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                blackClock.setNull();
//                whiteClock.setNull();
//                try {
//                    bChessTimer.join();
//                    wChessTimer.join();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(GameWindowBasic.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            }
//        });
//        gameWindowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void createGameModes(BoardPanel bp) {
     //   gameState = mode;
        switch (gameState) {
            case GENERAL:
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

    public void endGame(String winner) {

        switch (gameState) {
            case GENERAL:
                auto.endGame(winner);
                break;
            case LOAD_GAME:
            case LOAD_VIEW:
                view.endGame(winner);
                break;
            case AI_MODEL:
                comp.endGame(winner);
                break;
        }
        //    gameWindowFrame.dispose();
    }

//    private JMenuBar createMenuBar() {
//        final JMenuBar menuBar = new JMenuBar();
//        return menuBar;
//    }
//    private JPanel buttons() {
//        JPanel buttons = new JPanel();
//        buttons.setLayout(new GridLayout(1, 3, 10, 0));
//
//        final JButton quit = new JButton("Quit");
//
//        quit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int n = JOptionPane.showConfirmDialog(
//                        gameWindowFrame,
//                        "Are you sure you want to quit?",
//                        "Confirm quit", JOptionPane.YES_NO_OPTION);
//                if (n == JOptionPane.YES_OPTION) {
//                    if (timer != null) {
//                        timer.stop();
//                    }
//                    gameWindowFrame.dispose();
//                }
//            }
//        });
//        final JButton nGame = new JButton("New game");
//
//        nGame.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int n = JOptionPane.showConfirmDialog(
//                        gameWindowFrame,
//                        "Are you sure you want to begin a new game?",
//                        "Confirm new game", JOptionPane.YES_NO_OPTION);
//
//                if (n == JOptionPane.YES_OPTION) {
//                    SwingUtilities.invokeLater(new StartMenu());
//                    gameWindowFrame.dispose();
//                }
//            }
//        });
//
//        final JButton instr = new JButton("How to play");
//
//        instr.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(gameWindowFrame,
//                        "Move the chess pieces on the board by clicking\n"
//                        + "and dragging. The game will watch out for illegal\n"
//                        + "moves. You can win either by your opponent running\n"
//                        + "out of time or by checkmating your opponent.\n"
//                        + "\nGood luck, hope you enjoy the game!",
//                        "How to play",
//                        JOptionPane.PLAIN_MESSAGE);
//            }
//        });
    //buttons.add(instr);
    //buttons.add(nGame);
//        buttons.add(quit);
//
//        buttons.setPreferredSize(buttons.getMinimumSize());
//
//        return buttons;
//    }
//    private JPanel clockJPanel() {
//        JPanel gameData = new JPanel();
//        gameData.setLayout(new GridLayout(3, 2, 0, 0));
//        // PLAYER NAMES
//        JLabel w = new JLabel(whiteClock.getTime());
//        JLabel b = new JLabel(blackClock.getTime());
//
//        w.setHorizontalAlignment(JLabel.CENTER);
//        w.setVerticalAlignment(JLabel.CENTER);
//        b.setHorizontalAlignment(JLabel.CENTER);
//        b.setVerticalAlignment(JLabel.CENTER);
//
//        w.setSize(w.getMinimumSize());
//        b.setSize(b.getMinimumSize());
//
//        // CLOCKS
//        final JLabel bTime = new JLabel(blackClock.getTime());
//        final JLabel wTime = new JLabel(whiteClock.getTime());
//
//        bTime.setHorizontalAlignment(JLabel.CENTER);
//        bTime.setVerticalAlignment(JLabel.CENTER);
//        wTime.setHorizontalAlignment(JLabel.CENTER);
//        wTime.setVerticalAlignment(JLabel.CENTER);
//
    //       wChessTimer = new ChessTimer(boardPanel, whiteIsActive, whiteClock, wTime);
//        bChessTimer = new ChessTimer(boardPanel, !whiteIsActive, blackClock, bTime);
//        wChessTimer.start();
//        bChessTimer.start();
//        gameData.add(wTime);
//        gameData.add(bTime);
//
//        gameData.setPreferredSize(gameData.getMinimumSize());
//        return gameData;
//    }
//    private JPanel textJPanel() {
//        JPanel textPanel = new JPanel();
//        textArea = new JTextArea();
//        textArea.setEditable(false);
//        JScrollBar scrollBar = new JScrollBar();
//
//        textPanel.add(textArea);
//        textPanel.add(scrollBar);
//        textPanel.setPreferredSize(new Dimension(200, 400));
//        return textPanel;
//    }
//    public JTextArea getTextArea() {
//        return textArea;
//    }
    public String getGameView() {
        return gameView;
    }

    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    public GameWindowGameAuto getAuto() {
        return auto;
    }

    public GameWindowGameView getView() {
        return view;
    }

    public GameWindowGameComp getComp() {
        return comp;
    }

    public GameStateEnum getGameState() {
        return gameState;
    }

    public void setGameState(GameStateEnum gameState) {
        this.gameState = gameState;
    }

    public int getHH() {
        return hh;
    }

    public int getMM() {
        return mm;
    }

    public int getSS() {
        return ss;
    }

}
