package ch.sparkpudding.sceneeditor.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileChooserUtils {
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i < 0)
			return "";

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public static FileFilter getFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				if (getExtension(f).equals("lel"))
					return true;
				else
					return false;
			}

			@Override
			public String getDescription() {
				return "Lel files";
			}
		};
	}
}
