package ch.sparkpudding.sceneeditor.panel;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.FrameSceneEditor;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class PanelScene extends JPanel {

	private PanelEntityTree panelEntityTree;

	private JComboBox<String> comboBoxScenes;

	public PanelScene(PanelEntityTree panelEntityTree) {
		this.panelEntityTree = panelEntityTree;

		init();
		setupLayout();
		addListener();
	}

	private void init() {
		comboBoxScenes = new JComboBox<String>();

		for (Scene scene : FrameSceneEditor.ce.getScenes().values()) {
			comboBoxScenes.addItem(scene.getName());
		}

		comboBoxScenes.setSelectedItem(FrameSceneEditor.ce.getCurrentScene().getName());
	}

	private void setupLayout() {
		setLayout(new FlowLayout());

		add(comboBoxScenes);
	}

	private void addListener() {
		comboBoxScenes.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				FrameSceneEditor.ce
						.setCurrentScene(FrameSceneEditor.ce.getScenes().get(comboBoxScenes.getSelectedItem()));
			}
		});
	}

}
