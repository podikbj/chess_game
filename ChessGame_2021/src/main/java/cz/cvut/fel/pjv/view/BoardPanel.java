package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.CheckmateDetector;
import cz.cvut.fel.pjv.start.GameManager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
//public final class BoardPanel extends JPanel {

    private final TileComponent[][] boardTileComponents;
    private GameWindow g;
    private GameManager gameManager = GameManager.getInstance();

    public BoardPanel(GameWindow g) {

        this.boardTileComponents = new TileComponent[8][8];

        setLayout(new GridLayout(8, 8, 0, 0));

         LinkedList<Tile> tileList = gameManager.getTileList();
        int x = 0;
        int y = 0;
        
        for (Tile t : tileList) {
            x = t.getX();
            y = t.getY();
            boardTileComponents[x][y] = new TileComponent(this, t);
            this.add(boardTileComponents[x][y]);
        }

        
        //validate();
        //  whiteTurn = true;
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
    }

    @Override
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                TileComponent tc = boardTileComponents[x][y];
                tc.paintComponent(g);
            }
        }

    }
//    public BoardPanel(Tile[][] tileArray) {
//        this.tileArray = tileArray;
//    }
//
//    public Tile[][] getTileArray() {
//        return tileArray;
//    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
