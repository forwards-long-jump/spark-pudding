package ch.sparkpudding.sceneeditor.panel;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Entity;

/**
 * Contains the different parameter of an entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntity extends JPanel {

	private PanelComponent panelComponent;
	
	private Entity currentEntity;

	/**
	 * ctor
	 */
	public PanelEntity() {
		init();
		setupLayout();
		addListener();
	}

	private void init() {
		this.panelComponent = new PanelComponent();
	}

	private void setupLayout() {
		setLayout(new FlowLayout());
		
		add(panelComponent);
	}

	private void addListener() {
		// TODO Auto-generated method stub
		
	}

	public void setEntity(Entity entity) {
		currentEntity = entity;
		panelComponent.setEntity(currentEntity);
	}
}
