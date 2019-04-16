package ch.sparkpudding.sceneeditor.action;

@SuppressWarnings("serial")
public class ActionTest extends AbstractAction {

	private int count = 0;

	public ActionTest(String name) {
		super(name);
	}
	
	@Override
	public void undoAction() {
		System.out.println("Undo dummy action");
		count--;
	}

	@Override
	public boolean doAction() {
		System.out.println("Dummy action" + count);
		count++;
		return true;
	}

}
