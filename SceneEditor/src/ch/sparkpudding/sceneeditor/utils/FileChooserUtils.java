package ch.sparkpudding.sceneeditor.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Utils for the file chooser with .lel files
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 30 May 2019
 *
 */
public class FileChooserUtils {
	
	/**
	 * Obtain the extension of the selected file
	 * 
	 * @param f the file wich extension need to be get
	 * @return the file extensions
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i < 0) {
			return "";
		}

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * Prepare the filter for files in the file chooser
	 * 
	 * @return filter for .lel files 
	 */
	public static FileFilter getFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				if (getExtension(f).equals("lel")) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			public String getDescription() {
				return "Lel files";
			}
		};
	}
}
