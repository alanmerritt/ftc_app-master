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
	
	private Gamepad gamepad;
	private boolean aPressedLast = false;
	private boolean upPressedLast = false;
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
	public abstract void changeValue();
	
	/**
	 * Determines if the dpad up button has been clicked.
	 * @return Whether or not the button has been clicked.
	 */
	public boolean upButtonClicked() {
		if(gamepad.dpad_up && !upPressedLast) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if the dpad down button has been clicked.
	 * @return Whether or not the button has been clicked.
	 */
	public boolean downButtonClicked() {
		if(gamepad.dpad_down && !downPressedLast) {
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
	public final void calibrate(CalibrationManager manager, Telemetry t) {
		
		//Loop until the a button is clicked.
		while(gamepad.a && !aPressedLast) {
			
			//Run user implementation.
			changeValue();
			
			t.addLine("Calibrating.");
			t.addData("A", gamepad.a);
			t.addData("A last", !aPressedLast);
			
			//Update click values.
			aPressedLast = gamepad.a;
			upPressedLast = gamepad.dpad_up;
			downPressedLast = gamepad.dpad_down;
			
		}
		
		//Add the value to the CalibrationManager.
		manager.add(valueName, Double.toString(value));
		
	}
	
}
