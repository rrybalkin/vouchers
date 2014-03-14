package com.romansun.utils;

import java.io.File;

/**
 * Class contains additional useful methods for project
 * @author Roman Rybalkin
 *
 */
public class AdditionalUtils {
	
	/**
	 * Method for changing the first symbol of string to upper register
	 * @param str string which will be changed
	 * @return changed string with upper first symbol
	 */
	public static String upFirst(String str) {
		StringBuilder res = new StringBuilder(1000);
		if (str != null && !str.isEmpty()) {
			res.append(str.substring(0, 1).toUpperCase());
			res.append(str.substring(1).toLowerCase());
			
			return res.toString();
		} else {
			return "?";
		}
	}
	
	/**
	 * Method for creating unique file within folder; 
	 * If file with initial name already exists - name will be extended by suffix = " (i)"
	 * @param folder folder where file will be stored
	 * @param name name of report
	 * @param extension extension of file
	 * @return file with unique name within report folder
	 */
	public static File createUniqueFile(File folder, String name, String extension) {
		File file = new File(folder.getAbsolutePath() + "//" + name + "." + extension);
		int index = 1;
		while (file.exists()) {
			file = new File(folder.getAbsolutePath() + "//" + name + " (" + index++ + ")" + "." + extension);
		}
		
		return file;
	}
}