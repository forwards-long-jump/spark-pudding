package ch.sparkpudding.sceneeditor.menu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Camera.Mode;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Represent the MenuCamera of the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 22 May 2019
 *
 */
@SuppressWarnings("serial")
public class MenuCamera extends JMenu {

	private JMenuItem resetToOrigin;
	private JMenuItem moveToEntity;
	private JMenuItem moveEntityToCamera;
	private JMenuItem moveToGameView;

	/**
	 * ctor
	 */
	public MenuCamera() {
		init();
		addAction();
		addKeyStroke();
		addItem();
	}

	/**
	 * Set basic attributes and create item
	 */
	private void init() {
		setText("Camera");

		resetToOrigin = new JMenuItem("Move to origin", KeyEvent.VK_O);
		moveToEntity = new JMenuItem("Move to selected entity", KeyEvent.VK_E);
		moveEntityToCamera = new JMenuItem("Move selected entity to camera", KeyEvent.VK_C);
		moveToGameView = new JMenuItem("Move camera to game view", KeyEvent.VK_G);
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addAction() {
		moveEntityToCamera.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (SceneEditor.selectedEntity != null
						&& SceneEditor.selectedEntity.getLiveEntity().hasComponent("position")) {

					Camera camera = SceneEditor.getEditingCamera();
					Entity entity = SceneEditor.selectedEntity.getLiveEntity();
					Component position = entity.getComponents().get("position");

					Dimension halfSize = new Dimension();

					if (entity.hasComponent("size")) {
						halfSize.setSize(entity.getComponents().get("size").getFields().get("width").getDouble() / 2,
								entity.getComponents().get("size").getFields().get("height").getDouble() / 2);
					}

					position.getField("x")
							.setValue((camera.getPosition().getX() + SceneEditor.coreEngine.getGameWidth() / 2)
									/ camera.getScaling() - halfSize.getWidth());

					position.getField("y")
							.setValue((camera.getPosition().getY() + SceneEditor.coreEngine.getGameHeight() / 2)
									/ camera.getScaling() - halfSize.getHeight());
				}
			}
		});

		moveToEntity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera camera = SceneEditor.getEditingCamera();
				setCameraSettings(camera);

				if (SceneEditor.selectedEntity != null
						&& SceneEditor.selectedEntity.getLiveEntity().hasComponent("position")) {
					Entity entity = SceneEditor.selectedEntity.getLiveEntity();
					Component position = entity.getComponents().get("position");
					Dimension halfSize = new Dimension();

					if (entity.hasComponent("size")) {
						halfSize.setSize(entity.getComponents().get("size").getFields().get("width").getDouble() / 2,
								entity.getComponents().get("size").getFields().get("height").getDouble() / 2);
					}

					// Uncomment to make camera scale at its default size when moving to an entity
//					if (camera.getScaling() > 1) {
//						SceneEditor.getEditingCamera().setScalingPoint(
//								new Point2D.Double(position.getField("x").getDouble() / camera.getScaling(),
//										position.getField("y").getDouble() / camera.getScaling()));
//					} else {
//						camera.setScalingPoint(
//								new Point2D.Double(position.getField("x").getDouble() * camera.getScaling(),
//										position.getField("<").getDouble() * camera.getScaling()));
//					}
//					camera.setTargetScaling(1);

					camera.centerTargetAt((float) (position.getField("x").getDouble() + halfSize.getWidth()),
							(float) (position.getField("y").getDouble() + halfSize.getHeight()));

				}
			}
		});

		resetToOrigin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera camera = SceneEditor.getEditingCamera();
				setCameraSettings(camera);

				if (camera.getScaling() > 1) {
					SceneEditor.getEditingCamera()
							.setScalingPoint(new Point2D.Double(camera.getPosition().getX() / camera.getScaling(),
									camera.getPosition().getY() / camera.getScaling()));
				} else {
					camera.setScalingPoint(new Point2D.Double(camera.getPosition().getX() * camera.getScaling(),
							camera.getPosition().getY() * camera.getScaling()));
				}

				camera.setTargetPosition(0, 0);
				camera.setTargetScaling(1);
			}
		});
		
		moveToGameView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Camera camera = SceneEditor.getEditingCamera();
				Camera gameCamera = SceneEditor.getGameCamera();
			
				camera.setTranslateMode(Mode.NO_FOLLOW);
				camera.setPosition(gameCamera.getPosition().getX(), gameCamera.getPosition().getY());
				camera.setTargetToPosition();
				camera.setScalingPoint(new Point2D.Double(SceneEditor.coreEngine.getGameWidth() / 2, SceneEditor.coreEngine.getGameHeight() / 2));
				camera.setSmoothScaleSpeedCoeff(0.1f);
				camera.setScaling(gameCamera.getScaling());
				camera.setTargetScaling(gameCamera.getScaling());
			}
		});
	}

	/**
	 * Set camera settings to offer a smooooth experience
	 * 
	 * @param camera
	 */
	private void setCameraSettings(Camera camera) {
		camera.setTranslateMode(Mode.SMOOTH);
		camera.setSmoothScaleSpeedCoeff(0.05f);
		camera.setSmoothSpeedCoeff(new Point2D.Double(0.1, 0.1));
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addKeyStroke() {
		moveToGameView.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_DOWN_MASK));
		resetToOrigin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));
		moveToEntity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.CTRL_DOWN_MASK));
		moveEntityToCamera.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));
	}

	/**
	 * Add the item to the menu
	 */
	private void addItem() {
		add(moveToGameView);
		add(resetToOrigin);
		add(moveToEntity);
		add(moveEntityToCamera);
	}

}
