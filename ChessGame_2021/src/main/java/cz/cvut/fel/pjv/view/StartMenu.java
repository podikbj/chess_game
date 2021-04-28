package cz.cvut.fel.pjv.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StartMenu implements Runnable {

    public static boolean isManual = false;
    private GameStateEnum mode = GameStateEnum.GENERAL;

    public void run() {
        final JFrame startWindow = new JFrame("Chess");

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));
        final JButton start = new JButton("Start");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameWindowBasic gwb = new GameWindowBasic();
                BoardPanel bp = new BoardPanel(gwb, mode);
                gwb.createGameModes(bp, mode);
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
        buttons.add(quit);
        startWindow.add(buttons, BorderLayout.SOUTH);

        JPanel gameState = new JPanel();
        gameState.setLayout(new GridLayout(1, 3, 10, 0));

        String[] gs = {GameStateEnum.GENERAL.name(), GameStateEnum.UPLOAD.name(),
            GameStateEnum.IIMODEL.name()};
        final JComboBox comboBox = new JComboBox(gs);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                String strMode = (String) box.getSelectedItem();
                if (strMode.equals("UPLOAD")) {
                    mode = GameStateEnum.UPLOAD;
                }
                if (strMode.equals("IIMODEL")) {
                    mode = GameStateEnum.IIMODEL;
                }
                if (strMode.equals("GENERAL")) {
                    mode = GameStateEnum.GENERAL;
                }
            }
        });

        gameState.add(comboBox);
        startWindow.add(gameState, BorderLayout.NORTH);

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);

    }

}
