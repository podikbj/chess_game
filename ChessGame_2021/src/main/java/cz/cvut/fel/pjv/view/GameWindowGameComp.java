package cz.cvut.fel.pjv.view;

//import cz.cvut.fel.pjv.chessgame.Piece;
//import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.GameManager;
//import cz.cvut.fel.pjv.start.MovesIterator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.Stack;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GameWindowGameComp {

    private final GameWindowBasic gameWindowBasic;
    private final JFrame gameWindowFrame;
    private final BoardPanel boardPanel;
    private final GameManager gameManager = GameManager.getInstance();

    private static Logger logger = Logger.getLogger(GameForm.class.getName());
    private String gameView = "";
    private JTextArea textArea = new JTextArea();

    private int computer = -1; // 0 - black color

    public GameWindowGameComp(GameWindowBasic gameWindowBasic, BoardPanel bp) {

        this.gameWindowBasic = gameWindowBasic;
        this.gameWindowFrame = new JFrame("Play chess game with PC mode");
        gameWindowFrame.setLocation(100, 100);

        this.boardPanel = bp;
        this.gameView = gameWindowBasic.getGameView();

        JPanel players = createPlayersPanel();

        players.setSize(players.getPreferredSize());
        gameWindowFrame.add(players, BorderLayout.NORTH);

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);
        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);

        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);

        gameWindowFrame.setSize(gameWindowFrame.getPreferredSize());
        gameWindowFrame.setSize(new Dimension(600, 600));
        gameWindowFrame.setResizable(false);
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
                        "Are you sure you want to quit?",
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
                        "Are you sure you want to upload a new game?",
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
        textPanel.setPreferredSize(new Dimension(200, 400));
        return textPanel;
    }

    private JPanel createPlayersPanel() {

        JPanel players = new JPanel();
        String[] colors = {Colors.NONE.name().toLowerCase(), Colors.BLACK.name().toLowerCase(),
            Colors.WHITE.name().toLowerCase()};

        final JComboBox comboBox = new JComboBox(colors);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                final String strColor = (String) box.getSelectedItem();
                if (strColor.equals("black")) {
                    computer = 0;

                }
                if (strColor.equals("white")) {
                    computer = 1;
                }
                boardPanel.setComputer(computer);
            }
        });

        final JButton compTag = new JButton("Comp tag");
        compTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doTag();
            }
        });

        final JPanel personPanel = new JPanel();
        final JTextField personInput = new JTextField("Person", 10);
        personPanel.add(personInput);

        Box playersSettings = Box.createHorizontalBox();

        playersSettings.add(personPanel);
        playersSettings.add(Box.createHorizontalStrut(10));
        playersSettings.add(comboBox);
        playersSettings.add(Box.createHorizontalStrut(10));
        playersSettings.add(compTag);
        playersSettings.add(Box.createHorizontalStrut(10));

        playersSettings.add(Box.createVerticalGlue());

        players.add(playersSettings);
        return players;
    }

    private void doTag() {
        boardPanel.doTah(computer);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setGameView(String gameView) {
        this.gameView = gameView;
    }

    public int getComputer() {
        return computer;
    }

    public void endGame(String winner) {
        if (winner == "1 - 0") {
//            if (timer != null) {
//                timer.stop();
//            }
            int n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "White wins by checkmate!",
                    "White wins!", JOptionPane.DEFAULT_OPTION);

        } else {
//            if (timer != null) {
//                timer.stop();
//            }
            int n = JOptionPane.showConfirmDialog(
                    gameWindowFrame,
                    "Black wins by checkmate!",
                    "Black wins!", JOptionPane.DEFAULT_OPTION);

        }
    }
}
