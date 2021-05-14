package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.CheckMatePositionControl;
import cz.cvut.fel.pjv.start.GameManager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author kira
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {

    private final TileComponent[][] boardTileComponents;
    private TileComponent currentTileComponent;
    private Piece currentPiece;
    final private GameWindowBasic gameWindowBasic;

    private int computer = -1;
    final private GameManager gameManager = GameManager.getInstance();
    final private CheckMatePositionControl checkMatePositionControl;
    final private LinkedList<Tile> tileList;
    private LinkedList<Piece> lastRemoved = new LinkedList<>();
    private List<Piece> gmRemovedPieces;
    private GameStateEnum gameState = null;
    private String winner = "";

    private int xB = 0;
    private int yB = 0;
    private boolean whiteIsActive = true;

    private int counter = 0;
    private HashSet<String> tags = new HashSet<String>();

    /**
     *
     * @param gameWindow
     * @param gameState
     */
    public BoardPanel(GameWindowBasic gameWindow, GameStateEnum gameState) {

        this.boardTileComponents = new TileComponent[8][12];
        this.gameWindowBasic = gameWindow;
        this.gameState = gameState;
        setLayout(new GridLayout(8, 12));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.tileList = gameManager.getTileList();

        checkMatePositionControl = gameManager.getCheckMatePositionControl();

        for (Tile t : tileList) {
            xB = t.getX();
            yB = t.getY();
            boardTileComponents[yB][xB] = new TileComponent(this, t);
        }

        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 12; j++) {
                this.add(boardTileComponents[i][j]);
            }
        }

        this.setPreferredSize(new Dimension(600, 400));
        this.setMaximumSize(new Dimension(600, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(600, 400));
    }

    @Override
    public void paintComponent(Graphics g) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 12; j++) {
                TileComponent tcomp = boardTileComponents[i][j];
                tcomp.paintComponent(g);
            }
        }

        if (currentPiece != null) {
            if ((currentPiece.getColor() == 1 && whiteIsActive)
                    || (currentPiece.getColor() == 0 && !whiteIsActive)) {
                final Image img = currentPiece.getPieceImage();
                g.drawImage(img, xB, yB, null);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (gameState == GameStateEnum.LOAD_VIEW) {
            JOptionPane.showMessageDialog(currentTileComponent, "Only view mode");
            return;
        }
        String gv = gameWindowBasic.getGameView();
        if (gameState == GameStateEnum.LOAD_GAME && gv.contains("#")) {
            JOptionPane.showMessageDialog(currentTileComponent, "Game is over");
            return;
        }

        xB = e.getX();
        yB = e.getY();

        currentTileComponent = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));

        Tile currentTile = currentTileComponent.getCurrentTile();
        currentPiece = null;

        if (!currentTile.getIsEmpty()) {

            currentPiece = currentTile.getCurrentPiece();

            //  if (StartMenu.isManual) {
            if (gameState == GameStateEnum.INITIAL_MANUAL_SETTING) {
                //currentTileComponent.setDisplayPiece(false);
                repaint();
                return;
            }

            if (currentPiece.getColor() == 0 && whiteIsActive) {
                return;
            }
            if (currentPiece.getColor() == 1 && !whiteIsActive) {
                return;
            }
            if (gameState == GameStateEnum.AI_MODEL) {
                if (gameWindowBasic.getComp().getComputer() == -1) {
                    JOptionPane.showMessageDialog(currentTileComponent, "Choose piece color for computer player");
                    return;
                }

                if (currentPiece.getColor() == 0 && gameWindowBasic.getComp().getComputer() == 0 && !whiteIsActive) {
                    return;
                }

                if (currentPiece.getColor() == 1 && gameWindowBasic.getComp().getComputer() == 1 && whiteIsActive) {
                    return;
                }
            }
            currentTileComponent.setDisplayPiece(false);
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (gameState == GameStateEnum.LOAD_VIEW) {
            return;
        }

        TileComponent startTileComponent = currentTileComponent;
        Tile startTile = startTileComponent.getCurrentTile();

        currentTileComponent = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        Tile currentTile = currentTileComponent.getCurrentTile();

        if (currentPiece != null) {

            if (StartMenu.isManual) {
                if (currentTile.getIsEmpty()) {
                    gameManager.move(currentPiece, currentTile, tags, lastRemoved);
                    currentPiece.setWasMoved(false);
                    currentPiece.setWasRemoved(false);

                    gameManager.addLastMove(startTile, 0);
                    gameManager.addLastMove(currentTile, 1);
                }
                repaint();
                return;
            }

            if (currentPiece.getColor() == 0 && whiteIsActive) {
                return;
            }
            if (currentPiece.getColor() == 1 && !whiteIsActive) {
                return;
            }
            checkMatePositionControl.setWhiteIsActive(whiteIsActive);
            int castling = -1;
            if (currentPiece.toString().equals("R") && !currentPiece.isWasMoved()
                    && (currentTile.getX() == 9 || currentTile.getX() == 7)) {
                castling = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to provide castling?", "Confirmation castling",
                        JOptionPane.OK_CANCEL_OPTION);
                System.out.println("castling :" + castling);
            }

            if (gameManager.isMoveAllowed(currentPiece, currentTile, startTile, castling)) {

                currentTileComponent.setDisplayPiece(true);

                gameManager.move(currentPiece, currentTile, tags, lastRemoved);
                gameManager.addLastMove(startTile, 0);
                gameManager.addLastMove(currentTile, 1);

                if (checkMatePositionControl.doesMoveCauseCheck(currentPiece, currentTile, startTile, false)) {
                    if (checkMatePositionControl.canEscapeCheck()) {
                        tags.add("+");
                    } else {
                        if (whiteIsActive) {
                            winner = "1 - 0";
                        } else {
                            winner = "0 - 1";
                        }
                        tags.add("#");
                        setGameView(currentPiece, currentTile, startTile);
                        currentPiece = null;
                        repaint();
                        this.removeMouseListener(this);
                        this.removeMouseMotionListener(this);
                        gameWindowBasic.endGame(winner, false);
                        return;
                        //repaint();
                    }

                }

                //if a pawn get oppisite border you are allowed to convert it to any piece exept king
                if (currentPiece.toString().equals("P")) {
                    if ((currentPiece.getColor() == 0 && currentTile.getY() == 0)
                            || (currentPiece.getColor() == 1 && currentTile.getY() == 7)) {
                        String[] options = {"Queen", "Rook", "Bishop", "Knight", "Cancel"};
                        int x = JOptionPane.showOptionDialog(
                                null,
                                "Click a button to convert this pawn to another piece", "Click a button",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                        //gameManager.changePawn(currentPiece, currentTile, whiteIsActive, x, tags);
                        gameManager.changePawn(currentTile, whiteIsActive, x, tags);

                    }

                }

                if (castling == JOptionPane.YES_OPTION) {
                    doCastling(currentTile);
                }

                Tile tileRemovedPiece = addRemovedPiecesToLSP();
                if (tileRemovedPiece != null) {
                    repaintTile(tileRemovedPiece);
                }

                setGameView(currentPiece, currentTile, startTile);
                whiteIsActive = !whiteIsActive;

            } else {

                startTileComponent.setDisplayPiece(false);
                currentPiece = null;

            }
        }

        repaint();

    }

    private void setGameView(Piece currentPiece, Tile currentTile, Tile startTile) {

        counter++;

        StringBuilder sb = new StringBuilder();
        sb.append(gameWindowBasic.getGameView());

        if (whiteIsActive) {
            sb.append(String.valueOf((counter + 1) / 2)).append(". ");
        }

        if (tags.contains("O-O-O")) {
            sb.append("O-O-O");
        } else if (tags.contains("O-O")) {
            sb.append("O-O");
        } else {

            if (currentPiece.toString() != "P") {
                sb.append(currentPiece.toString());
            }
            sb.append(startTile.coordinatesString());
            if (tags.contains("x")) {
                sb.append("x");
            } else {
                sb.append("-");
            }

            if (tags.contains("x")) {
                // removedPieces = gameManager.getRemovedPieces();
                if (lastRemoved == null && lastRemoved.size() == 0) {
                    return;
                }
                String strP = lastRemoved.get(lastRemoved.size() - 1).toString();
                if (!strP.equals("P")) {
                    sb.append(strP);
                }
            }

            sb.append(currentTile.coordinatesString());

            if (tags.contains("Q") || tags.contains("R") || tags.contains("B") || tags.contains("N")) {
                sb.append("Q");
            } else if (tags.contains("R")) {
                sb.append("R");
            } else if (tags.contains("B")) {
                sb.append("B");
            } else if (tags.contains("N")) {
                sb.append("N");
            }

            if (tags.contains("#")) {
                sb.append("#");
            } else if (tags.contains("+")) {
                sb.append("+");
            }
        }

        sb.append(" ");
        if (counter % 4 == 0) {
            sb.append("\n");
        }

        currentPiece = null;
        tags.clear();

        gameWindowBasic.setGameView(sb.toString());
        switch (gameWindowBasic.getGameState()) {
            case GENERAL:
                gameWindowBasic.getAuto().getTextArea().setText(gameWindowBasic.getGameView());
                break;
            case LOAD_GAME:
            case LOAD_VIEW:
                gameWindowBasic.getView().getTextArea().setText(gameWindowBasic.getGameView());
                break;
            case AI_MODEL:
                gameWindowBasic.getComp().getTextArea().setText(gameWindowBasic.getGameView());
                break;
        }
    }

    /**
     *
     * @return
     */
    public Tile addRemovedPiecesToLSP() {
        //int color = (whiteIsActive) ? 0 : 1;
        gmRemovedPieces = gameManager.getRemovedPieces();

        Piece remP = null;
        Tile t = null;
        if (gmRemovedPieces.size() == 0) {
            return t;
        }

        for (Piece p : gmRemovedPieces) {
            if (p.getCurrentTile() == null) {
                remP = p;
                break;
            }
        }

        t = gameManager.getTileList().stream()
                .filter(p -> p.getX() < 4)
                .filter(p -> p.getIsEmpty())
                .findFirst().get();
        if (remP != null && t != null) {
            remP.setCurrentTile(t);
            t.setCurrentPiece(remP);

        }

        return t;

    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {
        TileComponent tc = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e
    ) {
        TileComponent tc = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e
    ) {
        xB = e.getX() - 24;
        yB = e.getY() - 24;

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e
    ) {
        TileComponent tc = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCastling(Tile currentTile) {
        //System.out.println("castling3 :" + castling);
        Tile finishKingTile = checkMatePositionControl.doCastling(currentTile, tags, lastRemoved);
        repaintTile(finishKingTile);
    }

    private void repaintTile(Tile tile) {
        TileComponent tc = boardTileComponents[tile.getY()][tile.getX()];
        tc.setDisplayPiece(true);
        tc.repaint();
    }

    /**
     *
     * @return
     */
    public boolean isWhiteIsActive() {
        return whiteIsActive;
    }

    /**
     *
     * @param whiteIsActive
     */
    public void setWhiteIsActive(boolean whiteIsActive) {
        this.whiteIsActive = whiteIsActive;
    }

    private Piece getCurrentPiece() {
        return currentPiece;
    }

    /**
     *
     * @return
     */
    public HashSet<String> getTags() {
        return tags;
    }

    //Computer's game
    /**
     *
     * @param computer
     */
    public void doTah(int computer) {

        if (computer == 1 && !whiteIsActive) {
            return;
        }
        if (computer == 0 && whiteIsActive) {
            return;
        }
        List<Piece> pieces = gameManager.getPieces(computer);
        List<Tile> tlist = null;
        Tile startTile = null;
        Tile finTile = null;

        Random random = new Random();
        int s, f, x;

        checkMatePositionControl.setWhiteIsActive(whiteIsActive);
        while (true) {
            s = 0;
            f = pieces.size() - 1;
            x = s + random.nextInt(f - s + 1);
            currentPiece = pieces.get(x);
            startTile = currentPiece.getCurrentTile();
            tlist = gameManager.getAllowedTiles(currentPiece);
            if (tlist != null && tlist.size() > 0) {
                break;
            }

        }

        s = 0;
        f = tlist.size() - 1;
        x = s + random.nextInt(f - s + 1);

        finTile = tlist.get(x);

        if (gameManager.isMoveAllowed(currentPiece, finTile, startTile, -1)) {
            gameManager.move(currentPiece, finTile, tags, lastRemoved);
            gameManager.addLastMove(startTile, 0);
            gameManager.addLastMove(finTile, 1);

            if (checkMatePositionControl.doesMoveCauseCheck(currentPiece, finTile, startTile, false)) {
                tags.add("+");
            }
        }
        setGameView(currentPiece, finTile, startTile);
        whiteIsActive = !whiteIsActive;
        this.repaint();
    }

//    public void setComputer(int computer) {
//        this.computer = computer;
//    }
    /**
     *
     * @param counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

}
