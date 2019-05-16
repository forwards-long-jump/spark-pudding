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
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;
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

					SceneEditor.setSelectedEntity(((SEEntity) ((JList<?>) e.getSource()).getSelectedValue()));

				} else {
					panelEntity.removeEntity();
				}
			}
		});

		SceneEditor.coreEngine.getScheduler().notify(Trigger.COMPONENT_ADDED, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < SceneEditor.coreEngine.getCurrentScene().getEntities().size(); i++) {
					Entity entity = SceneEditor.coreEngine.getCurrentScene().getEntities().get(i);
					if (entity.hasComponent("se-selected")) {
						for (SEEntity seEntity : SceneEditor.currentScene.getSEEntities()) {
							// FIXME: this *kinda* works but could be way better
							if (seEntity.getLiveEntity() == entity && (jListEntities.getSelectedValue() == null
									|| jListEntities.getSelectedValue().getLiveEntity() != entity)) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										selectSEEntity(seEntity);
									}
								});
								return;
							}
						}
					}
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

		revalidate();
	}

	/**
	 * Select the specified seEntity in the tree
	 * 
	 * @param entity to select
	 */
	public void selectSEEntity(SEEntity entity) {
		jListEntities.setSelectedValue(entity, true);
	}

}
