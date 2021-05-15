package cz.cvut.fel.pjv.start;

import java.awt.Color;

/**
 * Represents a chess clock
 * @author kira
 */
public class Clock {

    private int hh;
    private int mm;
    private int ss;
    private int color;

    /**
     * Constructor for Clock instance. 
     * @param hh hours
     * @param mm minutes
     * @param ss seconds
     * @param color clock color(white for white player)
     */
    public Clock(int hh, int mm, int ss, int color) {
        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
        this.color = color;
    }

    /**
     *
     * @return true if clock display 0
     */
    public boolean outOfTime() {
        return (hh == 0 && mm == 0 && ss == 0);
    }

    /**
     * Decrements time on 1 second
     */
    public void decrementClock() {
        if (this.mm == 0 && this.ss == 0) {
            this.ss = 59;
            this.mm = 59;
            this.hh--;
        } else if (this.ss == 0) {
            this.ss = 59;
            this.mm--;
        } else {
            this.ss--;
        }
    }

    /**
     *
     * @return current time as a string
     */
    public String getTime() {
        String fHrs = String.format("%02d", this.hh);
        String fMins = String.format("%02d", this.mm);
        String fSecs = String.format("%02d", this.ss);
        String fTime = fHrs + ":" + fMins + ":" + fSecs;
        return fTime;
    }

    /**
     *Sets the clock to zero
     */
    public void setNull() {
        this.hh = 0;
        this.mm = 0;
        this.ss = 0;
    }

    /**
     * Indicates of which player the clock belongs to
     * @return clock color
     */
    public int getColor() {
        return color;
    }
    
    
}
