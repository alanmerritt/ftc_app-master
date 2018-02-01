package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;

/**
 * Created by MerrittAM on 1/29/2018.
 */

public abstract class Auto_Calibration extends LinearOpMode {
	
	protected CalibrationManager manager;
	ArrayList<CalibrationValue> valueList;
	protected int currentCalibrationValue;
	protected boolean leftPressedLast;
	protected boolean rightPressedLast;
	
	protected void initialize(String calibrationFileName) {
		
		//Create a new calibration manager. Save the data to a non-default location.
		manager = new CalibrationManager(telemetry,
				calibrationFileName);
		
		//Create a list of calibration values.
		valueList = new ArrayList<>();
		addCalibrationValues(valueList);
		
		telemetry.addLine(valueList.size() + " values added.");
		telemetry.update();
		
		//Current value to edit.
		currentCalibrationValue = 0;
		
		leftPressedLast = false;
		rightPressedLast = false;
		
	}
	
	void runCalibration() {
		
		//Exit when be is pressed.
		while(!gamepad1.b)
		{
			
			boolean leftPressedFirst = gamepad1.dpad_left;
			boolean rightPressedFirst = gamepad1.dpad_right;
			
			//Edit the current value is x is pressed.
			if(gamepad1.x)
			{
				valueList.get(currentCalibrationValue).calibrate(manager);
			}
			
			//Cycle through the values.
			if(leftPressedFirst && !leftPressedLast)
			{
				currentCalibrationValue--;
				telemetry.addLine("Decrement");
				if(currentCalibrationValue < 0)
				{
					currentCalibrationValue = valueList.size()-1;
					telemetry.addLine("To end");
				}
			}
			if(rightPressedFirst && !rightPressedLast)
			{
				currentCalibrationValue++;
				telemetry.addLine("Increment");
				if(currentCalibrationValue >= valueList.size())
				{
					currentCalibrationValue = 0;
					telemetry.addLine("To beginning");
				}
			}
			
			
			//Display some helpful information.
			telemetry.addLine(valueList.get(currentCalibrationValue).getValueName());
			
			telemetry.addLine();
			telemetry.addLine("Press dpad left or right to switch values.");
			telemetry.addLine("Press x to edit the current value.");
			telemetry.addLine("Press b to end calibration.");
			
			telemetry.addData("Current value", currentCalibrationValue);
			telemetry.addData("DPad right", gamepad1.dpad_right);
			telemetry.addData("DPad right last", rightPressedLast);
			telemetry.addData("DPad left", gamepad1.dpad_left);
			telemetry.addData("Ddpad left last", leftPressedLast);
			
			telemetry.update();
			
			leftPressedLast = leftPressedFirst;
			rightPressedLast = rightPressedFirst;
			
		}
		
	}
	
	abstract void addCalibrationValues(ArrayList<CalibrationValue> list);
	
}
