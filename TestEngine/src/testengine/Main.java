package testengine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

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
				if (arg0.getKeyCode() == KeyEvent.VK_F5) {
					ce.scheduleSystemReloadFromDisk();
				}
			}
		});

		// Source: https://www.baeldung.com/java-nio2-watchservice
		// Make the test engine detect system changes and automatically refresh the game
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
			
			Path path = Paths.get(Main.class.getResource("/testgame/systems").getPath());
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				System.out.println("Meanwhile");
				
				key.pollEvents().clear();
				
				ce.scheduleSystemReloadFromDisk();
				key.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
