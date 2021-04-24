package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.start.MovesIterator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GameWindowGameComp {

    private GameWindowBasic gameWindowBasic;
    private JFrame gameWindowFrame;
    private BoardPanel boardPanel;
    private GameManager gameManager = GameManager.getInstance();
    private boolean whiteIsActive = true;
    private Stack<String> tagType;
    private Piece currentPiece = null;
    private Tile tile = null;
    private LinkedList<Tile> tileList;
    private static Logger logger = Logger.getLogger(GameForm.class.getName());
    private String gameView = "";
    private JTextArea textArea = new JTextArea();
    private StringBuilder sb = new StringBuilder();
    private Box components = Box.createVerticalBox();
    private int computer = 0;

    public GameWindowGameComp(GameWindowBasic gameWindowBasic, BoardPanel bp) {

        this.gameWindowBasic = gameWindowBasic;
        this.gameWindowFrame = new JFrame("Chess II model");
        gameWindowFrame.setLocation(100, 100);

        this.boardPanel =bp;

        this.tagType = boardPanel.getTagType();
        this.tileList = gameManager.getTileList();
        this.gameView = gameWindowBasic.getGameView();

        JPanel textPanel = textJPanel();
        textPanel.setSize(textPanel.getPreferredSize());
        gameWindowFrame.add(textPanel, BorderLayout.EAST);

        gameWindowFrame.add(boardPanel, BorderLayout.CENTER);

        gameWindowFrame.add(buttons(), BorderLayout.SOUTH);
        gameWindowFrame.add(components, BorderLayout.NORTH);
        fillComponents();

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
        JScrollBar scrollBar = new JScrollBar();

        textPanel.add(textArea);
        textPanel.add(scrollBar);
        //   textPanel.setLayout(new GridLayout(3, 2, 0, 0));
        textPanel.setPreferredSize(new Dimension(200, 400));
        return textPanel;
    }

    private void fillComponents() {

        Box comp = Box.createVerticalBox();
        final JTextField compInput = new JTextField("Comp", 10);
        comp.add(compInput);
        final JButton compTag = new JButton("Comp tag");

        compTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doTag();
            }
        });
        comp.add(compTag);

        components.add(comp);

        Box person = Box.createVerticalBox();
        final JTextField personInput = new JTextField("Person", 10);
        person.add(personInput);
        components.add(person);
        Box.createVerticalGlue();

        components.setPreferredSize(components.getMinimumSize());

    }

    private void doTag() {
        boardPanel.doTah();
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setGameView(String gameView) {
        this.gameView = gameView;
    }
    
    

}
