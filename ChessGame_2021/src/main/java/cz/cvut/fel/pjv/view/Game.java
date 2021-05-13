
package cz.cvut.fel.pjv.view;

import javax.swing.*;

/**
 *
 * @author kira
 */
public class Game implements Runnable {
    public void run() {
        SwingUtilities.invokeLater(new StartMenu());
    }
        
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
    
}
