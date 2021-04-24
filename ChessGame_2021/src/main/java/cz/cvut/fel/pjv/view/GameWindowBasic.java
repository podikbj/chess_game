package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Players;
import cz.cvut.fel.pjv.chessgame.Rook;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.ChessTimer;
import cz.cvut.fel.pjv.start.Clock;
import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.start.MovesIterator;
import cz.cvut.fel.pjv.start.Report;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import jdk.jshell.JShellException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.SwingUtilities;

public class GameWindowBasic {

    private JFrame gameWindowFrame;
    private final static Dimension FRAME_DIM = new Dimension(600, 600);
    
    private GameWindowGameAuto auto;
    private GameWindowGameView view;
    private GameWindowGameComp comp;
    
    private JTextArea textArea;
    private GameManager gameManager;

    public Clock blackClock;
    public Clock whiteClock;
    private boolean whiteIsActive = true;
    private ChessTimer wChessTimer;
    private ChessTimer bChessTimer;
    private String gameView = "";
    private MovesIterator it = MovesIterator.getInstance();
    private Stack<String> tagType;
    private Piece currentPiece = null;
    private Tile tile = null;
    private LinkedList<Tile> tileList;
    private GameStateEnum gameState;

    private static Logger logger = Logger.getLogger(GameForm.class.getName());

    public GameWindowBasic() {
        this.gameWindowFrame = new JFrame("Chess basic");
        gameWindowFrame.setLocation(100, 100);
        final JMenuBar menuBar = createMenuBar();

        gameWindowFrame.setJMenuBar(menuBar);

        // this.boardPanel = new BoardPanel();
        this.gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        gameManager.initializePieces();
        gameManager.initializeCheckMatePositionControl();
        //  this.tagType = boardPanel.getTagType();
        this.tileList = gameManager.getTileList();

        blackClock = new Clock(60, 0, 0);
        whiteClock = new Clock(60, 0, 0);

        //MovesIterator it = null;
        JPanel gameData = clockJPanel();
        gameData.setSize(gameData.getPreferredSize());
        gameWindowFrame.add(gameData, BorderLayout.NORTH);

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);

        //  gameWindowFrame.add(boardPanel, BorderLayout.CENTER);
        //gameWindowFrame.add(leftSidePanel, BorderLayout.WEST);
        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
        gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
        gameWindowFrame.setSize(FRAME_DIM);
        gameWindowFrame.setResizable(false);
        gameWindowFrame.pack();
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
        gameWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createGameModes(BoardPanel bp, GameStateEnum mode) {
        gameState = mode;
        switch (mode) {
            case GENERAL:
                auto = new GameWindowGameAuto(this, bp);
                break;
            case UPLOAD:
                view = new GameWindowGameView(this, bp);
                break;
            case IIMODEL:
                comp = new GameWindowGameComp(this, bp);
                break;
        }

    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        return menuBar;
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final JButton quit = new JButton("Quit");

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Are you sure you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);

//                if (n == JOptionPane.YES_OPTION) {
//                    if (timer != null) {
//                        timer.stop();
//                    }
//                    gameWindowFrame.dispose();
//                }
            }
        });

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

        buttons.add(quit);

        buttons.setPreferredSize(buttons.getMinimumSize());

        return buttons;
    }

    private JPanel clockJPanel() {
        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3, 2, 0, 0));
        // PLAYER NAMES
        JLabel w = new JLabel(whiteClock.getTime());
        JLabel b = new JLabel(blackClock.getTime());

        w.setHorizontalAlignment(JLabel.CENTER);
        w.setVerticalAlignment(JLabel.CENTER);
        b.setHorizontalAlignment(JLabel.CENTER);
        b.setVerticalAlignment(JLabel.CENTER);

        w.setSize(w.getMinimumSize());
        b.setSize(b.getMinimumSize());

        // CLOCKS
        final JLabel bTime = new JLabel(blackClock.getTime());
        final JLabel wTime = new JLabel(whiteClock.getTime());

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);
//
        //       wChessTimer = new ChessTimer(boardPanel, whiteIsActive, whiteClock, wTime);
//        bChessTimer = new ChessTimer(boardPanel, !whiteIsActive, blackClock, bTime);
//        wChessTimer.start();
//        bChessTimer.start();

        gameData.add(wTime);
        gameData.add(bTime);

        gameData.setPreferredSize(gameData.getMinimumSize());
        return gameData;
    }

    private JPanel textJPanel() {
        JPanel textPanel = new JPanel();
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollBar scrollBar = new JScrollBar();

        textPanel.add(textArea);
        textPanel.add(scrollBar);
         textPanel.setPreferredSize(new Dimension(200, 400));
        return textPanel;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

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
    
    
    
    


}
