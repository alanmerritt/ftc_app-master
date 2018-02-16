package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 11/14/2017.
 */
@Autonomous(name = "Auto_Blue_Backside", group = "Autonomous")
public class Auto_Blue_BackSide extends Auto {
	@Override
	public void runOpMode() throws InterruptedException {
		
		
		initialize("BlueBacksideCalibration");
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		grabAndLift();
		
		blueKnockOff();
		
		quickRaise();
		
		moveForward();
//		sleep(500);
		quickRaise();
		
		rotate();
//		sleep(500);
		quickRaise();
		
		moveToSide();
		
//		sleep(500);
		quickRaise();
		
		moveForwardToColumn();
		
		sleep(1000);
		
		lowerAndRelease();
		
		sleep(1000);
		
		backup();
		
		sleep(500);
		
		//faceRelic();
		
	}
	
	private void moveForward() {
		
		double dist = Double.parseDouble(specificManager.get("DriveOffPlatformDistance"));
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets((int)(-dist * INCH)); //23
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void rotate() {
		
		double rot = Double.parseDouble(specificManager.get("RotationValue"));
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > -rot && !isStopRequested()) { //175
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			telemetry.addData("Yaw", yaw);
			driveMotors(.3, -.3, -.3, .3);
		}
		stopDriveMotors();
		
	}
	
	private void moveToSide() {
		
		double driveDistance;
		switch(vuMark) {
			
			case RIGHT:
				driveDistance = Double.parseDouble(specificManager.get("RightColDistance")); //24
				break;
			case CENTER:
				driveDistance = Double.parseDouble(specificManager.get("CenterColDistance")); //16
				break;
			case LEFT:
			default:
				driveDistance = Double.parseDouble(specificManager.get("LeftColDistance")); //8
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition((int)(driveDistance * INCH));
		frontRight.setTargetPosition((int)(-driveDistance * INCH));
		backRight.setTargetPosition((int)(driveDistance * INCH));
		backLeft.setTargetPosition((int)(-driveDistance * INCH));
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void moveForwardToColumn() {
		
		double dist = Double.parseDouble(specificManager.get("DriveToBoxDistance"));
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets((int)(dist * INCH)); //9
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void faceRelic() {
		
		double rot = Double.parseDouble(specificManager.get("FaceRelicRotation"));
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw < rot && !isStopRequested()) { //-85
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			driveMotors(-.3, .3, .3, -.3);
		}
		stopDriveMotors();
		
	}
	
}
