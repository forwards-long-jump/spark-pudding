package testengine;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.CoreEngine;

public class Main {

	public static void main(String[] args) {
		try {
			new CoreEngine(new JPanel(), Main.class.getResource("/testgame").getPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
