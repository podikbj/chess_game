package cz.cvut.fel.pjv.start;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MovesIterator {

    private static MovesIterator instance = null;
    private LinkedList<String> moves = new LinkedList<>();
    private int pos = -1;
    private int size = 0;

    private MovesIterator() {
        //  this.moves = moves;
    }

    public static MovesIterator getInstance() {
        if (instance == null) {
            instance = new MovesIterator();
        }
        return instance;
    }

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

    public boolean hasNext(int x) {
        if (x == 1) {
            return pos < moves.size() - 1 && pos >= 0;
        }
        if (x == -1) {
            return pos > 0 && pos <= moves.size() - 1;
        }
        return false;
    }

    public int getPos() {
        return pos;
    }

    public String getCurrentElement() {
        String str = null;
        str = moves.get(pos);
        return str;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void addMovement(String movement) {
        moves.add(movement);
        pos++;
    }

    public int getSize() {
        return moves.size();
    }

}
