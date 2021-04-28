package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.ChessTimer;
import cz.cvut.fel.pjv.start.Clock;
import cz.cvut.fel.pjv.start.GameManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public Clock blackClock;
    public Clock whiteClock;
    private ChessTimer wChessTimer;
    private ChessTimer bChessTimer;

    private boolean whiteIsActive = true;
    private HashSet<String> tags = new HashSet<String>();

    private JTextArea textArea = new JTextArea();
    private GameManager gameManager = GameManager.getInstance();
    private LinkedList<Tile> tileList;
    private static Logger logger = Logger.getLogger(GameForm.class.getName());
    private String gameView = "";

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

        blackClock = new Clock(60, 0, 0);
        whiteClock = new Clock(60, 0, 0);

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);
        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);

        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
        gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
        gameWindowFrame.setSize(new Dimension(600, 600));
        gameWindowFrame.setResizable(false);
        gameWindowFrame.pack();
        gameWindowFrame.setVisible(true);
        gameWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

//                Players players = Players.getInstance();
//                String gameDate = players.getDate();
//                String wPlayer = players.getWpLastName();
//                String bPlayer = players.getBpLastName();
//                final String pathPGN = "src/main/resources/"
//                        + gameDate
//                        + "_"
//                        + wPlayer
//                        + bPlayer
//                        + ".pgn";
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

}
