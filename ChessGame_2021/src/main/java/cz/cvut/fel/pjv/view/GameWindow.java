package cz.cvut.fel.pjv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameWindow {

    private JFrame gameWindow;
    private final static Dimension FRAME_DIM = new Dimension(600, 600);
    private BoardPanel boardPanel;

    public GameWindow() {
        this.gameWindow = new JFrame("Chess");
        gameWindow.setLocation(100, 100);
        final JMenuBar menuBar = createMenuBar();

        gameWindow.setJMenuBar(menuBar);

        this.boardPanel = new BoardPanel(this);

        gameWindow.add(boardPanel, BorderLayout.CENTER);
        gameWindow.add(buttons(), BorderLayout.SOUTH);
        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setSize(FRAME_DIM);
        gameWindow.setResizable(false);
        gameWindow.pack();
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPNG = new JMenuItem("Open PNG file");
        openPNG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        fileMenu.add(openPNG);
        return fileMenu;
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final JButton quit = new JButton("Quit");
        //buttons.setPreferredSize(buttons.getMinimumSize());
        buttons.add(quit);
        return buttons;
    }

}
