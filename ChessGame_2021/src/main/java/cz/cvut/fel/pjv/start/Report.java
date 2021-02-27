package cz.cvut.fel.pjv.start;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Report {

    private static Logger logger = Logger.getLogger(GameManager.class.getName());
    private static Handler fileHandler = null;
    private boolean ReportIsFirstInUse = false;
    private List<String> moveSequence = GameManager.getInstance().getMoveSequence();
    
    private void createReport() throws IOException {
        Date dateNow = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("YYYY.MM.DD");

        File myFile = new File(formatDate + "Game.txt");

        if (ReportIsFirstInUse == false) {
            try {
                PrintWriter writer = new PrintWriter(myFile);
                logger.info("Clearing the contents of the Reports.txt file starts");
                writer.print("");
                writer.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error occur in file cleaning", e);
            }
            ReportIsFirstInUse = true;
        }

        try {
            PrintWriter writer = new PrintWriter(myFile);
            logger.info("Printing GameReport to file starts");

//            writer.println("============================================================");
//            writer.println("HOUSE CONFIGURATION REPORT");
//            writer.println("============================================================");
            int counter = 1;
            for (String str : moveSequence) {
                str = counter + ". " + str;
                writer.println(str);
                counter += 1;
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error occur in printing GameReport", ex);
            ex.printStackTrace();
        }

    }

}
