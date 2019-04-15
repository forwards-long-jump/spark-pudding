package ch.sparkpudding.sceneeditor.action;

@SuppressWarnings("serial")
public class ActionTest extends AbstractAction {

	public ActionTest(String name) {
		super(name);
	}

	@Override
	public boolean doAction() {
		System.out.println("Dummy action");
		return true;
	}

}
