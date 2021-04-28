package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.Clock;
import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.start.MovesIterator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GameWindowGameView {

    private JFrame gameWindowFrame;
    private GameWindowBasic gameWindowBasic;
    private BoardPanel boardPanel;
    private GameManager gameManager = GameManager.getInstance();
    private boolean whiteIsActive = true;
    private MovesIterator it = MovesIterator.getInstance();
    private HashSet<String> tags = new HashSet<String>();
    private Piece currentPiece = null;
    private Tile tile = null;
    private LinkedList<Tile> tileList;
    private static Logger logger = Logger.getLogger(GameForm.class.getName());
    private String gameView = "";
    private JTextArea textArea = new JTextArea();
    private StringBuilder sb = new StringBuilder();
    private boolean firstStep = true;
    private String lastNextTag = "";
    private String lastPreviouseTag = "";
    private int counter = 0;
    private LinkedList<Piece> removedPieces = new LinkedList<Piece>();

    public GameWindowGameView(GameWindowBasic gameWindowBasic, BoardPanel bp) {

        this.gameWindowBasic = gameWindowBasic;

        this.gameWindowFrame = new JFrame("Chess upload");
        gameWindowFrame.setLocation(100, 100);
        final JMenuBar menuBar = createMenuBar();

        gameWindowFrame.setJMenuBar(menuBar);

        this.boardPanel = bp;

        this.tags = boardPanel.getTags();
        this.tileList = gameManager.getTileList();
        this.gameView = gameWindowBasic.getGameView();

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
        menuBar.add(createFileMenuUploadFromPGN());
        return menuBar;
    }

    private JMenu createFileMenuUploadFromPGN() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPNG = new JMenuItem("Upload game");
        openPNG.addActionListener(new ActionListener() {
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
                counter++;
                if (counter > 1) {
                    JOptionPane.showMessageDialog(gameWindowFrame, "To start new game press New game button");
                    return;
                }

                final String pathPGN = "src/main/resources/2021.04.14_wPLastNbPlayerLastN.pgn";
                LinkedList<String> moves = new LinkedList<>();

                File myFilePGN = new File(pathPGN);
                int counter = 0;
                try (
                        BufferedReader br = new BufferedReader(new FileReader(myFilePGN));) {

                    String itemLine = "";

                    String itemSubLine1 = "";
                    String itemSubLine2 = "";
                    sb.append(gameView);
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        if (counter < 7) {
                            counter++;
                            continue;
                        }
                        counter++;
                        sb.append(line).append("\n");

                        boolean wasKilled = false;
                        String[] itemsLine = line.split(" ");

                        for (int i = 0; i < itemsLine.length; i++) {
                            if (i % 3 == 0) {
                                whiteIsActive = true;
                                continue;
                            }

                            it.addMovement(itemsLine[i]);
                            doNextTag(itemsLine[i]);
                            whiteIsActive = false;
                            boardPanel.repaint();
                        }
                        gameView = sb.toString();

                    }

                    br.close();

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error occur in uploading Game", ex);
                }
                textArea.setText(gameView);
            }

        }
        );

        fileMenu.add(openPNG);
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

        final JButton next = new JButton("Next");

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (it.getPos() == it.getSize() - 1) {
                    return;
                }
                final int x = 1;
                it.next(x);
                String element = it.getCurrentElement();

                whiteIsActive = (it.getPos() % 2 == 1) ? true : false;
                int color = (whiteIsActive == true) ? 0 : 1;
                
                doNextTag(element);
                whiteIsActive = !whiteIsActive;
                boardPanel.repaint();
                lastNextTag = element;
            }
        });

        final JButton previous = new JButton("Previous");

        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (it.getPos() == 0) {
                    return;
                }
                final int x = -1;
                String element = it.getCurrentElement();

                whiteIsActive = (it.getPos() % 2 == 1) ? true : false;
                int color = (whiteIsActive == true) ? 0 : 1;

                it.next(x);
                boolean wasKilled = element.contains("x");

                String[] itemsSubLine = getSubTegs(element);
                if (itemsSubLine[0].equals("O")) {
                    doCastling(itemsSubLine, false);
                    boardPanel.repaint();
                    return;
                }

                setCurrentTile(itemsSubLine[0]);
                Tile finTilef1 = tile;
                setCurrentTile(itemsSubLine[1]);
                currentPiece = tile.getCurrentPiece();
                gameManager.move(currentPiece, finTilef1, tags, removedPieces);
                boardPanel.repaint();
                if (wasKilled) {

                    final String f2Pawn = "P";

                    String itemSub1 = itemsSubLine[1];
                    if (itemSub1.contains("+")) {
                        itemSub1 = itemSub1.replace("+", " ").trim();
                    }
                    final String f2 = itemSub1.substring(0, 1);
                    //final String f2 = itemsSubLine[1].substring(0, 1);
                    //final String f = (itemsSubLine[1].length() == 3) ? f2 : f2Pawn;
                    final String f = Character.isUpperCase(f2.charAt(0)) ? f2 : f2Pawn;
                    Tile finTilef2 = tile;
                    tile = tileList.stream().filter(p -> p.getX() < 4)
                            .filter(p -> p.getCurrentPiece().getColor() == color)
                            .filter(p -> p.getCurrentPiece().toString().equals(f))
                            .findFirst().get();

                    currentPiece = tile.getCurrentPiece();
                    gameManager.move(currentPiece, finTilef2, tags, removedPieces);
                    boardPanel.repaint();
                }
                lastPreviouseTag = element;
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

        buttons.add(previous);
        buttons.add(next);
        buttons.add(nGame);
        buttons.add(quit);
        buttons.setPreferredSize(buttons.getMinimumSize());
        return buttons;

    }

    private int getX(String str) {
        HashMap<String, Integer> chMap = new HashMap<>();

        chMap.put("a", 4);
        chMap.put("b", 5);
        chMap.put("c", 6);
        chMap.put("d", 7);
        chMap.put("e", 8);
        chMap.put("f", 9);
        chMap.put("g", 10);
        chMap.put("h", 11);
        if (chMap.containsKey(str)) {
            return chMap.get(str);
        } else {
            return -1;
        }

    }

    private void doNextTag(String tag) {

        if (tag.contains("+")) {
            tag = tag.replace("+", " ").trim();
        }
        boolean wasKilled = tag.contains("x");
        String[] itemsSubLine = getSubTegs(tag);
        if (itemsSubLine == null) {
            return;
        }

        if (itemsSubLine[0].equals("O")) {
            doCastling(itemsSubLine, true);
            return;
        }

        setCurrentTile(itemsSubLine[0]);
        currentPiece = tile.getCurrentPiece();
        setCurrentTile(itemsSubLine[1]);

        gameManager.move(currentPiece, tile, tags, removedPieces);

        if (wasKilled) {
            boardPanel.addRemovedPiecesToLSP();
        }

    }

    private String[] getSubTegs(String tag) {

        if (tag.contains("+")) {
            tag = tag.replace("+", " ").trim();
        }
        String[] itemsSubLine = null;
        boolean wasKilled = tag.contains("x");
        if (wasKilled) {
            itemsSubLine = tag.split("x");
        } else {
            itemsSubLine = tag.split("-");
        }

        return itemsSubLine;

    }

    private void setCurrentTile(String subTeg) {
        if (subTeg.length() == 2) {
            final int xStart = getX(subTeg.substring(0, 1));
            final int yStart = Integer.parseInt(subTeg.substring(1)) - 1;
            tile = tileList.stream().filter(p -> p.getX() == xStart && p.getY() == yStart)
                    .findFirst().get();
        }
        if (subTeg.length() > 2) {
            final int xStart = getX(subTeg.substring(1, 2));
            final int yStart = Integer.parseInt(subTeg.substring(2, 3)) - 1;
            tile = tileList.stream().filter(p -> p.getX() == xStart && p.getY() == yStart)
                    .findFirst().get();
        }
    }

    private void doCastling(String[] itemsSubLine, boolean straight) {

        String[] start = {"h1", "a1", "h8", "a8"};
        String[] finish = {"f1", "d1", "f8", "d8"};

        if (straight == false) {
            String[] temp = Arrays.copyOf(start, start.length);
            start = Arrays.copyOf(finish, finish.length);
            finish = Arrays.copyOf(temp, temp.length);
        }

        if (whiteIsActive && itemsSubLine.length == 2) {
            //setCurrentTile("h1");
            setCurrentTile(start[0]);
        }
        if (whiteIsActive && itemsSubLine.length == 3) {
            //setCurrentTile("a1");
            setCurrentTile(start[1]);
        }
        if (!whiteIsActive && itemsSubLine.length == 2) {
            //setCurrentTile("h8");
            setCurrentTile(start[2]);
        }
        if (!whiteIsActive && itemsSubLine.length == 3) {
            //setCurrentTile("a8");
            setCurrentTile(start[3]);
        }
        currentPiece = tile.getCurrentPiece();
        if (whiteIsActive && itemsSubLine.length == 2) {
            //setCurrentTile("f1");
            setCurrentTile(finish[0]);
        }
        if (whiteIsActive && itemsSubLine.length == 3) {
            //setCurrentTile("d1");
            setCurrentTile(finish[1]);
        }
        if (!whiteIsActive && itemsSubLine.length == 2) {
            //setCurrentTile("f8");
            setCurrentTile(finish[2]);
        }
        if (!whiteIsActive && itemsSubLine.length == 3) {
            //setCurrentTile("d8");
            setCurrentTile(finish[3]);
        }
        gameManager.getCheckMatePositionControl().setWhiteIsActive(whiteIsActive);
        gameManager.move(currentPiece, tile, tags, removedPieces);

        if (straight == false) {
            currentPiece.setWasMoved(false);
            tags.add("R");
        }

        gameManager.doCastling(tile, tags, removedPieces);
    }
//    
//    public void swap(String[] start, String[] finish){
//        String[] temp = Arrays.copyOf(start, start.length);
//        start = Arrays.copyOf(finish, finish.length);
//        finish = Arrays.copyOf(temp, temp.length);     
//    }

    private JPanel textJPanel() {
        JPanel textPanel = new JPanel();

        textArea.setEditable(false);
        JScrollBar scrollBar = new JScrollBar();

        textPanel.add(textArea);
        //textPanel.add(scrollBar);
        textPanel.setPreferredSize(new Dimension(200, 400));
        return textPanel;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

}
