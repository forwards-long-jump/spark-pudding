package ch.sparkpudding.sceneeditor;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Main {

	public static void main(String[] args) {
		try {
			new FrameSceneEditor(Main.class.getResource("/emptygame").getPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
