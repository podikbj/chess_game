package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.view.GameStateEnum;
import cz.cvut.fel.pjv.view.StartMenu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.imageio.ImageIO;

/**
 * Represents a chess piece
 */
public abstract class Piece {

    protected final int color; // 1 - white; 0 - black
    protected Tile currentTile;
    protected String imageFilePath;
    private BufferedImage pieceImage;
    protected boolean wasMoved = false;
    protected boolean wasRemoved;
    protected GameManager gameManager = GameManager.getInstance();
    protected List<Tile> tileList = gameManager.getTileList();
    private List<Tile> tempList = null;
    private static Logger logger = Logger.getLogger(Piece.class.getName());

    /**
     * Constructor for Piece.
     *
     * @param color piece color
     * @param tile current tile
     * @param blackPiecePath path to black piece picture file
     * @param whitePiecePath path to white piece picture file
     * @param wasRemoved true if the piece was removed from the chess board
     */
    public Piece(int color, Tile tile, String blackPiecePath, String whitePiecePath, boolean wasRemoved) {
        this.color = color;
        this.currentTile = tile;
        this.wasRemoved = wasRemoved;
        imageFilePath = (this.color == 0) ? blackPiecePath : whitePiecePath;
        this.tileList = gameManager.getTileList();
        try {
            if (this.pieceImage == null) {
                this.pieceImage = ImageIO.read(new File(imageFilePath));
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "File not found", ex);
            ex.printStackTrace();

        }

    }

    /**
     * Checks if the piece is allowed to move to finish tile
     *
     * @param finTile current tag finish tile
     * @return true if move is allowed by chess rules
     */
    public abstract boolean isMoveAllowed(Tile finTile);

    /**
     * Finds intermediate tiles between current tile and finish tile
     *
     * @param finTile current tag finish tile
     * @return list of intermediate tiles
     */
    public abstract List<Tile> getIntermediateTiles(Tile finTile);

    /**
     * Returns string that includes unique instance name
     *
     * @return string that includes unique instance name
     */
    public abstract String toString();

    /**
     * Checks if the move is along diagonal tiles of the board
     *
     * @param finTile current tag finish tile
     * @return true if move is along diagonal tiles ot the board
     */
    public boolean isDiagonalMove(Tile finTile) {
        return (Math.abs(finTile.getX() - currentTile.getX()) == Math.abs(finTile.getY() - currentTile.getY()));
    }

    /**
     * Checks if the move is along vertical tiles of the board
     *
     * @param finTile current tag finish tile
     * @return true if move is along vertical tiles ot the board
     */
    public boolean isVerticalMove(Tile finTile) { // x const
        return (finTile.getX() == currentTile.getX() && finTile.getY() != currentTile.getY());
    }

    /**
     * Checks if the move is along horizontal tiles of the board
     *
     * @param finTile current tag finish tile
     * @return true if move is along horizontal tiles ot the board
     */
    public boolean isHorizontalMove(Tile finTile) { // x const

        return (finTile.getY() == currentTile.getY() && finTile.getX() != currentTile.getX());
    }

    /**
     * Finds any occupied tile in horizontal move
     *
     * @param finTile current tag finish tile
     * @return true if have found any occupied tile in horizontal move
     */
    protected boolean isAnyTileIsOccupiedHorizontalMove(Tile finTile) {

        boolean b = false;
        tempList = getListOfIntermediateHorizontalTiles(finTile);
        if (tempList != null) {
            b = tempList.stream().filter(p -> !p.getIsEmpty()).findAny().isPresent();
        }
        tempList = null;
        return b;
    }

    /**
     * Finds intermediate tiles between current tile and finish tile in
     * horizontal move
     *
     * @param finTile current tag finish tile
     * @return list of intermediate tiles
     */
    public List<Tile> getListOfIntermediateHorizontalTiles(Tile finTile) {
        tempList = tileList.stream()
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
                .collect(toList());

        return tempList;
    }

    /**
     * Find any occupied tile in vertical move
     *
     * @param finTile current tag finish tile
     * @return true if have found any occupied tile in vertical move
     */
    protected boolean isAnyTileIsOccupiedVerticalMove(Tile finTile) {

        boolean b = false;
        tempList = getListOfIntermediateVerticalTiles(finTile);
        if (tempList != null) {
            b = tempList.stream().filter(p -> !p.getIsEmpty()).findAny().isPresent();
        }
        tempList = null;
        return b;
    }

    /**
     * Finds intermediate tiles between current tile and finish tile in vertical
     * move
     *
     * @param finTile current tag finish tile
     * @return list of intermediate tiles
     */
    public List<Tile> getListOfIntermediateVerticalTiles(Tile finTile) {
        tempList = tileList.stream()
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
                .collect(toList());

        return tempList;
    }

    /**
     * Find any occupied tile in diagonal move
     *
     * @param finTile current tag finish tile
     * @return true if have found any occupied tile in diagonal move
     */
    protected boolean isAnyTileIsOccupiedDiagonalMove(Tile finTile) {

        boolean b = false;
        tempList = getListOfIntermediateDiagonalTiles(finTile);
        if (tempList != null) {
            b = tempList.stream().filter(p -> !p.getIsEmpty()).findAny().isPresent();
        }
        tempList = null;
        return b;
    }

    /**
     * Finds intermediate tiles between current tile and finish tile in diagonal
     * move
     *
     * @param finTile current tag finish tile
     * @return list of intermediate tiles
     */
    public List<Tile> getListOfIntermediateDiagonalTiles(Tile finTile) {

        tempList = tileList.stream()
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
                                && p.getY() > currentTile.getY()
                                && p.getX() > finTile.getX()
                                && p.getY() < finTile.getY());

                    }

                    if (finTile.getX() < currentTile.getX() && finTile.getY() < currentTile.getY()) {

                        xy = (p.getX() < currentTile.getX()
                                && p.getY() < currentTile.getY()
                                && p.getX() > finTile.getX()
                                && p.getY() > finTile.getY());

                    }

                    return xy;
                }
                )
                .
                collect(toList());
        return tempList;
    }

    /**
     * Move piece to finish tile. Marks captured piece as Removed. Adds captured piece to list
     *
     * @param finTile current tag finish tile
     * @param tags set of special string symbols like "+, x, 0-0, 0-0-0"
     * @param lastRemoved list of removed pieces
     * @return true always 
     */
     public boolean move(Tile finTile, HashSet<String> tags, LinkedList<Piece> lastRemoved) {

        if (StartMenu.gameState != GameStateEnum.INITIAL_MANUAL_SETTING) {
            wasMoved = true;
        }
        if (!finTile.getIsEmpty() && this.color != finTile.getCurrentPiece().color) {
            tags.add("x");
            Piece removedPiece = finTile.getCurrentPiece();
            removedPiece.setCurrentTile(null);
            removedPiece.wasRemoved = true;
            lastRemoved.addLast(removedPiece);
            finTile.removePiece();

            currentTile.removePiece();
            finTile.setCurrentPiece(this);
            this.currentTile = finTile;

            return true;
        }

        currentTile.removePiece();
        finTile.setCurrentPiece(this);
        this.currentTile = finTile;
        finTile.setIsEmpty(false);
        //addMoveEntry(finTile);
        return true;
    }

    /**
     * Checks if piece on finish tile is the same color or not
     *
     * @param finTile current tag finish tile
     * @return false if finish tile is empty. Othewise returns true if piece on
     * finish tile is the same color
     */
    protected boolean isTheSameColor(Tile finTile) {

        if (finTile.getIsEmpty()) {
            return false;
        }
        return (currentTile.getCurrentPiece().color == finTile.getCurrentPiece().color);

    }

    /**
     * Getter for current tile
     *
     * @return
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Setter for current tile
     *
     * @param tile
     */
    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
    }

    /**
     * Getter for piece color
     *
     * @return
     */
    public int getColor() {
        return color;
    }

    /**
     * Setter for path to piece picture file
     *
     * @param path
     */
    protected void setPath(String path) {
        this.imageFilePath = path;
    }

    /**
     * Getter for path to piece picture file
     *
     * @return
     */
    public String getPath() {
        return imageFilePath;
    }

    /**
     * Getter for piece picture
     *
     * @return
     */
    public BufferedImage getPieceImage() {
        return pieceImage;
    }

    /**
     * Getter for wasMoved
     *
     * @return true if peice was moved at least once
     */
    public boolean isWasMoved() {
        return wasMoved;
    }

    /**
     * Getter for wasRemoved
     *
     * @return true if piece was removed from the board
     */
    public boolean isWasRemoved() {
        return wasRemoved;
    }

    /**
     * Sets true if piece was moved at least once
     *
     * @param wasMoved
     */
    public void setWasMoved(boolean wasMoved) {
        this.wasMoved = wasMoved;
    }

    /**
     * Sets true if piece was removed from the board
     *
     * @param wasRemoved
     */
    public void setWasRemoved(boolean wasRemoved) {
        this.wasRemoved = wasRemoved;
    }

}
