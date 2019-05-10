package ch.sparkpudding.sceneeditor.panel;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.ecs.SEScene;

/**
 * The panel which show the different scene
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
@SuppressWarnings("serial")
public class PanelScene extends JPanel {

	private PanelEntityTree panelEntityTree;

	private JComboBox<String> comboBoxScenes;

	private static final String TITLE = "Scenes";

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

		// TODO Implement editable to add scene
		// comboBoxScenes.setEditable(true);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		comboBoxScenes.setPreferredSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, comboBoxScenes.getPreferredSize().height));
		comboBoxScenes.setMaximumSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, comboBoxScenes.getPreferredSize().height));

		add(comboBoxScenes);

		setBorder(BorderFactory.createTitledBorder(TITLE));
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		comboBoxScenes.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					SEScene newScene = SceneEditor.seScenes.get(comboBoxScenes.getSelectedItem());
					SceneEditor.coreEngine.setCurrentScene(newScene.getLiveScene());
					panelEntityTree.updateListEntities(newScene);
				}
			}
		});
	}

	/**
	 * Populate the panel, allow to wait for the coreEngine to load all the scenes
	 */
	public void populatePanel() {
		// Get the last currentScene to prevent selecting something else when populating
		Scene lastScene = SceneEditor.coreEngine.getCurrentScene();

		// Populate
		comboBoxScenes.removeAllItems();
		for (SEScene scene : SceneEditor.seScenes.values()) {
			comboBoxScenes.addItem(scene.getLiveScene().getName());
		}

		// Reset lastScene after populating
		comboBoxScenes.setSelectedItem(lastScene.getName());
	}

}
