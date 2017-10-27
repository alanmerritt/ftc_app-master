package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by MerrittAM on 10/20/2017.
 * OpMode used for calibrating parts of the robot without reinstalling the program.
 */
@TeleOp(name = "Calibration", group = "TeleOp")
public class Calibration extends LinearOpMode {


	@Override
	public void runOpMode() throws InterruptedException {
		
		
		final Servo leftGripper = hardwareMap.servo.get("leftGripper");
		final Servo rightGripper = hardwareMap.servo.get("rightGripper");
		
		CalibrationManager manager = new CalibrationManager();
		
		waitForStart();
		
		
		new CalibrationValue(gamepad1, "lServoOpen") {
			public void changeValue() {
				
				if (upButtonClicked() && value <= 1) {
					value += 0.05;
				} else if (downButtonClicked() && value >= 0) {
					value -= 0.05;
				}
				
				leftGripper.setPosition(value);
				
				telemetry.addLine("Left servo open position.");
				telemetry.addData("Value", value);
				telemetry.update();
				
			}
		}.calibrate(manager, telemetry);
		/*
		new CalibrationValue(gamepad1, "lServoClose") {
			public void changeValue() {
				
				if (upButtonClicked() && value <= 1) {
					value += 0.05;
				} else if (downButtonClicked() && value >= 0) {
					value -= 0.05;
				}
				
				leftGripper.setPosition(value);
				
				telemetry.addLine("Left servo close position.");
				telemetry.addData("Value", value);
				telemetry.update();
				
			}
		};
		
		new CalibrationValue(gamepad1, "rServoOpen") {
			public void changeValue() {
				
				if (upButtonClicked() && value <= 1) {
					value += 0.05;
				} else if (downButtonClicked() && value >= 0) {
					value -= 0.05;
				}
				
				rightGripper.setPosition(value);
				
				telemetry.addLine("Right servo open position.");
				telemetry.addData("Value", value);
				telemetry.update();
				
			}
		};
		
		new CalibrationValue(gamepad1, "rServoClose") {
			public void changeValue() {
				
				if (upButtonClicked() && value <= 1) {
					value += 0.05;
				} else if (downButtonClicked() && value >= 0) {
					value -= 0.05;
				}
				
				rightGripper.setPosition(value);
				
				telemetry.addLine("Right servo close position.");
				telemetry.addData("Value", value);
				telemetry.update();
				
			}
		};
		*/
		manager.save();
		
	}
	
}
