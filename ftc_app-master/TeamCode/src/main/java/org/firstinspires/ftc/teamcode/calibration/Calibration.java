package org.firstinspires.ftc.teamcode.calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
		final DcMotor leftLift = hardwareMap.dcMotor.get("leftLift");
		leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		final DcMotor rightLift = hardwareMap.dcMotor.get("rightLift");
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		CalibrationManager manager = new CalibrationManager(telemetry);
		
		waitForStart();
		
		
		new CalibrationValue(gamepad1, "lServoOpen") {
			protected void changeValue() {
				
				if (upButtonClicked() && value <= 1) {
					value += 0.05;
				}
				if (downButtonClicked() && value >= 0) {
					value -= 0.05;
				}
				
				leftGripper.setPosition(value);
				
				
				telemetry.addLine("Left servo open position.");
				telemetry.addData("Value", value);
				telemetry.update();
				
			}
		}.calibrate(manager);
		
		sleep(500);
		
		new CalibrationValue(gamepad1, "lServoClose") {
			protected void changeValue() {

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
		}.calibrate(manager);
		
		sleep(500);
		
		new CalibrationValue(gamepad1, "rServoOpen") {
			protected void changeValue() {

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
		}.calibrate(manager);
		
		sleep(500);
		
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
		}.calibrate(manager);
		
		sleep(500);
		
		new CalibrationValue(gamepad1, "liftPower") {
			protected void changeValue() {

				if(upButtonClicked() && value <= 1) {
					value += .1;
				} else if(downButtonClicked() && value >= 0) {
					value -= .1;
				}

				if(gamepad1.right_trigger != 0) {
					rightLift.setPower(-value);
					leftLift.setPower(-value);
				} else if(gamepad1.left_trigger != 0) {
					rightLift.setPower(value);
					leftLift.setPower(value);
				} else {
					rightLift.setPower(0);
					leftLift.setPower(0);
				}

				telemetry.addLine("Lift power.");
				telemetry.addData("Value", value);
				telemetry.update();

			}
		}.calibrate(manager);

		manager.save();
		telemetry.addLine("Data saved.");
		telemetry.update();
		
		sleep(5000);
		
	}
	
}
