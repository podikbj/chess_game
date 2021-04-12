package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.CheckMatePositionControl;
import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.start.Clock;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
//import java.time.Clock;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.*;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {

    private final TileComponent[][] boardTileComponents;
    //private final TileComponent[][] leftSideTileComponents;

    private TileComponent currentTileComponent;
    private Piece currentPiece;
    private GameWindow gameWindow;
    private GameManager gameManager = GameManager.getInstance();
    private CheckMatePositionControl checkMatePositionControl;

    private int xB = 0;
    private int yB = 0;
    private boolean whiteIsActive;

    private int counter = 0;

    public BoardPanel(GameWindow g) {

        //this.boardTileComponents = new TileComponent[8][8];
        this.boardTileComponents = new TileComponent[8][12];
        //this.leftSideTileComponents = new TileComponent[4][8];

        this.gameWindow = g;

        //setLayout(new GridLayout(8, 8, 0, 0));
        setLayout(new GridLayout(8, 12));

        //  this.leftSidePanel = new LeftSidePanel(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        LinkedList<Tile> tileList = gameManager.getTileList();
        //LinkedList<Tile> leftSideTileList = gameManager.getLeftSideTileList();

        gameManager.initializeCheckMatePositionControl();
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

        //validate();
        whiteIsActive = true;
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
//         this.tc = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
//          Tile currentTile = tc.getCurrentTile();
//          currentTile.removePiece();
        //   repaint();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xB = e.getX();
        yB = e.getY();
        boolean initialMove = false;

        currentTileComponent = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        Tile currentTile = currentTileComponent.getCurrentTile();
        currentPiece = null;

        if (!currentTile.getIsEmpty()) {

            currentPiece = currentTile.getCurrentPiece();

            if (StartMenu.isManual) {
                currentTileComponent.setDisplayPiece(false);
                repaint();
                return;
            }

            if (currentPiece.getColor() == 0 && whiteIsActive) {
                return;
            }
            if (currentPiece.getColor() == 1 && !whiteIsActive) {
                return;
            }
            currentTileComponent.setDisplayPiece(false);
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        TileComponent startTileComponent = currentTileComponent;
        Tile startTile = startTileComponent.getCurrentTile();

        currentTileComponent = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        Tile currentTile = currentTileComponent.getCurrentTile();

        if (currentPiece != null) {

            if (StartMenu.isManual) {
                if (currentTile.getIsEmpty()) {
                    gameManager.move(currentPiece, currentTile);
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

            int castling = -1;
            if (currentPiece.toString().equals("R") && !currentPiece.isWasMoved()) {
                castling = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to provide castling?", "Confirmation castling",
                        JOptionPane.DEFAULT_OPTION);

            }

            LinkedList<Tile> tileList = gameManager.getTileList();
            LinkedList<Piece> pieces = gameManager.getPieces(1);

            if (gameManager.isMoveAllowed(currentPiece, currentTile, startTile, whiteIsActive, castling)) {

                currentTileComponent.setDisplayPiece(true);

                gameManager.move(currentPiece, currentTile);

                checkMatePositionControl.isItCheck(currentPiece, whiteIsActive);

                //if a pawn get oppisite border you are allowed to convert it to any piece exept king
                if (currentPiece.toString().equals("P")) {
                    if ((currentPiece.getColor() == 0 && currentTile.getY() == 0)
                            || (currentPiece.getColor() == 1 && currentTile.getY() == 7)) {
                        String[] options = {"Queen", "Rook", "Bishop", "Knight", "Cancel"};
                        int x = JOptionPane.showOptionDialog(
                                null,
                                "Click a button to convert this pawn to another piece", "Click a button",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                        gameManager.changePawn(currentPiece, currentTile, whiteIsActive, x);

                    }

                }

                if (castling == JOptionPane.YES_OPTION) {
                    Tile finishKingTile = checkMatePositionControl.doCastling(currentTile, whiteIsActive);
                    TileComponent finishKingTileComponent = boardTileComponents[finishKingTile.getY()][finishKingTile.getX()];
                    finishKingTileComponent.setDisplayPiece(true);
                    finishKingTileComponent.repaint();

                }

                setGameView(currentPiece, currentTile, startTile, whiteIsActive);
                Tile tileRemovedPiece = addRemovedPiecesToLSP(whiteIsActive);
                if (tileRemovedPiece != null) {
                    TileComponent tileRemovedPieceComponent = boardTileComponents[tileRemovedPiece.getY()][tileRemovedPiece.getX()];
                    tileRemovedPieceComponent.setDisplayPiece(true);
                    tileRemovedPieceComponent.repaint();
                }

                whiteIsActive = !whiteIsActive;

            } else {

                startTileComponent.setDisplayPiece(true);
                currentPiece = null;

            }
        }

        repaint();

    }

    private void setGameView(Piece currentPiece, Tile currentTile, Tile startTile, boolean whiteIsActive) {

        StringBuffer sb = new StringBuffer();

        sb.append(gameWindow.getTextArea().getText());

        if (whiteIsActive) {
            counter++;
            sb.append(String.valueOf(counter));
            sb.append(". ");
        }

        sb.append(currentPiece.toString())
                .append(startTile.coordinatesString())
                .append(currentTile.coordinatesString())
                .append(" ");

        if (!whiteIsActive) {
            sb.append("\n");
        }

        gameWindow.setGameView(sb.toString());
        gameWindow.getTextArea().setText(gameWindow.getGameView());

    }

    private Tile addRemovedPiecesToLSP(boolean whiteIsActive) {
        int color = (whiteIsActive) ? 0 : 1;
        LinkedList<Piece> removedPieces = gameManager.getRemovedPieceses(color);
        Piece remP = null;
        Tile t = null;
        for (Piece p : removedPieces) {
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
        // updateLocation(e);
    }

    @Override
    public void mouseMoved(MouseEvent e
    ) {
        TileComponent tc = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateLocation(MouseEvent e) {
        //  tc.setLocation(x + e.getX(), y + e.getY());
        repaint();
    }

    public boolean isWhiteIsActive() {
        return whiteIsActive;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

}
