package ch.sparkpudding.sceneeditor.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * The action to register the addition of a new system
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 28 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionAddSystem extends AbstractAction {

	private String name;
	private String content =
			"function getRequiredComponents()\n" +
			"	return {\"position\", \"size\"}\n" 
		  + "end\n" 
		  + "\n" +
		  "function updateEntities(entity)\n" +
		  "	-- entity.position.x = entity.position.x + 1\n" +
		  "end\n" +
	      "\n" +
		  "--[[ Uncomment to loop manually\nfunction update()\n" + 
		  "	for i, entity in ipairs(entities) do\n" + 
	      "	end\n" + 
		  "end\n---]]";

	/**
	 * ctor
	 * 
	 * @param name The name of the system
	 */
	public ActionAddSystem(String name) {
		super("Add system (" + name + ")");
		this.name = name;
	}

	@Override
	public boolean doAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Path newFilePath = Paths.get(SceneEditor.coreEngine.getGameFolder() + "/systems/" + name + ".lua");
				try {
					Files.write(newFilePath, content.getBytes());
					SceneEditor.coreEngine.reloadSystemsFromDisk();

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							SceneEditor.fireSystemListChanged();
						}
					});

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		return true;
	}

	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Path newFilePath = Paths.get(SceneEditor.coreEngine.getGameFolder() + "/systems/" + name + ".lua");
				try {
					Files.delete(newFilePath);
					SceneEditor.coreEngine.reloadSystemsFromDisk();

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							SceneEditor.fireSystemListChanged();
						}
					});

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
