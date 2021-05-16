package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Pawn;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Players;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.CheckMatePositionControl;
import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.start.MovesIterator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Represents game window for game load/game view game mode
 *
 * @author kira
 */
public class GameWindowGameView {

    private final JFrame gameWindowFrame;
    private final GameWindowBasic gameWindowBasic;
    private final BoardPanel boardPanel;
    private final GameManager gameManager = GameManager.getInstance();
    private boolean whiteIsActive = true;
    private final MovesIterator it = MovesIterator.getInstance();
    private HashSet<String> tags = new HashSet<String>();
    private Piece currentPiece = null;
    private Tile tile = null;
    private final LinkedList<Tile> tileList;
    private final static Logger logger = Logger.getLogger(GameWindowGameView.class.getName());
    private String gameView = "";
    private JTextArea textArea = new JTextArea();
    private StringBuilder sb = new StringBuilder();
    private final JMenu fileMenu = new JMenu("File");
    private int tryCounter = 0;
    private LinkedList<Piece> lastRemoved = new LinkedList<>();
    private GameStateEnum gameState;

    private Players pl = Players.getInstance();
    private ArrayList< StringBuilder> initialGameData = new ArrayList<>();
    private String pathPGN;
    private File myFilePGN;
    private String winner = "";

    int x = -1;
    String s = null;
    String trimedTag;

    /**
     * Constructor for GameWindowGameView game window instance.
     *
     * @param gameWindowBasic basic game window
     * @param bp chess board
     * @param gameState current game mode
     */
    public GameWindowGameView(GameWindowBasic gameWindowBasic, BoardPanel bp, GameStateEnum gameState) {

        this.gameWindowBasic = gameWindowBasic;

        this.gameWindowFrame = new JFrame("Chess load");
        gameWindowFrame.setLocation(100, 100);
        final JMenuBar menuBar = createMenuBar();

        gameWindowFrame.setJMenuBar(menuBar);

        this.boardPanel = bp;

        this.tags = boardPanel.getTags();
        this.tileList = gameManager.getTileList();
        this.gameView = gameWindowBasic.getGameView();
        this.gameState = gameState;

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
        menuBar.add(createFileMenuLoadFromPGNForView());
        menuBar.add(createFileMenuLoadFromPGNForGame());
        menuBar.add(createFileMenuSaveToPGN());
        return menuBar;
    }

    private JMenu createFileMenuLoadFromPGNForView() {

        final JMenuItem openPNG = new JMenuItem("View game mode");
        openPNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameStateEnum.LOAD_GAME) {
                    JOptionPane.showMessageDialog(openPNG, "Only for view game mode");
                    return;
                }
                gameWindowFrame.setTitle("View game mode");
                loadGameFromFile();
                boardPanel.repaint();
            }
        });
        fileMenu.add(openPNG);
        return fileMenu;
    }

    private JMenu createFileMenuLoadFromPGNForGame() {
        final JMenuItem openPNG = new JMenuItem("Play game after loading mode");
        openPNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameStateEnum.LOAD_VIEW) {
                    JOptionPane.showMessageDialog(openPNG, "Only for play game mode");
                    return;
                }
                gameWindowFrame.setTitle("Play game mode");
                loadGameFromFile();
                boardPanel.setWhiteIsActive(whiteIsActive);
                tags.clear();
                boardPanel.repaint();
            }
        });
        fileMenu.add(openPNG);
        return fileMenu;
    }

    private void loadGameFromFile() {

        File myFilePGN = null;

        final String pathPGN = "C:/Users/kira/OneDrive/Dokumenty/NetBeansProjects/ChessGame_2021/src/main/resources";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(pathPGN));
        int result = fileChooser.showOpenDialog(gameWindowFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            myFilePGN = fileChooser.getSelectedFile();
        }

        tryCounter++;
        if (tryCounter > 1) {
            JOptionPane.showMessageDialog(gameWindowFrame, "To start new game press New game button");
            return;
        }

        int counterTitle = 0;
        int counter = 0;
        try (
                BufferedReader br = new BufferedReader(new FileReader(myFilePGN));) {

            sb.append(gameView);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (counterTitle < 8) {
                    counterTitle++;
                    continue;
                }

                sb.append(line).append("\n");

                String[] itemsLine = line.split(" ");

                for (int i = 0; i < itemsLine.length; i++) {

                    counter++;
                    if (i % 3 == 0) {
                        whiteIsActive = true;
                        counter--;
                        continue;
                    }

                    it.addMovement(itemsLine[i]);
                    doNextTag(itemsLine[i]);
                    whiteIsActive = !whiteIsActive;
                    boardPanel.setWhiteIsActive(whiteIsActive);
                    boardPanel.repaint();
                }
                gameView = sb.toString();
            }
            br.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error occurs in loading Game", ex);
        }
        boardPanel.setCounter(counter);
        textArea.setText(gameView);
        gameWindowBasic.setGameView(gameView);
    }

    private JMenu createFileMenuSaveToPGN() {
        final JMenuItem savePNG = new JMenuItem("Save as PGN");
        savePNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameStateEnum.LOAD_VIEW) {
                    JOptionPane.showMessageDialog(savePNG, "Only for play game mode");
                    return;
                }
                saveGame(winner);
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

            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Do you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    gameWindowFrame.dispose();
                }
            }
        });

        final JButton next = new JButton("Next");
        next.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameStateEnum.LOAD_GAME) {
                    JOptionPane.showMessageDialog(next, "Only for view game mode");
                    return;
                }
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
            }
        });

        final JButton previous = new JButton("Previous");
        previous.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState == GameStateEnum.LOAD_GAME) {
                    JOptionPane.showMessageDialog(previous, "Only for view game mode");
                    return;
                }
                if (it.getPos() == 0) {
                    return;
                }
                final int x = -1;
                String element = it.getCurrentElement();

                whiteIsActive = (it.getPos() % 2 == 1) ? true : false;
                int color = (whiteIsActive == true) ? 0 : 1;

                it.next(x);
                boolean wasKilled = element.contains("x");

                if (element.contains("+")) {
                    element = element.replace("+", " ").trim();
                }
                if (element.contains("#")) {
                    element = element.replace("#", " ").trim();
                }
                if (element.contains("1/2-1/2")) {
                    element = element.replace("1/2 - 1/2", " ").trim();
                }

                boolean changePawn = false;
                //String[] tagArr = {element};
                trimedTag = element;
                if (trimTagsEnd()) {
                    changePawn = true;
                }
                String[] itemsSubLine = getSubTegs(trimedTag);

                if (itemsSubLine[0].equals("O")) {
                    doCastling(itemsSubLine, false);
                    boardPanel.repaint();
                    return;
                }

                setCurrentTile(itemsSubLine[0]);
                Tile finTilef1 = tile;
                setCurrentTile(itemsSubLine[1]);
                currentPiece = tile.getCurrentPiece();
                if (changePawn) {
                    int c = currentPiece.getColor();
                    tile.removePiece();
                    currentPiece = null;
                    Pawn newPawn = new Pawn(c, tile, true);
                    tile.setCurrentPiece(newPawn);
                    currentPiece = newPawn;
                }
                gameManager.move(currentPiece, finTilef1, tags, lastRemoved);

                gameManager.addLastMove(tile, 0);
                gameManager.addLastMove(finTilef1, 1);

                boardPanel.repaint();
                if (wasKilled) {

                    final String f2Pawn = "P";

                    String itemSub1 = itemsSubLine[1];
                    if (itemSub1.contains("+")) {
                        itemSub1 = itemSub1.replace("+", " ").trim();
                    }
                    if (itemSub1.contains("#")) {
                        itemSub1 = itemSub1.replace("#", " ").trim();
                    }
                    if (itemSub1.contains("1/2-1/2")) {
                        itemSub1 = itemSub1.replace("1/2-1/2", " ").trim();
                    }
                    final String f2 = itemSub1.substring(0, 1);
                    final String f = Character.isUpperCase(f2.charAt(0)) ? f2 : f2Pawn;
                    Tile finTilef2 = tile;
                    currentPiece = lastRemoved.getLast();
                    lastRemoved.removeLast();
                    gameManager.move(currentPiece, finTilef2, tags, lastRemoved);
                    gameManager.addLastMove(tile, 0);
                    gameManager.addLastMove(finTilef2, 1);

                    boardPanel.repaint();
                }
            }
        }
        );

        final JButton nGame = new JButton("New game");

        nGame.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Do you want to load a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindowFrame.dispose();
                }
            }
        }
        );

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
        if (tag.contains("#")) {
            tag = tag.replace("#", " ").trim();
        }
        if (tag.contains("1/2-1/2")) {
            tag = tag.replace("1/2-1/2", " ").trim();
        }

        trimedTag = tag;
        boolean changePawn = trimTagsEnd();

        boolean wasKilled = trimedTag.contains("x");

        String[] itemsSubLine = getSubTegs(trimedTag);
        if (itemsSubLine == null) {
            return;
        }

        if (itemsSubLine[0].equals("O")) {
            doCastling(itemsSubLine, true);
            return;
        }

        setCurrentTile(itemsSubLine[0]);
        Tile startTile = tile;
        currentPiece = tile.getCurrentPiece();
        gameManager.addLastMove(tile, 0);

        setCurrentTile(itemsSubLine[1]);

        tags.clear();
        gameManager.move(currentPiece, tile, tags, lastRemoved);
        CheckMatePositionControl checkMatePositionControl = gameManager.getCheckMatePositionControl();
        checkMatePositionControl.setWhiteIsActive(whiteIsActive);
        if (changePawn) {
            gameManager.changePawn(tile, whiteIsActive, x, tags);
            tags.add(s);
        }
        if (checkMatePositionControl.doesMoveCauseCheck(currentPiece, tile, startTile, false)
                && checkMatePositionControl.canEscapeCheck()) {

            tags.add("+");
        }

        gameManager.addLastMove(tile, 1);

        if (wasKilled) {
            boardPanel.addRemovedPiecesToLSP();
        }

    }

    private String[] getSubTegs(String tag) {

        if (tag.contains("+")) {
            tag = tag.replace("+", " ").trim();
        }
        if (tag.contains("#")) {
            tag = tag.replace("#", " ").trim();
        }
        if (tag.contains("1/2-1/2")) {
            tag = tag.replace("1/2-1/2", " ").trim();
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
            setCurrentTile(start[0]);
        }
        if (whiteIsActive && itemsSubLine.length == 3) {
            setCurrentTile(start[1]);
        }
        if (!whiteIsActive && itemsSubLine.length == 2) {
            setCurrentTile(start[2]);
        }
        if (!whiteIsActive && itemsSubLine.length == 3) {
            setCurrentTile(start[3]);
        }
        currentPiece = tile.getCurrentPiece();
        if (whiteIsActive && itemsSubLine.length == 2) {
            setCurrentTile(finish[0]);
        }
        if (whiteIsActive && itemsSubLine.length == 3) {
            setCurrentTile(finish[1]);
        }
        if (!whiteIsActive && itemsSubLine.length == 2) {
            setCurrentTile(finish[2]);
        }
        if (!whiteIsActive && itemsSubLine.length == 3) {
            setCurrentTile(finish[3]);
        }
        gameManager.getCheckMatePositionControl().setWhiteIsActive(whiteIsActive);
        gameManager.move(currentPiece, tile, tags, lastRemoved);

        if (straight == false) {
            currentPiece.setWasMoved(false);
            tags.add("R");
        }

        gameManager.doCastling(tile, tags, lastRemoved);
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
     * @return textArea
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * Setter for gameView
     * @param gameView
     */
    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    /**
     * Ends game
     * @param winner final game score
     * @param byTime true if game was played by time
     */
    public void endGame(String win, boolean byTime) {
        this.winner = win;
        int n;
        if (winner.equals("1-0")) {
            n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "White wins by checkmate! Save the game? \n"
                    + "Choosing \"No\" quits the game.",
                    "White wins!", JOptionPane.YES_NO_OPTION);

        } else {

            n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "Black wins by checkmate! Save the game? \n"
                    + "Choosing \"No\" quits the game.",
                    "Black wins!", JOptionPane.YES_NO_OPTION);

        }
        if (n == JOptionPane.YES_OPTION) {
            saveGame(winner);
        } else {
            gameWindowFrame.dispose();
        }
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

    private boolean trimTagsEnd() {

        boolean b = false;
        //String tag = tagArr[0];
        if (trimedTag.endsWith("R")) {
            trimedTag = trimedTag.replace("R", " ").trim();
            x = 1;
            s = "R";
            b = true;
        }
        if (trimedTag.endsWith("N")) {
            trimedTag = trimedTag.replace("N", " ").trim();
            x = 3;
            s = "N";
            b = true;
        }
        if (trimedTag.endsWith("B")) {
            trimedTag = trimedTag.replace("B", " ").trim();
            x = 2;
            s = "B";
            b = true;
        }
        if (trimedTag.endsWith("Q")) {
            trimedTag = trimedTag.replace("Q", " ").trim();
            x = 0;
            s = "Q";
            b = true;

        }
        return b;
    }
}
