package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.generator.ComponentGenerator;

/**
 * Display the components of the selected entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 9 April 2019
 * 
 */
@SuppressWarnings("serial")
public class PanelComponent extends JPanel {
	private ComponentGenerator componentGenerator;

	/**
	 * ctor
	 */
	public PanelComponent() {
		setupLayout();
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Set the games entity represented by this panel
	 * 
	 * @param entity The entity represented by this panel
	 */
	public void setEntity(SEEntity seEntity, Entity entity) {
		final int previousScroll;

		if (componentGenerator != null) {
			previousScroll = componentGenerator.getScrollPosition();
		} else {
			previousScroll = 0;
		}

		componentGenerator = new ComponentGenerator(seEntity, entity);

		removeAll();

		add(componentGenerator, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				componentGenerator.setScrollPosition(previousScroll);
			}
		});
		revalidate();
	}

	/**
	 * Override the default behavior to disable recursively all the components
	 */
	@Override
	public void setEnabled(boolean enabled) {
		if (componentGenerator != null) {
			componentGenerator.setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}
}
