package ch.sparkpudding.sceneeditor.action;

import java.util.EventListener;

public interface HistoryEventListener extends EventListener {

	public void historyEvent(int stackPointer, int stackSize);

}
