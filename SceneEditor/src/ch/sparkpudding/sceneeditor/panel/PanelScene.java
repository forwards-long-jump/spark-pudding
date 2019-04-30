package ch.sparkpudding.sceneeditor.panel;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.FrameSceneEditor;

/**
 * The panel which show the different scene
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelScene extends JPanel {

	private PanelEntityTree panelEntityTree;

	private JComboBox<String> comboBoxScenes;

	/**
	 * ctor
	 * 
	 * @param panelEntityTree the panel which show the entities of a scene
	 */
	public PanelScene(PanelEntityTree panelEntityTree) {
		this.panelEntityTree = panelEntityTree;

		init();
		setupLayout();
		addListener();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		comboBoxScenes = new JComboBox<String>();

		for (Scene scene : FrameSceneEditor.coreEngine.getScenes().values()) {
			comboBoxScenes.addItem(scene.getName());
		}

		comboBoxScenes.setSelectedItem(FrameSceneEditor.coreEngine.getCurrentScene().getName());
		
		panelEntityTree.updateListEntities(FrameSceneEditor.coreEngine.getCurrentScene());
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new FlowLayout());

		add(comboBoxScenes);
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		comboBoxScenes.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Scene newScene = FrameSceneEditor.coreEngine.getScenes().get(comboBoxScenes.getSelectedItem());
				FrameSceneEditor.coreEngine.setCurrentScene(newScene);
				panelEntityTree.updateListEntities(newScene);
			}
		});
	}

}
