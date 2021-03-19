package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class TileComponent extends JComponent {

    private BoardPanel boardPanel;
    private Tile currentTile;
    private int xC = 0;
    private int yC = 0;
    private int color;
    private boolean displayPiece;

    public TileComponent(BoardPanel boardPanel, Tile currentTile) {

        this.boardPanel = boardPanel;
        this.currentTile = currentTile;
        this.yC = currentTile.getX();
        this.xC = currentTile.getY();
        this.color = currentTile.getColor();
        this.displayPiece = true;
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (this.color == 1) {
            g.setColor(new Color(221, 192, 127));
        } else {
            g.setColor(new Color(101, 67, 33));
        }

        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        if (!currentTile.getIsEmpty() && displayPiece) {

            BufferedImage pieceImage = null;
            String imageFilePath = currentTile.getCurrentPiece().getPath();
            try {
                if (pieceImage == null) {
                    pieceImage = ImageIO.read(new File(imageFilePath));
                }
            } catch (IOException e) {
                System.out.println("File not found: " + e.getMessage());
            }

            g.drawImage(pieceImage, xC, yC, null);
        }
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public boolean isDisplayPiece() {
        return displayPiece;
    }

    public void setDisplayPiece(boolean displayPiece) {
        this.displayPiece = displayPiece;
    }
    
    

}
