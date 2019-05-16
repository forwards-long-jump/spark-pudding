package ch.sparkpudding.coreengine.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Useful functions to manipulate Streams
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class StreamTools {
	public static String read(InputStream stream) throws IOException {
		// we work on a copy in order to preserve the file stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream.transferTo(baos);
		
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		stream = new ByteArrayInputStream(baos.toByteArray());
		
		char[] buffer = new char[2048];
		Reader reader = new InputStreamReader(is);
		StringBuilder stringBuilder = new StringBuilder();
		int n;
		while ((n = reader.read(buffer)) > 0) {
			stringBuilder.append(buffer, 0, n);
		}
		return stringBuilder.toString();
	}
}
