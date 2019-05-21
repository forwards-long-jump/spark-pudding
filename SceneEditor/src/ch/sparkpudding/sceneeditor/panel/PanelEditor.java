package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

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

	private DefaultListModel<String> listModel;
	private JScrollPane listScroller;
	private JList<String> jList;
	private JPanel jPanelList;
	private JPanel jPanelButton;

	private RSyntaxTextArea rSyntaxTextArea;

	private JButton buttonAdd;
	private JButton buttonRemove;

	/**
	 * ctor
	 */
	public PanelEditor() {
		init();
		setupLayout();
	}

	private void init() {
		jTabbedPane = new JTabbedPane();

		jPanelList = new JPanel();
		jPanelButton = new JPanel();

		listModel = new DefaultListModel<String>();
		jList = new JList<String>(listModel);
		jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setVisibleRowCount(-1);

		listModel.addElement("System1");
		listModel.addElement("System2");
		listModel.addElement("System3");
		listModel.addElement("System4");
		listModel.addElement("System5");

		listScroller = new JScrollPane(jList);

		rSyntaxTextArea = new RSyntaxTextArea(10, 60);
		rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
		rSyntaxTextArea.setCodeFoldingEnabled(true);

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

		RTextScrollPane sp = new RTextScrollPane(rSyntaxTextArea);
		jTabbedPane.addTab("system", sp);

		jPanelList.setLayout(new BorderLayout());
		jPanelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jPanelButton.add(buttonAdd);
		jPanelButton.add(buttonRemove);

		jPanelList.add(listScroller, BorderLayout.CENTER);
		jPanelList.add(jPanelButton, BorderLayout.SOUTH);

		add(jTabbedPane, BorderLayout.CENTER);
		add(jPanelList, BorderLayout.EAST);
	}
}
