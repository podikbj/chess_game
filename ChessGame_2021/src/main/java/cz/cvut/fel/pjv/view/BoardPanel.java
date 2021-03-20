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
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.*;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
//public final class BoardPanel extends JPanel {

    private final TileComponent[][] boardTileComponents;
    private TileComponent currentTileComponent;
    //private Tile currentTile;
    private Piece currentPiece;
    private GameWindow gameWindow;
    private GameManager gameManager = GameManager.getInstance();
    private CheckMatePositionControl checkMatePositionControl;

    private int xB = 0;
    private int yB = 0;
    private boolean whiteIsActive;

    public BoardPanel(GameWindow g) {

        this.boardTileComponents = new TileComponent[8][8];

        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        LinkedList<Tile> tileList = gameManager.getTileList();

        gameManager.initializeCheckMatePositionControl();
        checkMatePositionControl = gameManager.getCheckMatePositionControl();

        checkMatePositionControl = gameManager.getCheckMatePositionControl();
//        int x = 0;
//        int y = 0;


        for (Tile t : tileList) {
            xB = t.getX();
            yB = t.getY();
            boardTileComponents[xB][yB] = new TileComponent(this, t);
            this.add(boardTileComponents[xB][yB]);
        }

        //validate();
        whiteIsActive = true;
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
    }

    @Override
    public void paintComponent(Graphics g) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
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

        currentTileComponent = (TileComponent) this.getComponentAt(new Point(e.getX(), e.getY()));
        Tile currentTile = currentTileComponent.getCurrentTile();
        currentPiece = currentTile.getCurrentPiece();
        //System.out.println(currentPiece);
        if (!currentTile.getIsEmpty()) {
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
                    TileComponent finishKingTileComponent = boardTileComponents[finishKingTile.getX()][finishKingTile.getY()];
                    finishKingTileComponent.setDisplayPiece(true);
                    finishKingTileComponent.repaint();
           
                }

                whiteIsActive = !whiteIsActive;
        
 
        repaint();

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
}
