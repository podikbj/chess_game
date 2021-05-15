package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.start.GameManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Represents game window for playing with PC game mode
 *
 * @author kira
 */
public class GameWindowGameComp {

    private final JFrame gameWindowFrame;
    private final BoardPanel boardPanel;

    private static Logger logger = Logger.getLogger(GameWindowGameComp.class.getName());
    private String gameView = "";
    private JTextArea textArea = new JTextArea();

    private int computerColor = -1; // 0 - black color

    /**
     * Constructor for GameWindowGameAuto game window instance.
     *
     * @param gameWindowBasic basic game window
     * @param bp chess board
     */
    public GameWindowGameComp(GameWindowBasic gameWindowBasic, BoardPanel bp) {

        this.gameWindowFrame = new JFrame("Play chess game with PC mode");

        gameWindowFrame.setLocation(300, 100);
        gameWindowFrame.setSize(new Dimension(600, 600));
        gameWindowFrame.setResizable(false);

        this.boardPanel = bp;

        final JPanel titlePanel = new JPanel();
        titlePanel.setSize(titlePanel.getPreferredSize());

        Box components = createComponents();
        components.setSize(components.getPreferredSize());

        gameWindowFrame.add(titlePanel, BorderLayout.NORTH);
        gameWindowFrame.add(components, BorderLayout.NORTH);

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);
        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);

        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);

        gameWindowFrame.pack();
        gameWindowFrame.setVisible(true);
        gameWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final JButton quit = new JButton("Quit");

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Do you want to quit?",
                        "Confirm quit", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    gameWindowFrame.dispose();
                }
            }
        });

        final JButton nGame = new JButton("New game");
        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindowFrame,
                        "Do you want to load a new game?",
                        "Confirm new game", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    gameWindowFrame.dispose();
                }
            }
        });

        buttons.add(nGame);
        buttons.add(quit);
        buttons.setPreferredSize(buttons.getMinimumSize());

        return buttons;
    }

    private JPanel textJPanel() {
        JPanel textPanel = new JPanel();
        textArea.setEditable(false);
        textPanel.add(textArea);
        textPanel.setPreferredSize(new Dimension(250, 400));
        return textPanel;
    }

    private Box createComponents() {

        Box components = Box.createHorizontalBox();

        JPanel compPanel = new JPanel();
        String[] colors = {Colors.NONE.name().toLowerCase(), Colors.BLACK.name().toLowerCase(),
            Colors.WHITE.name().toLowerCase()};

        final JComboBox comboBox = new JComboBox(colors);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                final String strColor = (String) box.getSelectedItem();
                if (strColor.equals("black")) {
                    computerColor = 0;

                }
                if (strColor.equals("white")) {
                    computerColor = 1;
                }
                //boardPanel.setComputer(computerColor);
            }
        });

        final JButton compTag = new JButton("Comp tag");
        compTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doTag();
            }
        });

        compPanel.add(comboBox);
        compPanel.add(compTag);

        final JPanel personPanel = new JPanel();
        final JTextField personInput = new JTextField("Person", 10);
        personPanel.add(personInput);

        components.add(personPanel);
        components.add(compPanel);

        return components;
    }

    private void doTag() {
        boardPanel.doTah(computerColor);
    }

    /**
     * Getter for textArea
     *
     * @return
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * Setter for gameView
     *
     * @param gameView
     */
    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    /**
     * Getter for computerColor
     *
     * @return color of computer player
     */
    public int getComputer() {
        return computerColor;
    }

    /**
     * Ends game
     *
     * @param winner final game score
     * @param byTime true if game was played by time
     */
    public void endGame(String winner, boolean byTime) {
        if (winner == "1 - 0") {
            int n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "White wins by checkmate!",
                    "White wins!", JOptionPane.DEFAULT_OPTION);

        } else {

            int n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "Black wins by checkmate!",
                    "Black wins!", JOptionPane.DEFAULT_OPTION);

        }
    }
}
