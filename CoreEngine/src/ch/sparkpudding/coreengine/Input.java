package ch.sparkpudding.coreengine;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public class Input {

	// Values read by the panel
	private List<Integer> keysPressed;
	private List<Integer> keysReleased;
	private List<Integer> mouseButtonsPressed;
	private List<Integer> mouseButtonsReleased;
	private Dimension mousePositionBuffer;
	private boolean mouseClickedBuffer = false;

	// Values to be read by systems
	private Map<Integer, Boolean> keys;
	private Map<Integer, Boolean> mouseButtons;
	private Dimension mousePosition;
	private boolean mouseClicked = false;

	private JPanel panel;

	public Input(JPanel panel) {
		this.panel = panel;
		createListeners();

		keysPressed = new ArrayList<Integer>();
		keysReleased = new ArrayList<Integer>();
		mouseButtonsPressed = new ArrayList<Integer>();
		mouseButtonsReleased = new ArrayList<Integer>();
		mousePositionBuffer = new Dimension();

		keys = new HashMap<Integer, Boolean>();
		mouseButtons = new HashMap<Integer, Boolean>();
	}

	public void update() {
		for (Integer key : keysPressed) {
			keys.put(key, true);
		}
		
		for (Integer key : keysReleased) {
			keys.put(key, false);
		}
		
		for (Integer key : mouseButtonsPressed) {
			mouseButtons.put(key, true);
		}
		
		for (Integer key : mouseButtonsReleased) {
			mouseButtons.put(key, false);
		}
		
		mousePosition = mousePositionBuffer;
		
		mouseClicked = mouseClickedBuffer;
		mouseClickedBuffer = false;
	}

	public void createListeners() {

		panel.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				keys.put(e.getKeyCode(), true);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keys.put(e.getKeyCode(), false);
			}
		});

		panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseButtonsReleased.add(e.getButton());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseButtonsPressed.add(e.getButton());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouseClickedBuffer = true;
			}
		});
	}

	public boolean isKeyDown(int keyCode) {
		return keys.getOrDefault(keyCode, false);
	}
	
	public boolean isMouseButtonDown(int keyCode) {
		return mouseButtons.getOrDefault(keyCode, false);
	}
	
	public boolean isMouseClicked()
	{
		return mouseClicked;
	}
	
	public Dimension getMousePosition() {
		return mousePosition;
	}
}
