package cz.cvut.fel.pjv.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartMenu implements Runnable {

    public void run() {
        final JFrame startWindow = new JFrame("Chess");

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final JButton start = new JButton("Start");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new GameWindow();
                startWindow.dispose();
            }
        });

        buttons.add(start);
        startWindow.add(buttons, BorderLayout.SOUTH);

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
        // 

    }


}
