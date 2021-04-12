package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.view.StartMenu;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.imageio.ImageIO;

public abstract class Piece {

    protected final int color; // 1 - white; 0 - black
    protected Tile currentTile;
    protected String imageFilePath;
    private BufferedImage pieceImage;
    protected boolean wasMoved = false;
    //private GameManager gameManager = GameManager.getInstance();
    //private GameManager gameManager;

    public Piece(int color, Tile tile, String blackPiecePath, String whitePiecePath) {
        this.color = color;
        this.currentTile = tile;
        imageFilePath = (this.color == 0) ? blackPiecePath : whitePiecePath;
        try {
            if (this.pieceImage == null) {
                this.pieceImage = ImageIO.read(new File(imageFilePath));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }

    }

    public abstract boolean isMoveAllowed(Tile finTile);

    public boolean isDiagonalMove(Tile finTile) {
        return (Math.abs(finTile.getX() - currentTile.getX()) == Math.abs(finTile.getY() - currentTile.getY()));
    }

    public boolean isVerticalMove(Tile finTile) { // x const
        return (finTile.getX() == currentTile.getX() && finTile.getY() != currentTile.getY());
    }

    public boolean isHorizontalMove(Tile finTile) { // x const

        return (finTile.getY() == currentTile.getY() && finTile.getX() != currentTile.getX());
    }

    protected boolean isAnyTileIsOccupiedHorizontalMove(Tile finTile) {

        boolean b = false;
        GameManager gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();
        b = tileList.stream()
                .filter(p -> p.getY() == currentTile.getY())
                .filter((p)
                        -> {
                    boolean x = false;
                    if (currentTile.getX() > finTile.getX()) {
                        x = (p.getX() > finTile.getX() && p.getX() < currentTile.getX());
                    }
                    if (currentTile.getX() < finTile.getX()) {
                        x = (p.getX() < finTile.getX() && p.getX() > currentTile.getX());
                    }
                    return x;
                }
                )
                .filter(p -> !p.getIsEmpty())
                .findAny().isPresent();

        return b;
    }

    protected boolean isAnyTileIsOccupiedVerticalMove(Tile finTile) {

        boolean b = false;
        GameManager gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();
        b = tileList.stream()
                .filter(p -> p.getX() == currentTile.getX())
                .filter((p)
                        -> {
                    boolean y = false;
                    if (currentTile.getY() > finTile.getY()) {
                        y = (p.getY() > finTile.getY() && p.getY() < currentTile.getY());
                    }
                    if (currentTile.getY() < finTile.getY()) {
                        y = (p.getY() < finTile.getY() && p.getY() > currentTile.getY());
                    }
                    return y;
                }
                )
                .filter(p -> !p.getIsEmpty())
                .findAny().isPresent();
        return b;
    }

    protected boolean isAnyTileIsOccupiedDiagonalMove(Tile finTile) {

        boolean b = false;
        GameManager gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();
        b = tileList.stream()
                .filter(p -> Math.abs(p.getX() - currentTile.getX()) == Math.abs(p.getY() - currentTile.getY()))
                .filter((p)
                        -> {
                    boolean xy = false;
                    if (finTile.getX() > currentTile.getX() && finTile.getY() > currentTile.getY()) {

                        xy = (p.getX() > currentTile.getX()
                                && p.getY() > currentTile.getY()
                                && p.getX() < finTile.getX()
                                && p.getY() < finTile.getY());
                    }

                    if (finTile.getX() > currentTile.getX() && finTile.getY() < currentTile.getY()) {

                        xy = (p.getX() > currentTile.getX()
                                && p.getY() < currentTile.getY()
                                && p.getX() < finTile.getX()
                                && p.getY() > finTile.getY());

                    }

                    if (finTile.getX() < currentTile.getX() && finTile.getY() > currentTile.getY()) {

                        xy = (p.getX() < currentTile.getX()
                                && p.getY() < currentTile.getY()
                                && p.getX() > finTile.getX()
                                && p.getY() > finTile.getY());

                    }

                    if (finTile.getX() < currentTile.getX() && finTile.getY() < currentTile.getY()) {

                        xy = (p.getX() < currentTile.getX()
                                && p.getY() > currentTile.getY()
                                && p.getX() > finTile.getX()
                                && p.getY() < finTile.getY());

                    }

                    return xy;
                }
                )
                .
                filter(p -> !p.getIsEmpty())
                .findAny().isPresent();
        return b;
    }

    private void addMoveEntry(Tile finTile) {
        GameManager gameManager = GameManager.getInstance();
        gameManager.getMoveSequence().add(currentTile.coordinatesString() + "-" + finTile.coordinatesString());
    }

    public boolean removePieceFromArray(Tile finTile) {

        if (!StartMenu.isManual && finTile.getIsEmpty()) {
            return false;
        }
        LinkedList<Piece> pieces;
        LinkedList<Piece> removedPieces;
        Piece removedPiece = finTile.getCurrentPiece();
        GameManager gameManager = GameManager.getInstance();

        pieces = gameManager.getPieces(removedPiece.color);
        removedPieces = gameManager.getRemovedPieceses(removedPiece.color);

        if (!StartMenu.isManual) {
            if (pieces.contains(removedPiece)) {
                pieces.remove(removedPiece);
                removedPieces.add(removedPiece);
                return true;
            }
        } else {
            if (removedPieces.contains(removedPiece)) {
                removedPieces.remove(removedPiece);
                pieces.add(removedPiece);
                return true;
            }

        }
        return false;
    }

    public boolean move(Tile finTile) {

        if (!StartMenu.isManual) {
            wasMoved = true;
        }
        if (!finTile.getIsEmpty() && this.color != finTile.getCurrentPiece().color) {
            Piece removedPiece = finTile.getCurrentPiece();
            removedPiece.setCurrentTile(null);
            removePieceFromArray(finTile);
            finTile.removePiece();

            currentTile.removePiece();
            finTile.setCurrentPiece(this);
            this.currentTile = finTile;
            addMoveEntry(finTile);

            return true;
        }

        currentTile.removePiece();
        finTile.setCurrentPiece(this);
        this.currentTile = finTile;
        finTile.setIsEmpty(false);
        addMoveEntry(finTile);
        return true;
    }

    protected boolean isTheSameColor(Tile finTile) {
        if (finTile.getIsEmpty()) {
            return false;
        }
        return (currentTile.getCurrentPiece().color == finTile.getCurrentPiece().color);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
    }

    public int getColor() {
        return color;
    }

    public void setPath(String path) {
        this.imageFilePath = path;
    }

    public String getPath() {
        return imageFilePath;
    }

    public BufferedImage getPieceImage() {
        return pieceImage;
    }

    public boolean isWasMoved() {
        return wasMoved;
    }

}
