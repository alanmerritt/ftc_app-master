package org.firstinspires.ftc.teamcode.calibration;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MerrittAM on 10/21/2017.
 * Used to standardize data flow to and from the calibration file.
 */

public class CalibrationManager {
	
	//File path to the calibration file.
	private String calibrationFilePath;
	
	private HashMap<String, String> data;
	
	private Telemetry telemetry;
	
	/**
	 * Initializes the calibration manager.
	 */
	public CalibrationManager(Telemetry telemetry) {
		
		this.telemetry = telemetry;
		data = new HashMap<String, String>();
		 calibrationFilePath = "/sdcard/FIRST/Calibration.txt";
		loadData();
		
	}
	
	public CalibrationManager(Telemetry telemetry, String filename) {
		
		this.telemetry = telemetry;
		data = new HashMap<String, String>();
		calibrationFilePath = "/sdcard/FIRST/" + filename + ".txt";
		loadData();
		
	}
	
	/**
	 * Loads the data from the calibration file.
	 * @return Whether or not the data read was successful.
	 */
	private boolean loadData() {
		
		try {
			
			//Open the file.
			FileInputStream filein = new FileInputStream(calibrationFilePath);
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
			
			try {
				new FileOutputStream(calibrationFilePath, false).close();
			} catch(FileNotFoundException e) {
				telemetry.addLine("New file could not be created.");
			} catch(IOException e) {
				
			}
			return false;
			
		} catch (IOException ex) {
			
			return false;
			
		}
		
		return true;
		
	}
	
	/**
	 * Returns the value of the specified key.
	 * @param key The key of the value to get.
	 * @return The value; or an empty string if the key does not exist.
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
		
		data.put(key, value);
		
	}
	
	public void save() {
		
		try {
			
			
			FileOutputStream fileout = new FileOutputStream(calibrationFilePath);
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
			
			telemetry.addLine("File could not be found.");
			telemetry.addLine(ex.toString());
			telemetry.update();
			
		} catch (IOException ex) {
			
			telemetry.addLine("IO Exception thrown.");
			telemetry.addLine(ex.toString());
			telemetry.update();
			
		}
			
	}
	
}
