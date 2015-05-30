package menon.cs7910.hw03;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileReader {
	
	public static List<String> getLinesFromTextFile(String textFilePath) throws FileNotFoundException {
		
		BufferedReader textFileReader = new BufferedReader(new FileReader(textFilePath));
		
		String lines = null;
		List<String> returnValue = new ArrayList<String>();
		try {
			lines = textFileReader.readLine();
			while(lines != null) {
				if (lines.trim().length() > 0) {
					returnValue.add(lines.trim());
				}
				lines = textFileReader.readLine();
			}
			textFileReader.close();
		} catch (FileNotFoundException e) {
			System.err.println("File " + textFilePath + " was not found.");
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			System.err.println("IOException thrown while reading file " + textFilePath + ".");
			e.printStackTrace();
			return null;
		}
		
		return returnValue;
	}
}

