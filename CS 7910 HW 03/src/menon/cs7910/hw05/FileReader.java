package menon.cs7910.hw05;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class FileReader {

	/**
	 * @param folderName
	 * @return a collection of file names in the input folder parameter
	 */
	public static Collection<String> getFileNames(String folderName) {
		
		Collection<String> returnValue = new ArrayList<String>();
		
		if (folderName != null && folderName.trim().length() > 0) {
			
			File folder = new File(folderName.trim());
			if (folder.isDirectory()) {
				
				File[] listOfFiles = folder.listFiles();
				for (File file : listOfFiles) {
					returnValue.add(file.getName());
				}
				
			}
		
		}
		
		return returnValue;
		
	}
	
}
