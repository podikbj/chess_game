package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Players;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.ChessTimer;
import cz.cvut.fel.pjv.start.Clock;
import cz.cvut.fel.pjv.start.GameManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
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

/**
 *
 * @author kira
 */
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

    private String pathPGN;
    private File myFilePGN;
    private String winner;

    private int gameIsOver = 1;
    private Players pl = Players.getInstance();
    private ArrayList< StringBuilder> initialGameData = new ArrayList<>();

    /**
     *
     * @param gameWindowBasic
     * @param bp
     */
    public GameWindowGameAuto(GameWindowBasic gameWindowBasic, BoardPanel bp) {
        this.gameWindowBasic = gameWindowBasic;

        this.gameWindowFrame = new JFrame("Chess general");
        gameWindowFrame.setLocation(100, 100);

        this.boardPanel = bp;

        this.tags = boardPanel.getTags();
        this.tileList = gameManager.getTileList();
        this.gameView = gameWindowBasic.getGameView();

        hh = gameWindowBasic.getHH();
        mm = gameWindowBasic.getMM();
        ss = gameWindowBasic.getSS();

        blackClock = new Clock(hh, mm, ss, 0);
        whiteClock = new Clock(hh, mm, ss, 1);

        Box components = Box.createVerticalBox();
        components.setSize(components.getPreferredSize());
        Box clockBox = clockJPanel();
        clockBox.setSize(clockBox.getPreferredSize());
        components.add(clockBox, BorderLayout.SOUTH);
        gameWindowFrame.add(components, BorderLayout.NORTH);

        final JMenuBar menuBar = createMenuBar();
        gameWindowFrame.setJMenuBar(menuBar);

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);

        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);
        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
        //gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
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

                setMyFilePGN();

                try {
                    PrintWriter writer = new PrintWriter(myFilePGN);
                    writer.print("");
                    writer.close();
                    writer = new PrintWriter(myFilePGN);

                    fillInInitialGameData();
                    StringBuilder sb = new StringBuilder();
                    addToInitialGameData(sb.append("[Result ]")
                            .append("\n"));

                    for (StringBuilder element : initialGameData) {
                        String str = element.toString();
                        writer.println(str);
                    }
                    saveGameData(writer);

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Can't save game to file", ex);
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
                        "Do you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    blackClock.setNull();
                    whiteClock.setNull();
                    gameWindowFrame.dispose();
                }
            }
        });

        final JButton gGame = new JButton("Start game after manual setting");

        gGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StartMenu.isManual = false;
            }
        });

        final JButton nGame = new JButton("New game");
        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Do you want to load a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindowFrame.dispose();
                }
            }
        });

        final JButton draw = new JButton("Draw");
        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Do you want to end the game in a draw?",
                        "Confirm draw", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    setMyFilePGN();
                    winner = "1/2 - 1/2";
                    saveGame(winner);

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
                    gameWindowFrame.dispose();
                }
            }
        }
        );

        buttons.add(nGame);

        buttons.add(gGame);
        buttons.add(draw);
        buttons.add(quit);

        buttons.setPreferredSize(buttons.getMinimumSize());
        return buttons;

    }

    private Box clockJPanel() {

        Box clockBox = Box.createHorizontalBox();

        JPanel wClockPanel = new JPanel();
        final JLabel wLabel = new JLabel("White");
        final JLabel wTime = new JLabel(whiteClock.getTime());
        //wTime.setSize(new Dimension(20, 15));

        JPanel bClockPanel = new JPanel();
        final JLabel bLabel = new JLabel("Black");
        final JLabel bTime = new JLabel(blackClock.getTime());

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);

        ChessTimer wChessTimer = new ChessTimer(boardPanel, whiteClock, wTime, this);
        ChessTimer bChessTimer = new ChessTimer(boardPanel, blackClock, bTime, this);

        if (!(hh == 0 && mm == 0 && ss == 0)) {
            wThread = new Thread(wChessTimer, "White");
            wThread.start();
            bThread = new Thread(bChessTimer, "Black");
            bThread.start();

        } else {
            wTime.setText("Untimed game");
            bTime.setText("Untimed game");
        }

        wClockPanel.add(wLabel);
        wClockPanel.add(wTime);

        bClockPanel.add(bLabel);
        bClockPanel.add(bTime);

        clockBox.add(wClockPanel);
        clockBox.add(bClockPanel);

        clockBox.setPreferredSize(clockBox.getPreferredSize());

        return clockBox;
    }

    private JPanel textJPanel() {
        JPanel textPanel = new JPanel();

        textArea.setEditable(false);
        textPanel.add(textArea);
        textPanel.setPreferredSize(new Dimension(250, 400));
        return textPanel;
    }

    /**
     *
     * @return
     */
    public JTextArea getTextArea() {
        return textArea;
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
    public JFrame getGameWindowFrame() {
        return gameWindowFrame;
    }

    /**
     *
     * @param winner
     * @param byTime
     */
    public void endGame(String winner, boolean byTime) {
        int n;
        StringBuilder sb = new StringBuilder();

        if (byTime) {
            sb.append(" wins by time! Save the game? \n"
                    + "Choosing \"No\" quits the game.");

        } else {
            sb.append(" wins by checkmate! Save the game? \n"
                    + "Choosing \"No\" quits the game.");
        }
        String message = (winner.equals("1 - 0"))
                ? "White" + sb.toString() : "Black" + sb.toString();

        String title = (winner.equals("1 - 0"))
                ? "White wins!" : "Black wins!";

        n = JOptionPane.showConfirmDialog(
                gameWindowFrame,
                message, "Confirm save", JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            saveGame(winner);
        } else {
            gameWindowFrame.dispose();
        }

        if (wThread == null && bThread == null) {
            return;
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
        gameWindowFrame.dispose();
    }

    private void saveGame(String result) {

        setMyFilePGN();

        try {
            PrintWriter writer = new PrintWriter(myFilePGN);
            writer.print("");
            writer.close();
            writer = new PrintWriter(myFilePGN);

            initialGameData.clear();
            StringBuilder sb = new StringBuilder();
            addToInitialGameData(sb.append("[Event \"").append(pl.getCupName()).append("\"]"));

            sb = new StringBuilder();
            addToInitialGameData(sb.append("[Site \"").append(pl.getCity() + ", ").append(pl.getCountry()).append("\"]"));

            sb = new StringBuilder();
            addToInitialGameData(sb.append("[Date \"").append(pl.getDate()).append("\"]"));

            sb = new StringBuilder();
            addToInitialGameData(sb.append("[Round \"").append(pl.getRound()).append("\"]"));

            sb = new StringBuilder();
            addToInitialGameData(sb.append("[White \"").append(pl.getWpLastName())
                    .append(", ")
                    .append(pl.getWpName())
                    .append("\"]"));

            sb = new StringBuilder();
            addToInitialGameData(sb.append("[Black \"").append(pl.getBpLastName())
                    .append(", ")
                    .append(pl.getBpName())
                    .append("\"]"));

            sb = new StringBuilder();
            addToInitialGameData(sb.append("[Result \"").append(result)
                    .append("\"]")
                    .append("\n"));

            for (StringBuilder element : initialGameData) {
                String str = element.toString();
                writer.println(str);
            }
            saveGameData(writer);

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't save game to file", ex);
            ex.printStackTrace();
        }
    }

    private void setMyFilePGN() {
//        if (myFilePGN != null) {
//            return;
//        }
        myFilePGN = new File("src/main/resources/"
                + pl.getDate()
                + "_"
                + pl.getWpLastName()
                + pl.getBpLastName()
                + ".pgn");

        pathPGN = "C:/Users/kira/OneDrive/Dokumenty/NetBeansProjects/ChessGame_2021/src/main/resources";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(pathPGN));
        int result = fileChooser.showOpenDialog(gameWindowFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            myFilePGN = fileChooser.getSelectedFile();
        }
    }

    private void fillInInitialGameData() {

        initialGameData.clear();
        StringBuilder sb = new StringBuilder();
        addToInitialGameData(sb.append("[Event \"").append(pl.getCupName()).append("\"]"));

        sb = new StringBuilder();
        addToInitialGameData(sb.append("[Site \"").append(pl.getCity() + ", ").append(pl.getCountry()).append("\"]"));

        sb = new StringBuilder();
        addToInitialGameData(sb.append("[Date \"").append(pl.getDate()).append("\"]"));

        sb = new StringBuilder();
        addToInitialGameData(sb.append("[Round \"").append(pl.getRound()).append("\"]"));

        sb = new StringBuilder();
        addToInitialGameData(sb.append("[White \"").append(pl.getWpLastName())
                .append(", ")
                .append(pl.getWpName())
                .append("\"]"));

        sb = new StringBuilder();
        addToInitialGameData(sb.append("[Black \"").append(pl.getBpLastName())
                .append(", ")
                .append(pl.getBpName())
                .append("\"]"));
    }

    private void addToInitialGameData(StringBuilder sb) {
        initialGameData.add(sb);
    }

    private void saveGameData(PrintWriter writer) {

        try {
            BufferedWriter bw = new BufferedWriter(writer);
            gameView = gameWindowBasic.getGameView();
            bw.write(gameView);
            bw.close();

            writer.flush();
            writer.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't find file", ex);
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public synchronized void decrementGameIsOver() {
        this.gameIsOver--;
    }

    /**
     *
     * @return
     */
    public synchronized int getGameIsOver() {
        return gameIsOver;
    }
}
