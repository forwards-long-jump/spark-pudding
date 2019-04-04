package ch.sparkpudding.coreengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.Scene;
import ch.sparkpudding.coreengine.filereader.LelManager;

public class CoreEngine {

	private double msPerUpdate = (1000 / 60);
	private boolean exit = false;
	
	private JPanel panel;
	public Input input;
	
	private LelManager lelManager;
	
	private Map<String, Scene> scenes;
	private Scene currentScene;
	
	private List<System> systems;
	private System renderSystem;	

	public CoreEngine(JPanel panel, String gameFolder) throws Exception {
		this.panel = panel;
		this.input = new Input(panel);
		
		this.lelManager = new LelManager(gameFolder);
		
		this.scenes = new HashMap<String, Scene>();
		this.currentScene = null;
		
		this.systems = new ArrayList<System>();
		this.renderSystem = null;
		
		
		new Thread(() -> {
			startGame();
		}).start();
	}

	private void startGame() {
		double previous = System.currentTimeMillis();
		double lag = 0.0;

		while (!exit) {
			double current = System.currentTimeMillis();
			double elapsed = current - previous;

			previous = current;
			lag += elapsed;

			while (lag >= msPerUpdate) {
				update();
				lag -= msPerUpdate;
			}

			render();
		}
	}

	private void update() {
		// TODO: Update logic
		// for 
	}

	private void render() {
		// TODO: Render logic
		// using panel and renderSystem
	}
	
	public void pauseAll()
	{
		// TODO: pause
	}
	
	public void pause()
	{
		// TODO: pause (toggle)
	}
	
	public void addScene(String name, Scene s)
	{
		scenes.put(name, s);
	}
	
	public void setScene(String name)
	{
		setScene(name, false);
	}
	
	public void setScene(String name, boolean reset)
	{
		// TODO: set current scene
	}
	
	public void resetScene()
	{
		// TODO: reset current scene
	}
}
