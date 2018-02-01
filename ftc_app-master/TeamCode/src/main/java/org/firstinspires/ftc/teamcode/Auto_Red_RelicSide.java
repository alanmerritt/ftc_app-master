package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 11/9/2017.
 */
@Autonomous(name = "Auto_Red_RelicSide", group = "Autonomous")
public class Auto_Red_RelicSide extends Auto {
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize("RedRelicsideCalibration");
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		// --- Knock off balls. ---
		
		grabAndLift();
		
		redKnockOff();
		
		// --- Place glyph. ---
		
		maintainHeight();
		sleep(500);
		leftLift.setPower(0);
		rightLift.setPower(0);
		
		moveForward();
		
		sleep(500);
		
		rotate();
		
		maintainHeight();
		sleep(500);
		maintainHeight();
		
		moveToSide();
		
		maintainHeight();
		sleep(500);
		maintainHeight();
		
		moveForwardToColumn();
		
		sleep(500);
		
		lowerAndRelease();
		
		sleep(500);
		
		backup();
		
	}
	
	private void moveForward() {
		
		int dist = (int)Double.parseDouble(specificManager.get("DriveOffPlatformDistance"));
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(dist * INCH); //22
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
	
	}
	
	private void rotate() {
		
		int rot = (int)Double.parseDouble(specificManager.get("RotationValue"));
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > rot && !isStopRequested()) { //-85
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			driveMotors(.3, -.3, -.3, .3);
		}
		stopDriveMotors();
		
	}
	
	private void moveToSide() {
		
		int driveDistance;
		switch(vuMark) {
			
			case LEFT:
				driveDistance = (int)Double.parseDouble(specificManager.get("LeftColDistance")); //14
				break;
			case CENTER:
				driveDistance = (int)Double.parseDouble(specificManager.get("CenterColDistance")); //5
				break;
			case RIGHT:
			default:
				driveDistance = (int)Double.parseDouble(specificManager.get("RightColDistance")); //3
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition(-driveDistance * INCH);
		frontRight.setTargetPosition(driveDistance * INCH);
		backRight.setTargetPosition(-driveDistance * INCH);
		backLeft.setTargetPosition(driveDistance * INCH);
		driveMotors(.5, .5, .5, .5);
		while (frontLeft.isBusy() && !isStopRequested()) ;
		stopDriveMotors();
		
	}
	
	private void moveForwardToColumn() {
		
		int dist = (int)Double.parseDouble(specificManager.get("DriveToBoxDistance"));
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(dist * INCH); //8
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	@Deprecated
	private void moveToFirstColumn() {
		
		telemetry.addLine("Driving forward.");
		telemetry.update();
		driveMotors(.4, .4, .4, .4);
		sleep(1000);
		
		telemetry.addLine("Stopping.");
		telemetry.update();
		stopDriveMotors();
		sleep(500);
		
		double yaw = gyro.getYaw();
		while(yaw > -45) {
			
			telemetry.addLine("Rotating.");
			telemetry.addData("Yaw", yaw);
			telemetry.update();
			
			driveMotors(.35, -.35, -.35, .35);
			yaw = gyro.getYaw();
		}
		
		stopDriveMotors();
		
	}
	
}
