package ch.sparkpudding.sceneeditor.panel;

import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;

/**
 * Show the different entity of a Scene as a list
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntityTree extends JPanel {

	private PanelEntity panelEntity;

	private DefaultListModel<String> listModelEntities;
	private JList<String> jListEntities;
	private JScrollPane listScroller;

	/**
	 * ctor
	 * 
	 * @param panelEntity the panel where to show the informations of a selected
	 *                    entity
	 */
	public PanelEntityTree(PanelEntity panelEntity) {
		this.panelEntity = panelEntity;

		init();
		setupLayout();
		addListener();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		listModelEntities = new DefaultListModel<String>();

		jListEntities = new JList<String>(listModelEntities);
		jListEntities.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListEntities.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jListEntities.setVisibleRowCount(-1);

		listScroller = new JScrollPane(jListEntities);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new FlowLayout());

		add(listScroller);
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		jListEntities.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					System.out.println("Salut " + ((JList<String>) e.getSource()).getSelectedValue());
				}
			}
		});
	}

	/**
	 * Update the entity list with a Scene
	 * 
	 * @param scene The scene which contains the entities
	 */
	public void updateListEntities(Scene scene) {
		listModelEntities.removeAllElements();

		for (Entity entity : scene.getEntities()) {
			listModelEntities.addElement(entity.getName());
		}
	}

}
