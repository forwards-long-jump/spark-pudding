package testengine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.sparkpudding.coreengine.CoreEngine;

public class Main {
	static CoreEngine ce = null;

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.setTitle("SparkPudding - TestEngine");
		jframe.setSize(1280, 800);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		try {
			ce = new CoreEngine(Main.class.getResource("/testgame").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		jframe.add(ce);

		jframe.setVisible(true);
		
		// Refresh all systems when F5 is pressed
		ce.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_F5) {
					ce.scheduleSystemReloadFromDisk();
				}
			}
		});	
	}
}
