package ch.sparkpudding.sceneeditor.action;

import java.awt.Rectangle;

import ch.sparkpudding.coreengine.ecs.entity.Entity;

/**
 * The action to move / resize an entity. Must be called with its undo value
 * (used by SE to move entity in games)
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 31 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionTransformEntity extends AbstractAction {
	private Rectangle oldRectangle;
	private Rectangle newRectangle;
	private Entity entity;

	/**
	 * Transform size/pos of entity
	 * 
	 * @param entity   to change
	 * @param oldValue old rectangle for undo
	 * @param newValue new rectnagle for redo
	 */
	public ActionTransformEntity(Entity entity, Rectangle oldValue, Rectangle newValue) {
		super("Move/Resize entity " + entity.getName());
		this.entity = entity;
		this.oldRectangle = oldValue;
		this.newRectangle = newValue;
	}

	/**
	 * Set the value for the entity
	 */
	@Override
	public boolean doAction() {
		if (newRectangle.getX() == oldRectangle.getX() && newRectangle.getY() == oldRectangle.getY()
				&& newRectangle.getWidth() == oldRectangle.getWidth()
				&& newRectangle.getHeight() == oldRectangle.getHeight()) {
			return false;
		}
		entity.getComponents().get("position").getField("x").setValue(newRectangle.getX());
		entity.getComponents().get("position").getField("y").setValue(newRectangle.getY());
		entity.getComponents().get("size").getField("width").setValue(newRectangle.getWidth());
		entity.getComponents().get("size").getField("height").setValue(newRectangle.getHeight());
		return true;
	}

	/**
	 * Reset the oldValue
	 */
	@Override
	public void undoAction() {
		entity.getComponents().get("position").getField("x").setValue(oldRectangle.getX());
		entity.getComponents().get("position").getField("y").setValue(oldRectangle.getY());
		entity.getComponents().get("size").getField("width").setValue(oldRectangle.getWidth());
		entity.getComponents().get("size").getField("height").setValue(oldRectangle.getHeight());
	}

}
