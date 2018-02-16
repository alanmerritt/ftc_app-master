package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;

/**
 * Created by MerrittAM on 1/28/2018.
 * Calibration program for the blue, backside autonomous.
 */
@Autonomous(name = "Auto_Blue_Backside_Calibration", group = "Calibration")
public class Auto_Blue_Backside_Calibration extends Auto_Calibration {
	
	
	
	public void runOpMode() throws InterruptedException {
		
		initialize("BlueBacksideCalibration");
		
		waitForStart();
		
		runCalibration();
		
		manager.save();
		
		
	}
	
	void addCalibrationValues(ArrayList<CalibrationValue> list) {
		
		final double valueChange = .5;
		
		//Distance to drive off the platform.
		list.add(new CalibrationValue(gamepad1, "DriveOffPlatformDistance") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 40) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= 0) {
					value -= valueChange;
				}
				
				telemetry.addLine("Distance to drive off platform.");
				telemetry.addData("Distance", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		//How far to turn.
		list.add(new CalibrationValue(gamepad1, "RotationValue") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 180) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= -180) {
					value -= valueChange;
				}
				
				telemetry.addLine("Amount to rotate to face box.");
				telemetry.addData("Rotation", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		//How far to move to reach the left column.
		list.add(new CalibrationValue(gamepad1, "LeftColDistance") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 40) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= 0) {
					value -= valueChange;
				}
				
				telemetry.addLine("Distance to the left column.");
				telemetry.addData("Distance", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		//How far to move to reach the center column.
		list.add(new CalibrationValue(gamepad1, "CenterColDistance") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 40) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= 0) {
					value -= valueChange;
				}
				
				telemetry.addLine("Distance to center column.");
				telemetry.addData("Distance", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		//How far to move to reach the right column.
		list.add(new CalibrationValue(gamepad1, "RightColDistance") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 40) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= 0) {
					value -= valueChange;
				}
				
				telemetry.addLine("Distance to right column.");
				telemetry.addData("Distance", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		//How far to drive to reach the box.
		list.add(new CalibrationValue(gamepad1, "DriveToBoxDistance") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 20) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= 0) {
					value -= valueChange;
				}
				
				telemetry.addLine("Distance to the box.");
				telemetry.addData("Distance", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		list.add(new CalibrationValue(gamepad1, "FaceRelicRotation") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 180) {
					value += valueChange;
				}
				if(downButtonClicked() && value >= -180) {
					value -= valueChange;
				}
				
				telemetry.addLine("Face relic rotation.");
				telemetry.addData("Rotation", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
			}
		});
		
	}
	
}
