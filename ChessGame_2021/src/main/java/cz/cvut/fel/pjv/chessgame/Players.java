package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.Date;

public class Players {

    private static Players instance = null;

    private String wpName;
    private String wpLastName;
    private String bpName;
    private String bpLastName;
    private String date;

    public static Players getInstance(String wpName, String wpLastName, String bpName, String bpLastName, String date) {
        if (instance == null) {
            instance = new Players(wpName, wpLastName, bpName, bpLastName, date);
        }
        return instance;
    }

    public static Players getInstance() {
        return instance;
    }

    private Players(String wpName, String wpLastName, String bpName, String bpLastName, String date) {
        this.wpName = wpName;
        this.wpLastName = wpLastName;
        this.bpName = bpName;
        this.bpLastName = bpLastName;
        this.date = date;
    }

    public String getWpName() {
        return wpName;
    }

    public String getWpLastName() {
        return wpLastName;
    }

    public String getBpName() {
        return bpName;
    }

    public String getBpLastName() {
        return bpLastName;
    }

    public String getDate() {
        return date;
    }

}
