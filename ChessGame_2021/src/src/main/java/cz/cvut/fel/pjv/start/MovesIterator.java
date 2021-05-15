package cz.cvut.fel.pjv.start;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Represents iterator for moves collection
 *
 * @author kira
 */
public class MovesIterator {

    private static MovesIterator instance = null;
    private LinkedList<String> moves = new LinkedList<>();
    private int pos = 0;

    /**
     * Constructor for Move Iterator.
     */
    private MovesIterator() {

        moves.add("");

    }

    /**
     *
     * @return single instance of Move Iterator
     */
    public static MovesIterator getInstance() {
        if (instance == null) {
            instance = new MovesIterator();
        }
        return instance;
    }

    /**
     * if next/previous element exits returns that element
     *
     * @param x incrementing or decrementing value of iterator position
     * @return next or previous collection's element
     */
    public String next(int x) {

        if (!hasNext(x)) {
            return null;
        }

        String move = "";

        if (x == 1) {
            pos += 1;
        }
        if (x == -1) {
            pos -= 1;
        }
        move = moves.get(pos);
        return move;
    }

    /**
     *
     * @param x incrementing or decrementing value of iterator position
     * @return true if next/previouse element exists
     */
    public boolean hasNext(int x) {
        if (x == 1) {
            return pos < moves.size() - 1 && pos >= 0;
        }
        if (x == -1) {
            return pos > 0 && pos <= moves.size() - 1;
        }
        return false;
    }

    /**
     *
     * @return element position in collection
     */
    public int getPos() {
        return pos;
    }

    /**
     *
     * @return current element
     */
    public String getCurrentElement() {
        String str = null;
        str = moves.get(pos);
        return str;
    }

    /**
     * Adds element to collection
     *
     * @param movement
     */
    public void addMovement(String movement) {
        moves.add(movement);
        pos++;
    }

    /**
     *
     * @return size of collection
     */
    public int getSize() {
        return moves.size();
    }

}
