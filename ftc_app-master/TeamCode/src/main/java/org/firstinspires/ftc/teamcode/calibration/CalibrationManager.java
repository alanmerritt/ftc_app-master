package org.firstinspires.ftc.teamcode.calibration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MerrittAM on 10/21/2017.
 * Used to standardize data flow to and from the calibration file.
 */

public class CalibrationManager {
	
	//File path to the calibration file.
	private final static String CALIBRATION_FILE_PATH = "/sdcard/FIRST/Calibration.txt";
	
	private HashMap<String, String> data;
	
	/**
	 * Initializes the calibration manager.
	 */
	public CalibrationManager() {
		
		data = new HashMap<String, String>();
		loadData();
		
	}
	
	/**
	 * Loads the data from the calibration file.
	 * @return Whether or not the data read was successful.
	 */
	private boolean loadData() {
		
		try {
			
			//Open the file.
			FileInputStream filein = new FileInputStream(CALIBRATION_FILE_PATH);
			String allData = "";
			
			//Read all the data from the file.
			int c;
			while((c = filein.read()) != -1) {
				
				allData += (char)c;
				
			}
			
			//Close the file.
			filein.close();
			
			//An array of each row in the file.
			String[] rows = allData.split("\n");
			
			
			
			//Each row consists of two parts, a key and a value in the form key:value.
			for(String s : rows) {
				
				//Split the data into two parts.
				String[] splitRow = s.split(":");
				
				if(splitRow.length > 1) {
					
					//Make sure the data does not contain duplicates.
					if (!data.containsKey(splitRow[0])) {
						
						//Add the data to the hash map.
						data.put(splitRow[0], splitRow[1]);
						
					}
					
				}
				
			}
			
		} catch (FileNotFoundException ex) {
			
			//TODO: Create new file if one isn't found.
			return false;
			
		} catch (IOException ex) {
			
			return false;
			
		}
		
		return true;
		
	}
	
	/**
	 * Returns the value of the specified key.
	 * @param key The key of the value to get.
	 * @return The value; null if the key does not exist.
	 */
	public String get(String key) {
		
		//Check for the key and return the value.
		if(data.containsKey(key)) {
			return data.get(key);
		} else {
			return "";
		}
		
	}
	
	public void add(String key, String value) {
		
		if(!data.containsKey(key)) {
			
			data.put(key, value);
			
		}
		
	}
	
	public void save() {
		
		try {
			
			
			FileOutputStream fileout = new FileOutputStream(CALIBRATION_FILE_PATH);
			String dataToBeWritten = "";
			for(Map.Entry entry : data.entrySet()) {
				
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				dataToBeWritten += key + ":" + value + "\n";
				
			}
			
			//Remove the trailing linebreak.
			dataToBeWritten = dataToBeWritten.substring(0, dataToBeWritten.length()-1);
			
			fileout.write(dataToBeWritten.getBytes());
			
			fileout.close();
			
		} catch (FileNotFoundException ex) {
			
		} catch (IOException ex) {
			
		}
			
	}
	//TODO: Add methods for adding and saving data.
	
}
