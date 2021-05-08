package cz.cvut.fel.pjv.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartMenu implements Runnable {

    public static boolean isManual = false;
    private GameStateEnum gameState = GameStateEnum.LOAD_GAME;

    public void run() {

        final JFrame startWindow = new JFrame("Chess");
        startWindow.setLocation(300, 100);
        startWindow.setResizable(false);
        startWindow.setSize(260, 240);

        Box components = Box.createVerticalBox();
        startWindow.add(components);

        final JPanel titlePanel = new JPanel();
        components.add(titlePanel);
        final JLabel titleLabel = new JLabel("Chess");
        titlePanel.add(titleLabel);

        final JPanel blackPanel = new JPanel();
        components.add(blackPanel, BorderLayout.EAST);
        final JLabel blackPiece = new JLabel();
        final JTextField blackInput = new JTextField("Black", 10);
        blackPanel.add(blackInput);

        final JPanel whitePanel = new JPanel();
        components.add(whitePanel);
        final JLabel whitePiece = new JLabel();
        final JTextField whiteInput = new JTextField("White", 10);
        whitePanel.add(whiteInput);

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
                = new JComboBox<String>(new String[]{"0", "1", "2", "3"});

        Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(minutes.getPreferredSize());

        timerSettings.add(hours);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(seconds);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(minutes);

        timerSettings.add(Box.createVerticalGlue());

        components.add(timerSettings);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));
        final JButton start = new JButton("Start");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int hh = Integer.parseInt((String) hours.getSelectedItem());
                int mm = Integer.parseInt((String) minutes.getSelectedItem());
                int ss = Integer.parseInt((String) seconds.getSelectedItem());
                GameWindowBasic gwb = new GameWindowBasic(hh, mm, ss, gameState);
                BoardPanel bp = new BoardPanel(gwb, gameState);
                gwb.createGameModes(bp);
                //new GameForm();
                startWindow.dispose();
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
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(quit);

        JPanel gameStatePanel = new JPanel();
        gameStatePanel.setLayout(new GridLayout(1, 3, 10, 0));

        String[] gs = {"Play game mode", "View game mode",
            "Play game after loading mode", "Play game with PC mode"};
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
