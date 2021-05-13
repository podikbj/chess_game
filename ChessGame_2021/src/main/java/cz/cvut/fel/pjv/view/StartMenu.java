package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Players;
//import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
//import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author kira
 */
public class StartMenu implements Runnable {

    /**
     *
     */
    public static boolean isManual = false;
    private GameStateEnum gameState = GameStateEnum.LOAD_GAME;
    private static Logger logger = Logger.getLogger(StartMenu.class.getName());

    public void run() {

        final JFrame startWindow = new JFrame("Chess");
        startWindow.setLocation(300, 100);
        startWindow.setResizable(false);
        startWindow.setSize(new Dimension(300, 400));

        Box components = Box.createVerticalBox();
        startWindow.add(components);

        Box gameAttributesBox = Box.createVerticalBox();
        components.add(gameAttributesBox);

        final JPanel titlePanel = new JPanel();

        final JPanel cupNamePanel = new JPanel();
        final JTextField cupName = new JTextField("World Championship Match", 25);
        cupNamePanel.add(cupName);

        final JPanel cityPanel = new JPanel();
        final JTextField city = new JTextField("Moscow", 25);
        cityPanel.add(city);

        final JPanel countryPanel = new JPanel();
        final JTextField country = new JTextField("Russia", 25);
        countryPanel.add(country);

        final JPanel roundPanel = new JPanel();
        final JLabel rLabel = new JLabel("Round");
        final JTextField round = new JTextField("32", 20);
        roundPanel.add(rLabel);
        roundPanel.add(round);

        gameAttributesBox.add(titlePanel);
        gameAttributesBox.add(cupNamePanel);
        gameAttributesBox.add(cityPanel);
        gameAttributesBox.add(countryPanel);
        gameAttributesBox.add(roundPanel);

        final JPanel blackPanel = new JPanel();
        components.add(blackPanel);
        final JLabel bLabel = new JLabel("Black");
        final JTextField blackFirstName = new JTextField("Anatoly", 10);
        final JTextField blackLastName = new JTextField("Karpov", 10);
        blackPanel.add(bLabel);
        blackPanel.add(blackFirstName);
        blackPanel.add(blackLastName);

        final JPanel whitePanel = new JPanel();
        components.add(whitePanel);
        final JLabel wLabel = new JLabel("White");
        final JTextField whiteFirstName = new JTextField("Garry", 10);
        final JTextField whiteLastName = new JTextField("Kasparov", 10);
        whitePanel.add(wLabel);
        whitePanel.add(whiteFirstName);
        whitePanel.add(whiteLastName);

        final String[] minSecInts = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minSecInts[i] = "0" + Integer.toString(i);
            } else {
                minSecInts[i] = Integer.toString(i);
            }
        }

        final JComboBox<String> seconds = new JComboBox<String>(minSecInts);
        final JComboBox<String> minutes = new JComboBox<String>(minSecInts);
        final JComboBox<String> hours
                = new JComboBox<String>(new String[]{"0", "1", "2"});

        Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(minutes.getPreferredSize());

        timerSettings.add(hours);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(minutes);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(seconds);

        timerSettings.add(Box.createVerticalGlue());

        components.add(timerSettings);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 5, 0));
        final JButton start = new JButton("Start");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String wpName = whiteFirstName.getText();
                String wpLastName = whiteLastName.getText();
                String bpName = blackFirstName.getText();
                String bpLastName = blackLastName.getText();
                String date = new SimpleDateFormat("YYYY.MM.dd").format(new Date());

                Players players = Players.getInstance(wpName, wpLastName, bpName, bpLastName,
                        date, cupName.getText(), city.getText(), country.getText(), round.getText());

                int hh = Integer.parseInt((String) hours.getSelectedItem());
                int mm = Integer.parseInt((String) minutes.getSelectedItem());
                int ss = Integer.parseInt((String) seconds.getSelectedItem());
                GameWindowBasic gwb = new GameWindowBasic(hh, mm, ss, gameState);
                BoardPanel bp = new BoardPanel(gwb, gameState);
                gwb.createGameModes(bp);
                startWindow.dispose();
            }
        });

        final JButton help = new JButton("Help");

        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(startWindow,
                        "To begin a new game, input player names\n"
                        + "next to the pieces. Set the clocks and\n"
                        + "click \"Start\". Setting the timer to all\n"
                        + "zeroes begins a new untimed game.",
                        "How to play",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        startWindow,
                        "Are you sure you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    startWindow.dispose();
                }
            }
        });

        buttons.add(start);
        buttons.add(help);
        buttons.add(quit);

        JPanel gameStatePanel = new JPanel();

        String[] gs = {"Play game mode", "View game mode",
            "Play game after loading mode", "Play game with PC mode",
            "Initial manual setting game mode"};
        final JComboBox comboBox = new JComboBox(gs);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                String strMode = (String) box.getSelectedItem();
                if (strMode.equals("View game mode")) {
                    gameState = GameStateEnum.LOAD_VIEW;
                }
                if (strMode.equals("Play game after loading mode")) {
                    gameState = GameStateEnum.LOAD_GAME;
                }
                if (strMode.equals("Play game with PC mode")) {
                    gameState = GameStateEnum.AI_MODEL;
                }
                if (strMode.equals("Play game mode")) {
                    gameState = GameStateEnum.GENERAL;
                }
                if (strMode.equals("Initial manual setting game mode")) {
                    isManual = true;
                    gameState = GameStateEnum.INITIAL_MANUAL_SETTING;
                }
            }
        });

        gameStatePanel.add(comboBox);

        components.add(gameStatePanel);
        components.add(buttons);

        Component space = Box.createGlue();
        components.add(space);

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);

    }

}
