import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.sparkpudding.coreengine.CoreEngine;

public class Main {
	static CoreEngine ce = null;

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.setTitle(args[0]);
		jframe.setSize(1280, 800);

		jframe.setVisible(true);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		try {
			System.out.println("Loading game " + args[0]);
			ce = new CoreEngine(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		jframe.add(ce);
		
		ce.setFocusable(true);
		ce.requestFocus();

		jframe.setVisible(true);
	}
}
