package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.RTextScrollPane;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.coreengine.ecs.system.System;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.action.ActionRemoveSystem;
import ch.sparkpudding.sceneeditor.listener.SystemEventListener;
import ch.sparkpudding.sceneeditor.panel.modal.ModalSystem;
import ch.sparkpudding.sceneeditor.utils.ButtonTabComponent;

/**
 * The panel for the lua editor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 21 May 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEditor extends JPanel {

	public static final int DEFAULT_PANEL_HEIGHT = 400;

	private JTabbedPane jTabbedPane;

	private ListModel<System> listModel;
	private List<String> nameSystems;
	private JScrollPane listScroller;
	private JList<System> jList;
	private JPanel jPanelList;
	private JPanel jPanelButton;

	private JButton buttonAdd;
	private JButton buttonRemove;

	/**
	 * ctor
	 */
	public PanelEditor() {
		init();
		setupLayout();
		addListener();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		nameSystems = new ArrayList<String>();
		jTabbedPane = new JTabbedPane();

		jPanelList = new JPanel();
		jPanelButton = new JPanel();

		listModel = new DefaultListModel<System>();
		jList = new JList<System>(listModel);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setVisibleRowCount(-1);
		jList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof System) {
					String name = ((System) value).getName();
					// name = name.substring(name.lastIndexOf("."));
					((JLabel) renderer).setText(name);
				}
				return renderer;
			}
		});

		populateSystemList();

		listScroller = new JScrollPane(jList);

		buttonAdd = new JButton("+");
		buttonRemove = new JButton("-");
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

		jPanelList.setLayout(new BorderLayout());
		jPanelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jPanelButton.add(buttonRemove);
		jPanelButton.add(buttonAdd);

		jPanelList.add(listScroller, BorderLayout.CENTER);
		jPanelList.add(jPanelButton, BorderLayout.SOUTH);

		add(jTabbedPane, BorderLayout.CENTER);
		add(jPanelList, BorderLayout.EAST);
	}

	/**
	 * Add a tab for a system and verify if the tab already exist
	 * 
	 * @param system the system to add
	 */
	private void addTab(System system) {
		if (jTabbedPane.indexOfTab(system.getName()) != -1) {
			jTabbedPane.setSelectedIndex(jTabbedPane.indexOfTab(system.getName()));
			return;
		}

		try {
			ButtonTabComponent buttonTabComponent = new ButtonTabComponent(jTabbedPane);
			CompletionProvider provider = createCompletionProviderForSystem(system);
			AutoCompletion autoCompletion = new AutoCompletion(provider);

			buttonTabComponent.setDirty(false);

			TextEditorPane editorPane = new TextEditorPane(TextEditorPane.INSERT_MODE, false,
					FileLocation.create(system.getFilepath()));
			editorPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
			editorPane.setCodeFoldingEnabled(true);
			editorPane.setText(readFile(system.getFilepath()));
			editorPane.setTabSize(2);
			editorPane.convertSpacesToTabs();
			editorPane.addCaretListener(new CaretListener() {

				@Override
				public void caretUpdate(CaretEvent e) {
					buttonTabComponent.setDirty(editorPane.isDirty());
				}
			});
			editorPane.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if ((e.getKeyCode() == KeyEvent.VK_S) && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
						try {
							editorPane.save();
							buttonTabComponent.setDirty(editorPane.isDirty());
							SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

								@Override
								public void run() {
									SceneEditor.coreEngine.reloadSystemsFromDisk();
									SceneEditor.clearError();
								}
							});
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});

			autoCompletion.install(editorPane);

			RTextScrollPane sp = new RTextScrollPane(editorPane);
			jTabbedPane.addTab(system.getName(), sp);
			jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount() - 1);
			jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount() - 1, buttonTabComponent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create the completion provider for a given system
	 * 
	 * @param system The system that need a completion provider
	 * @return The completion provider for the system
	 */
	private CompletionProvider createCompletionProviderForSystem(System system) {
		DefaultCompletionProvider codeCP = new DefaultCompletionProvider();
		Map<String, List<String>> componentGroups = system.getComponentGroups();

		// Basic ECS completion
		List<String> addedComponent = new ArrayList<String>();
		for (String groupName : componentGroups.keySet()) {
			for (String componentName : componentGroups.get(groupName)) {
				if (!addedComponent.contains(componentName)) {
					for (Entry<String, Field> field : ch.sparkpudding.coreengine.ecs.component.Component.getTemplates()
							.get(componentName)) {
						codeCP.addCompletion(new BasicCompletion(codeCP, componentName + "." + field.getKey(), ""));
					}
				}
				addedComponent.add(componentName);
			}
		}

		return codeCP;
	}

	/**
	 * Read a file
	 * 
	 * @param filepath The path to the file
	 * @return a {@code String} containing the content of the file
	 */
	private String readFile(String filepath) {
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filepath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		SceneEditor.addSystemEventListener(new SystemEventListener() {

			@Override
			public void systemListChanged() {
				populateSystemList();
				findRemovedSystem();
			}
		});

		jList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getSource() instanceof JList<?>) {

					@SuppressWarnings("unchecked")
					JList<System> list = (JList<System>) mouseEvent.getSource();
					if (mouseEvent.getClickCount() == 2) {
						int index = list.locationToIndex(mouseEvent.getPoint());
						if (index >= 0) {
							System system = list.getModel().getElementAt(index);
							addTab(system);
						}
					}

				}
			}
		});

		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ModalSystem();
			}
		});

		buttonRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!jList.getSelectedValue().getName().equals("render.lua")) {
						new ActionRemoveSystem(jList.getSelectedValue().getName()).actionPerformed(e);
					} else {
						JOptionPane.showMessageDialog(SceneEditor.frameSceneEditor,
								"You can't remove the render system.", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(SceneEditor.frameSceneEditor, "The system can't be removed.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Find if a system has been removed and remove the associated pane
	 */
	private void findRemovedSystem() {
		for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
			String title = jTabbedPane.getTitleAt(i);
			if (!nameSystems.contains(title)) {
				jTabbedPane.remove(i);

				return;
			}
		}
	}

	/**
	 * Populate the list of system
	 */
	private void populateSystemList() {
		List<System> oldSystems = new ArrayList<System>();
		for (int i = 0; i < ((DefaultListModel<System>) listModel).getSize(); i++) {
			oldSystems.add(((DefaultListModel<System>) listModel).get(i));
		}

		((DefaultListModel<System>) listModel).removeAllElements();
		nameSystems.clear();

		((DefaultListModel<System>) listModel).addElement(SceneEditor.coreEngine.getRenderSystems());
		for (System system : SceneEditor.coreEngine.getSystems()) {
			((DefaultListModel<System>) listModel).addElement(system);
			nameSystems.add(system.getName());
		}

		for (System newSystem : SceneEditor.coreEngine.getSystems()) {
			boolean isNew = true;
			for (System system : oldSystems) {
				if (system.getName().equals(newSystem.getName())) {
					isNew = false;
					break;
				}
			}

			if (isNew) {
				addTab(newSystem);
				break;
			}
		}

		revalidate();
	}
}
