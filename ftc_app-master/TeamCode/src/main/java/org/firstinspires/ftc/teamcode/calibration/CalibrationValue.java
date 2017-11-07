package org.firstinspires.ftc.teamcode.calibration;


import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by MerrittAM on 10/24/2017.
 * Create a subclass and implement code to
 * change a value, which will be passed to
 * a CalibrationManager object.
 */
public abstract class CalibrationValue {
	
	protected Gamepad gamepad;
	private boolean aPressedFirst = false;
	private boolean aPressedLast = false;
	
	private boolean upPressedFirst = false;
	private boolean upPressedLast = false;
	
	private boolean downPressedFirst = false;
	private boolean downPressedLast = false;
	/**The value that will be saved.*/
	protected double value = 0;
	private String valueName;
	
	/**
	 * Initializes the CalibrationValue.
	 * @param gamepad The gamepad used for input.
	 * @param valueName The name of the value to be saved.
	 */
	public CalibrationValue(Gamepad gamepad, String valueName) {
		
		this.gamepad = gamepad;
		this.valueName = valueName;
		
	}
	
	/**
	 * User implementation to save a calibration value.
	 */
	protected abstract void changeValue();
	
	/**
	 * Determines if the dpad up button has been clicked.
	 * @return Whether or not the button has been clicked.
	 */
	public boolean upButtonClicked() {
		if(upPressedFirst && !upPressedLast) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if the dpad down button has been clicked.
	 * @return Whether or not the button has been clicked.
	 */
	public boolean downButtonClicked() {
		if(downPressedFirst && !downPressedLast) {
			return true;
		}
		return false;
	}
	
	/**
	 * Runs the user implementation until the a button
	 * on the gamepad is pressed, after which, it stores
	 * value in the CalibrationManager object parameter.
	 * @param manager The CalibrationManager object used for saving the value.
	 */
	public final void calibrate(CalibrationManager manager) {
		
		boolean exitLoop = false;
		
		//Try loading the existing value for this CalibrationValue.
		//If there is one, set value to it, otherwise, set value to 0.
		String previousValue = manager.get(valueName);
		if(!previousValue.equals("")) {
			value = Double.parseDouble(previousValue);
		} else {
			value = 0;
		}
		
		//Loop until the a button is clicked.
		while(!exitLoop) {
			
			aPressedFirst = gamepad.a;
			upPressedFirst = gamepad.dpad_up;
			downPressedFirst = gamepad.dpad_down;
			
			//Run user implementation.
			changeValue();
			
			exitLoop = aPressedFirst && !aPressedLast;
			
			//Update click values.
			aPressedLast = aPressedFirst;
			upPressedLast = upPressedFirst;
			downPressedLast = downPressedFirst;
			
		}
		
		//Add the value to the CalibrationManager.
		manager.add(valueName, Double.toString(value));
		
	}
	
}
