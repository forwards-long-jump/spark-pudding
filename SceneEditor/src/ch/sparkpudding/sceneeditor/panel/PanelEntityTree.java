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

import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;

/**
 * Show the different entity of a Scene as a list
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntityTree extends JPanel {

	private PanelEntity panelEntity;

	private DefaultListModel<SEEntity> listModelEntities;
	private JList<SEEntity> jListEntities;
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
		listModelEntities = new DefaultListModel<SEEntity>();

		jListEntities = new JList<SEEntity>(listModelEntities);
		jListEntities.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListEntities.setLayoutOrientation(JList.VERTICAL);
		jListEntities.setVisibleRowCount(-1);
		jListEntities.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof SEEntity) {
					// Here value will be of the Type 'CD'
					((JLabel) renderer).setText(((SEEntity) value).getDefaultEntity().getName());
				}
				return renderer;
			}
		});

		listScroller = new JScrollPane(jListEntities);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());
		
		listScroller.setPreferredSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, PanelSidebarRight.BASIC_ELEMENT_HEIGHT));
		listScroller.setMaximumSize(
				new Dimension(PanelSidebarRight.BASIC_ELEMENT_WIDTH, PanelSidebarRight.BASIC_ELEMENT_HEIGHT));

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
						&& ((JList<?>) e.getSource()).getSelectedValue() instanceof SEEntity) {
					panelEntity.setEntity((SEEntity) ((JList<?>) e.getSource()).getSelectedValue());
				} else {
					panelEntity.removeEntity();
				}
			}
		});
	}

	/**
	 * Update the entity list with a Scene
	 * 
	 * @param scene The scene which contains the entities
	 */
	public void updateListEntities(SEScene scene) {
		listModelEntities.removeAllElements();

		for (SEEntity entity : scene.getSEEntities()) {
			listModelEntities.addElement(entity);
		}
	}

}
