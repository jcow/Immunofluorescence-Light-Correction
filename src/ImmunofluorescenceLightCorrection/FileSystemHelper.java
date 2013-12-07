
package ImmunofluorescenceLightCorrection;

import java.io.File;

public class FileSystemHelper {
	
	/**
	 * Gets the directory, filename, and file extension of a file path
	 * @param path 
	 *		The path to the file
	 * @return 
	 */
	public static String[] getFileCharacteristics(String path){
		
		String[] parts;
		if(path.indexOf("/") != -1){
			parts = path.split("/");
		}
		else{
			parts = path.split("\\\\");
		}
		
		String directory = "";
		for(int i = 0; i < parts.length-1; i++){
			directory += parts[i];
			if(i != parts.length-2){
				directory += '/';
			}
		}
		String filename = parts[parts.length-1];
		int periodIndex = filename.indexOf(".");
		if(periodIndex != -1){
			filename = filename.substring(0, periodIndex);
		}
		
		String[] ret = new String[3];
		ret[0] = directory;
		ret[1] = filename;
		ret[2] = path.substring(path.length()-3, path.length());
		return ret;
	}
	
	/**
	 * Gets a listing of all the files within a directory
	 * @param directory
	 *		The files in the directory
	 * @return 
	 */
	public static String[] getFilesWithinDirectory(String directory){
		if(directory.compareTo("") == 0){
			return null;
		}
		
		final File folder = new File(directory);
		int fileCounter = 0;
		for (final File fileEntry : folder.listFiles()) {
			if(fileEntry.isFile()){
				fileCounter++;
			}
		}
		
		String[] files = new String[fileCounter];
		fileCounter = 0;
		for (final File fileEntry : folder.listFiles()) {
			if(fileEntry.isFile()){
				files[fileCounter] = fileEntry.getAbsolutePath();
				fileCounter++;
			}
		}
		
		return files;
	}
}
