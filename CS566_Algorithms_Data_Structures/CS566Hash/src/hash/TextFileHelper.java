package hash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFileHelper {
	public static int[] readLines(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int lineCount = Integer.parseInt(bufferedReader.readLine());
		String line;
		int[] outputArray = new int[lineCount];
		int i = 0;
		
		while ((line = bufferedReader.readLine()) != null) {
			outputArray[i++] = Integer.parseInt(line);
		}
		
		bufferedReader.close();
		
		return outputArray;
	}
}
