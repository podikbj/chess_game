package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.ChessTimer;
import cz.cvut.fel.pjv.start.Clock;
import cz.cvut.fel.pjv.start.GameManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GameWindowGameAuto {

    private GameWindowBasic gameWindowBasic;
    private JFrame gameWindowFrame;
    private BoardPanel boardPanel;

    public Clock blackClock = null;
    public Clock whiteClock = null;

    private Thread wThread = null;
    private Thread bThread = null;

    private boolean whiteIsActive = true;
    private HashSet<String> tags = new HashSet<String>();

    private JTextArea textArea = new JTextArea();
    private GameManager gameManager = GameManager.getInstance();
    private LinkedList<Tile> tileList;
    private static Logger logger = Logger.getLogger(GameWindowGameAuto.class.getName());
    private String gameView = "";

    private int hh = 0;
    private int mm = 0;
    private int ss = 0;

    //private GameStateEnum gameState;
    public GameWindowGameAuto(GameWindowBasic gameWindowBasic, BoardPanel bp) {
        this.gameWindowBasic = gameWindowBasic;

        this.gameWindowFrame = new JFrame("Chess general");
        gameWindowFrame.setLocation(100, 100);
        final JMenuBar menuBar = createMenuBar();

        gameWindowFrame.setJMenuBar(menuBar);

        this.boardPanel = bp;

        this.tags = boardPanel.getTags();
        this.tileList = gameManager.getTileList();
        this.gameView = gameWindowBasic.getGameView();

        hh = gameWindowBasic.getHH();
        mm = gameWindowBasic.getMM();
        ss = gameWindowBasic.getSS();

        blackClock = new Clock(hh, mm, ss);
        whiteClock = new Clock(hh, mm, ss);

        JPanel clockPanel = clockJPanel();

        clockPanel.setSize(clockPanel.getPreferredSize());
        gameWindowFrame.add(clockPanel, BorderLayout.NORTH);

        JPanel textPanel = textJPanel();

        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);

        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);

        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
        gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
        gameWindowFrame.setSize(
                new Dimension(600, 600));
        gameWindowFrame.setResizable(
                false);
        gameWindowFrame.pack();

        gameWindowFrame.setVisible(
                true);

        gameWindowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenuSaveToPGN());
        return menuBar;
    }

    private JMenu createFileMenuSaveToPGN() {
        final JMenu fileMenu = new JMenu("File save");
        final JMenuItem savePNG = new JMenuItem("Save as PGN");
        savePNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File myFilePGN = null;

                final String pathPGN = "C:/Users/kira/OneDrive/Dokumenty/NetBeansProjects/ChessGame_2021/src/main/resources";
                //final String pathPGN = "src/main/resources/2021.04.14_wPLastNbPlayerLastN.pgn";
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(pathPGN));
                int result = fileChooser.showOpenDialog(gameWindowFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    myFilePGN = fileChooser.getSelectedFile();
                }

                //File myFilePGN = new File(pathPGN);
                try {
                    FileWriter writer = new FileWriter(myFilePGN, true);
                    BufferedWriter bw = new BufferedWriter(writer);
                    gameView = gameWindowBasic.getGameView();
                    bw.write(gameView);
                    bw.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error occur in saving Game Report", ex);
                    ex.printStackTrace();
                }

            }

        });
        fileMenu.add(savePNG);
        return fileMenu;
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

                if (n == JOptionPane.YES_OPTION) {
                    blackClock.setNull();
                    whiteClock.setNull();
                    gameWindowFrame.dispose();
                }
            }
        });

        final JButton nGame = new JButton("New game");
        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Are you sure you want to upload a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindowFrame.dispose();
                }
            }
        });

        buttons.add(nGame);
        buttons.add(quit);
        buttons.setPreferredSize(buttons.getMinimumSize());
        return buttons;

    }

    private JPanel clockJPanel() {

        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3, 2, 0, 0));

        final JLabel bTime = new JLabel(blackClock.getTime());
        final JLabel wTime = new JLabel(whiteClock.getTime());

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);

        ChessTimer wChessTimer = new ChessTimer(boardPanel, whiteClock, wTime);
        ChessTimer bChessTimer = new ChessTimer(boardPanel, blackClock, bTime);

        if (!(hh == 0 && mm == 0 && ss == 0)) {
            wThread = new Thread(wChessTimer, "White");
            wThread.start();
            bThread = new Thread(bChessTimer, "Black");
            bThread.start();
        } else {
            wTime.setText("Untimed game");
            bTime.setText("Untimed game");
        }

        gameData.add(wTime);
        gameData.add(bTime);

        gameData.setPreferredSize(gameData.getMinimumSize());
        return gameData;
    }

    private JPanel textJPanel() {
        JPanel textPanel = new JPanel();

        textArea.setEditable(false);
        JScrollBar scrollBar = new JScrollBar();

        textPanel.add(textArea);
        //textPanel.add(scrollBar);
        textPanel.setPreferredSize(new Dimension(250, 400));
        return textPanel;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    public void endGame(String winner) {
        if (winner == "1 - 0") {
            int n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "White wins by checkmate!",
                    "White wins!", JOptionPane.DEFAULT_OPTION);

        } else {

            int n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "Black wins by checkmate!",
                    "Black wins!", JOptionPane.DEFAULT_OPTION);

        }

        if (wThread == null && bThread == null) {
            return;
        }
        
        blackClock.setNull();
        whiteClock.setNull();
        try {
            bThread.join();
            wThread.join();
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}
