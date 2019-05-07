package ch.sparkpudding.sceneeditor.panel;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.Lel;

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

		for (Scene scene : Lel.coreEngine.getScenes().values()) {
			comboBoxScenes.addItem(scene.getName());
		}

		comboBoxScenes.setSelectedItem(Lel.coreEngine.getCurrentScene().getName());
		comboBoxScenes.setPreferredSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, comboBoxScenes.getPreferredSize().height));
		comboBoxScenes.setMaximumSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, comboBoxScenes.getPreferredSize().height));

		// TODO Implement editable to add scene
		// comboBoxScenes.setEditable(true);

		panelEntityTree.updateListEntities(Lel.coreEngine.getCurrentScene());
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

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
				Scene newScene = Lel.coreEngine.getScenes().get(comboBoxScenes.getSelectedItem());
				Lel.coreEngine.setCurrentScene(newScene);
				panelEntityTree.updateListEntities(newScene);
			}
		});
	}

}
