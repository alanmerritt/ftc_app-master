package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 11/14/2017.
 */
@Autonomous(name = "Auto_Blue_RelicSide", group = "Autonomous")
public class Auto_Blue_RelicSide extends Auto {
	@Override
	public void runOpMode() throws InterruptedException {
		
		
		initialize("BlueRelicsideCalibration");
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		// --- Knock off balls. ---
		
		grabAndLift();
		
		blueKnockOff();
		
		// --- Place glyph. ---
		
		maintainHeight();
		sleep(500);
		
		quickRaise();
		
		moveForward();
		
		//sleep(500);
		quickRaise();
		
		rotate();
		
//		sleep(500);
		
		quickRaise();
		
		moveToSide();
		
//		sleep(500);
		
		quickRaise();
		
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
		setTargets(-dist * INCH); //24
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void rotate() {
		
		int rot  = (int)Double.parseDouble(specificManager.get("RotationValue"));
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > rot && !isStopRequested()) { // -85
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			driveMotors(.3, -.3, -.3, .3);
		}
		stopDriveMotors();
		
	}
	
	private void moveToSide() {
		
		int driveDistance;
		switch(vuMark) {
			
			case RIGHT:
				driveDistance = (int)Double.parseDouble(specificManager.get("RightColDistance")); //18
				break;
			case CENTER:
				driveDistance = (int)Double.parseDouble(specificManager.get("CenterColDistance")); //9
				break;
			case LEFT:
			default:
				driveDistance = (int)Double.parseDouble(specificManager.get("LeftColDistance")); //0
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition(driveDistance * INCH);
		frontRight.setTargetPosition(-driveDistance * INCH);
		backRight.setTargetPosition(driveDistance * INCH);
		backLeft.setTargetPosition(-driveDistance * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
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
	
}
