package ch.sparkpudding.coreengine;

public class CoreEngine {

	private double msPerUpdate = (1000 / 60);

	private boolean exit = false;

	public CoreEngine() {
		new Thread(() -> {
			startGame();
		}).start();
	}

	private void startGame() {
		double previous = System.currentTimeMillis();
		double lag = 0.0;

		while (!exit) {
			double current = System.currentTimeMillis();
			double elapsed = current - previous;

			previous = current;
			lag += elapsed;

			while (lag >= msPerUpdate) {
				update();
				lag -= msPerUpdate;
			}

			render();
		}
	}

	private void update() {
		// TODO: Update logic
	}

	private void render() {
		// TODO: Render logic
	}
}
