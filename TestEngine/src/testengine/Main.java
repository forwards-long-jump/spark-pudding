package testengine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;

public class Main {
	static CoreEngine ce = null;

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.setTitle("SparkPudding - TestEngine");
		jframe.setSize(1280, 800);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		try {
			ce = new CoreEngine(ClassLoader.getSystemResource("testgame2").getPath());
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
					ce.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {
						
						@Override
						public void run() {
							ce.reloadSystemsFromDisk();
						}
					});
				}
			}
		});

		// Source: https://www.baeldung.com/java-nio2-watchservice
		// Make the test engine detect system changes and automatically refresh the game
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();

			Path path = Paths.get(new URI(ClassLoader.getSystemResource("testgame2/systems/").toString()));
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				key.pollEvents().clear();

				ce.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {
					
					@Override
					public void run() {
						ce.reloadSystemsFromDisk();
					}
				});
				key.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
