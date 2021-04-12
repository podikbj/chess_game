package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Players;
import cz.cvut.fel.pjv.start.ChessTimer;
import cz.cvut.fel.pjv.start.Clock;
import cz.cvut.fel.pjv.start.Report;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
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

public class GameWindow {

    private JFrame gameWindowFrame;
    private final static Dimension FRAME_DIM = new Dimension(600, 600);
    private BoardPanel boardPanel;
    //private LeftSidePanel leftSidePanel;
    private JTextArea textArea;

    public Clock blackClock;
    public Clock whiteClock;
    private boolean whiteIsActive;
    private ChessTimer wChessTimer;
    private ChessTimer bChessTimer;
    private String gameView = "";
    private static Logger logger = Logger.getLogger(GameForm.class.getName());

    public GameWindow() {
        this.gameWindowFrame = new JFrame("Chess");
        gameWindowFrame.setLocation(100, 100);
        final JMenuBar menuBar = createMenuBar();

        gameWindowFrame.setJMenuBar(menuBar);

        this.boardPanel = new BoardPanel(this);

        blackClock = new Clock(60, 0, 0);
        whiteClock = new Clock(60, 0, 0);

        JPanel gameData = clockJPanel();
        gameData.setSize(gameData.getPreferredSize());
        gameWindowFrame.add(gameData, BorderLayout.NORTH);

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);

        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);
        //gameWindowFrame.add(leftSidePanel, BorderLayout.WEST);

        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
        gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
        gameWindowFrame.setSize(FRAME_DIM);
        gameWindowFrame.setResizable(false);
        gameWindowFrame.pack();
        gameWindowFrame.setVisible(true);
//        this.gameWindowFrame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                blackClock.setNull();
//                whiteClock.setNull();
//                try {
//                    bChessTimer.join();
//                    wChessTimer.join();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            }
//        });
        gameWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu1());
        menuBar.add(createFileMenu2());
        return menuBar;
    }

    private JMenu createFileMenu1() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPNG = new JMenuItem("Open PGN file");
        openPNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Players players = Players.getInstance();
                String gameDate = players.getDate();
                String wPlayer = players.getWpName() + players.getWpLastName();
                String bPlayer = players.getBpName() + players.getBpLastName();

                final String pathPGN = "src/main/resources/"
                        + gameDate
                        + "_"
                        + wPlayer
                        + bPlayer
                        + ".pgn";
                final String pathTXT = "src/main/resources/"
                        + gameDate
                        + "_"
                        + wPlayer
                        + bPlayer
                        + ".txt";

                File myFilePGN = new File(pathPGN);
                File myFileTXT = new File(pathTXT);

                try {

                    PrintWriter writer = new PrintWriter(pathPGN);
                    //writer = new PrintWriter(pathPGN);
                    writer.println(gameView);
//                    writer.flush();
//                    writer.close();

                    //writer = new PrintWriter(pathPGN);
                    writer = new PrintWriter(pathTXT);
                    writer.println(gameView);
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error occur in saving GameReport", ex);
                    ex.printStackTrace();
                }

            }
        });
        fileMenu.add(openPNG);
        return fileMenu;
    }

    private JMenu createFileMenu2() {
        final JMenu fileMenu = new JMenu("Game");
        final JMenuItem uploadGame = new JMenuItem("Upload game from file");
        uploadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Players players = Players.getInstance();
                String gameDate = players.getDate();
                String wPlayer = players.getWpName() + players.getWpLastName();
                String bPlayer = players.getBpName() + players.getBpLastName();

                final String path = "src/main/resources/"
                        + gameDate
                        + "_"
                        + wPlayer
                        + bPlayer
                        + ".txt";

                File myFile = new File(path);

                try {

//                    PrintWriter writer = new PrintWriter(myFile);
//                    writer = new PrintWriter(myFile);
//                    writer.println(gameView);
//                    writer.flush();
//                    writer.close();
                    FileReader fr = new FileReader(path);
                    Scanner scan = new Scanner(fr);
                    List<String> lines = new ArrayList<>();
                    while (scan.hasNextLine()) {
                        String[] split = scan.nextLine().split(" . ", 0);
                        for (String s : split) {
                            lines.add(s);
                        }
                    }

                    int a = 2;
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error occur in saving GameReport", ex);
                    ex.printStackTrace();
                }

            }
        });
        fileMenu.add(uploadGame);
        return fileMenu;
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final JButton quit = new JButton("Quit");

        buttons.add(quit);
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
        //   textPanel.setLayout(new GridLayout(3, 2, 0, 0));
        textPanel.setPreferredSize(new Dimension(100, 400));
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

}
