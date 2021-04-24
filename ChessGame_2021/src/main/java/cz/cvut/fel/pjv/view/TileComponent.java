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

@SuppressWarnings("serial")
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
        this.yC = currentTile.getY();
        this.xC = currentTile.getX();
        this.color = currentTile.getColor();
        this.displayPiece = true;
        if (yC == 0 && xC > 3) {
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.YELLOW));
        }
        if (yC == 7 && xC > 3) {
            this.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.YELLOW));
        }
        if (xC == 4) {
            this.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.YELLOW));
        }
        if (xC == 4 && yC == 7) {
            this.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 0, Color.YELLOW));
        }
        if (xC == 4 && yC == 0) {
            this.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 0, Color.YELLOW));
        }
        if (xC == 11) {
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.YELLOW));
        }
        if (xC == 11 && yC == 7) {
            this.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 2, Color.YELLOW));
        }
        if (xC == 11 && yC == 0) {
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.YELLOW));
        }
    }

    public void paintComponent(Graphics g) {

        if (this.xC > 3) {
            if (this.color == 1) {
                g.setColor(new Color(221, 192, 127));
            } else {
                g.setColor(new Color(101, 67, 33));
            }
        } else {
            g.setColor(new Color(200, 200, 200));
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

            if (currentTile.getCurrentPiece().getColor() == 1) {
                g.drawImage(pieceImage, xC - 8, yC - 2, null);
            }
            if (currentTile.getCurrentPiece().getColor() == 0) {
                g.drawImage(pieceImage, xC - 8, yC - 6, null);
            }

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

//    public void setxC(int xC) {
//        this.xC = xC;
//    }
//
//    public void setyC(int yC) {
//        this.yC = yC;
//    }
}
