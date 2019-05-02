package testengine;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.sparkpudding.coreengine.CoreEngine;

public class Main {

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
        jframe.setTitle("SparkPudding - TestEngine");
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setSize(1280, 800);
        jframe.setLocationRelativeTo(null);
        
        CoreEngine ce = null;

        try {
            ce = new CoreEngine(Main.class.getResource("/testgame").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        jframe.add(ce);
        
        jframe.setVisible(true);
	}
}
