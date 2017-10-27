package org.firstinspires.ftc.teamcode;

//import java.util.Date;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MerrittAM on 10/21/2017.
 * Used to write data out to a log file.
 */

public class DataLogger {
	
	private String logFilePath;
	private FileOutputStream fileout;
	
	public DataLogger() {
		
		logFilePath = "/sdcard/FIRST/Debug/Logs/log.txt";
		
		try {
			
			fileout = new FileOutputStream(logFilePath, true);
			
		} catch(FileNotFoundException ex) {
			
		}
			
	}
	
	public void write(String message) {
		
		try {
			
			fileout.write(message.getBytes());
			
		} catch(IOException ex) {
			
		}
		
	}
	
	public void close() {
		
		try {
			
			fileout.close();
			
		} catch(IOException ex) {
			
		}
		
	}
	
}
