package br.ufes.inf.nemo.ontouml2db.casting.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class CastType {
	
	private HashMap<String, String> types;
	
	public CastType(String fileTraceability) {
		types = new HashMap<String, String>();
		
		String path = getPath(fileTraceability);
		
		putTypes(path);
	}
	
	private String getPath(String fileTraceability) {
		String curDir = System.getProperty("user.dir");
		return curDir + "\\resources\\typeTraceability\\" + fileTraceability;
	}
	
	private void putTypes(String path) {
		String vector[];
		
		try {
			File sourceFile = new File(path);
			
			if (!sourceFile.isFile()) {
				System.out.println("Error accessing: " + sourceFile.getAbsolutePath());
				System.exit(1);
			}
			Scanner myReader = new Scanner(sourceFile);
			
			while (myReader.hasNextLine()) {
				vector = myReader.nextLine().split("=");
				types.put(vector[0], vector[1]);
			}
			myReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();	
		}
	}
	
	public String getCorrespondentFrom(String sourceType) {
		String result = types.get(sourceType.toUpperCase());
		
		if( result == null ) {
			result = "["+sourceType +" type is not tracked]";
		}
		
		return result;
	}
}
