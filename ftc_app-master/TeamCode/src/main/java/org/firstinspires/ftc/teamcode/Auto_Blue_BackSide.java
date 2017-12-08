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
		
		
		initialize();
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		grabAndLift();
		
		blueKnockOff();
		
		moveForward();
		sleep(500);
		
		rotate();
		sleep(500);
		
		moveToSide();
		
		sleep(500);
		
		moveForwardToColumn();
		
		sleep(1000);
		
		lowerAndRelease();
		
		sleep(1000);
		
		backup();
		
		sleep(500);
		
		
	}
	
	private void moveForward() {
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(-23 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void rotate() {
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > -175 && !isStopRequested()) {
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
				driveDistance = 25; //TODO:Move forward more.
				break;
			case CENTER:
				driveDistance = 16;
				break;
			case LEFT:
			default:
				driveDistance = 8;
			
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
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(9 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
}
