package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.Date;

/**
 * Represents chess players
 *
 * @author kira
 */
public class Players {

    private static Players instance = null;

    private String date;
    private String cupName;
    private String country;
    private String city;
    private String round;
    private String wpName;
    private String wpLastName;
    private String bpName;
    private String bpLastName;

    /**
     * Returns single instance of Players
     *
     * @param wpName white player first name
     * @param wpLastName white player last name
     * @param bpName black player first name
     * @param bpLastName black player last name
     * @param date game date
     * @param cupName cup's name
     * @param city city where game is going
     * @param country country where game is going
     * @param round game round
     * @return single instance of Players
     */
    public static Players getInstance(String wpName, String wpLastName, String bpName, String bpLastName,
            String date, String cupName, String city, String country, String round) {
        if (instance == null) {
            instance = new Players(wpName, wpLastName, bpName, bpLastName, date,
                    cupName, city, country, round);
        }
        return instance;
    }

    /**
     *
     * @return single instance of Players
     */
    public static Players getInstance() {
        return instance;
    }

    private Players(String wpName, String wpLastName, String bpName, String bpLastName,
            String date, String cupName, String city, String country, String round) {
        this.wpName = wpName;
        this.wpLastName = wpLastName;
        this.bpName = bpName;
        this.bpLastName = bpLastName;
        this.date = date;
        this.city = city;
        this.country = country;
        this.round = round;
        this.cupName = cupName;
    }

    /**
     *
     * @return white player first name as a string
     */
    public String getWpName() {
        return wpName;
    }

    /**
     *
     * @return white player last name as a string
     */
    public String getWpLastName() {
        return wpLastName;
    }

    /**
     *
     * @return black player first name as a string
     */
    public String getBpName() {
        return bpName;
    }

    /**
     *
     * @return black player last name as a string
     */
    public String getBpLastName() {
        return bpLastName;
    }

    /**
     *
     * @return game date as a string
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @return cup's name as a string
     */
    public String getCupName() {
        return cupName;
    }

    /**
     *
     * @return country as a string
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @return city as a string
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @return game round as a string
     */
    public String getRound() {
        return round;
    }

}
