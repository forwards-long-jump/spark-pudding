package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import ch.sparkpudding.coreengine.utils.Pair;
import ch.sparkpudding.coreengine.utils.RunnableOneParameter;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.action.AbstractAction;
import ch.sparkpudding.sceneeditor.action.ActionRemoveEntity;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;
import ch.sparkpudding.sceneeditor.listener.EntityEventAdapter;
import ch.sparkpudding.sceneeditor.panel.modal.ModalEntity;

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

	private JPanel panelButtons;
	private JButton buttonAdd;
	private JButton buttonRemove;

	private boolean canChange;

	private static final String TITLE = "Entity list";

	/**
	 * ctor
	 *
	 * @param panelEntity the panel where to show the informations of a selected
	 *                    entity
	 */
	public PanelEntityTree(PanelEntity panelEntity) {
		this.panelEntity = panelEntity;
		this.canChange = true;
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
		jListEntities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

		panelButtons = new JPanel();
		buttonAdd = new JButton("+");
		buttonRemove = new JButton("-");
		buttonRemove.setEnabled(false);
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
		add(panelButtons, BorderLayout.SOUTH);

		panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panelButtons.add(buttonRemove);
		panelButtons.add(buttonAdd);

		setBorder(BorderFactory.createTitledBorder(TITLE));
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		jListEntities.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(jListEntities.getSelectedIndex() != -1) {
					SceneEditor.setSelectedEntity(jListEntities.getSelectedValue());					
				}
			}
		});

		SceneEditor.coreEngine.getScheduler().notify(Trigger.COMPONENT_ADDED, new RunnableOneParameter() {
			@Override
			public void run() {
				ch.sparkpudding.coreengine.ecs.component.Component component = (ch.sparkpudding.coreengine.ecs.component.Component) ((Pair<?, ?>) getObject())
						.second();

				if (component.getName().equals("se-selected")) {
					Entity entity = (Entity) ((Pair<?, ?>) getObject()).first();

					for (SEEntity seEntity : SceneEditor.currentScene.getSEEntities()) {
						// FIXME: this *kinda* works but could be way better
						if (seEntity.getLiveEntity() == entity && canChange) {
							if ((jListEntities.getSelectedValue() == null
									|| jListEntities.getSelectedValue().getLiveEntity() != entity)) {
								canChange = false;
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										canChange = true;
										selectSEEntity(seEntity);
									}
								});
								return;
							} else {
								return;
							}
						}
					}

					// Entity not found
					if (canChange) {
						SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

							@Override
							public void run() {
								SceneEditor.setSelectedEntity(new SEEntity(null, entity));

							}
						});

						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								jListEntities.removeSelectionInterval(0, jListEntities.getModel().getSize());
							}
						});
					}
				}
			}
		});

		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ModalEntity();
			}
		});

		buttonRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractAction action = new ActionRemoveEntity(SceneEditor.selectedEntity,
						SceneEditor.currentScene.getLiveScene());
				action.actionPerformed(e);
			}
		});

		SceneEditor.addEntityEventListener(new EntityEventAdapter() {
			@Override
			public void changeSelectedEntity(SEEntity entity) {
				if (entity != null) {
					buttonRemove.setEnabled(true);
				} else {
					buttonRemove.setEnabled(false);
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
		SEEntity previousEntity = jListEntities.getSelectedValue();
		listModelEntities.removeAllElements();

		List<SEEntity> sortedEntities = new ArrayList<SEEntity>();
		for (SEEntity entity : scene.getSEEntities()) {
			sortedEntities.add(entity);
		}

		sortedEntities.sort(new Comparator<SEEntity>() {
			@Override
			public int compare(SEEntity arg0, SEEntity arg1) {
				if (arg0.getLiveEntity().getZIndex() > arg1.getLiveEntity().getZIndex()) {
					return 1;
				} else if (arg0.getLiveEntity().getZIndex() < arg1.getLiveEntity().getZIndex()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		for (SEEntity entity : sortedEntities) {
			listModelEntities.addElement(entity);
		}

		jListEntities.setSelectedValue(previousEntity, true);

		revalidate();
	}

	/**
	 * Select the specified seEntity in the tree
	 *
	 * @param entity to select
	 */
	public void selectSEEntity(SEEntity entity) {
		if (jListEntities.getSelectedValue() != entity) {
			jListEntities.setSelectedValue(entity, true);
		}
	}

	/**
	 * Remove selected entity components display 
	 */
	public void removeSelectedEntity() {
		panelEntity.removeEntity();
	}

}
