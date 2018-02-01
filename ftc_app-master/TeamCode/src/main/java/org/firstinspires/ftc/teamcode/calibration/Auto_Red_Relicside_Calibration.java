package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;

/**
 * Created by MerrittAM on 1/31/2018.
 * Calibration program for the red relic side autonomous.
 */
@Autonomous(name = "Auto_Red_Relicside_Calibration", group = "Calibration")
public class Auto_Red_Relicside_Calibration extends Auto_Calibration {
	
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize("RedRelicsideCalibration");
		
		waitForStart();
		
		runCalibration();
		
		manager.save();
		
		
	}
	
	@Override
	void addCalibrationValues(ArrayList<CalibrationValue> list) {
		
		
		//Distance to drive off the platform.
		list.add(new CalibrationValue(gamepad1, "DriveOffPlatformDistance") {
			@Override
			protected void changeValue() {
				
				if(upButtonClicked() && value <= 40) {
					value += 1;
				}
				if(downButtonClicked() && value >= 0) {
					value -= 1;
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
					value += 1;
				}
				if(downButtonClicked() && value >= -180) {
					value -= 1;
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
					value += 1;
				}
				if(downButtonClicked() && value >= 0) {
					value -= 1;
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
					value += 1;
				}
				if(downButtonClicked() && value >= 0) {
					value -= 1;
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
					value += 1;
				}
				if(downButtonClicked() && value >= 0) {
					value -= 1;
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
					value += 1;
				}
				if(downButtonClicked() && value >= 0) {
					value -= 1;
				}
				
				telemetry.addLine("Distance to the box.");
				telemetry.addData("Distance", value);
				telemetry.addLine();
				telemetry.addLine("Press A to continue.");
				
				telemetry.update();
				
			}
		});
		
		
	}
	
}
