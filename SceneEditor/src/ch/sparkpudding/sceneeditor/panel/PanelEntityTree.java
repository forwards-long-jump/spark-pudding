package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
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

	private DefaultListModel<Entity> listModelEntities;
	private JList<Entity> jListEntities;
	private JScrollPane listScroller;

	private static final String TITLE = "Entity list";

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
		listModelEntities = new DefaultListModel<Entity>();

		jListEntities = new JList<Entity>(listModelEntities);
		jListEntities.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListEntities.setLayoutOrientation(JList.VERTICAL);
		jListEntities.setVisibleRowCount(-1);
		jListEntities.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof Entity) {
					// Here value will be of the Type 'CD'
					((JLabel) renderer).setText(((Entity) value).getName());
				}
				return renderer;
			}
		});

		listScroller = new JScrollPane(jListEntities);
		listScroller.setPreferredSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, PanelSidebarRight.BASIC_ELEMENT_HEIGHT));
		listScroller.setMaximumSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, PanelSidebarRight.BASIC_ELEMENT_HEIGHT));
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());

		add(listScroller, BorderLayout.CENTER);

		setBorder(BorderFactory.createTitledBorder(TITLE));
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		jListEntities.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && e.getSource() instanceof JList<?>
						&& ((JList<?>) e.getSource()).getSelectedValue() instanceof Entity) {
					panelEntity.setEntity((Entity) ((JList<?>) e.getSource()).getSelectedValue());
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
			listModelEntities.addElement(entity);
		}
	}

}
