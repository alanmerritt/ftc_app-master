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
		
		
		initialize();
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		// --- Knock off balls. ---
		
		grabAndLift();
		
		blueKnockOff();
		
		// --- Place glyph. ---
		
		moveForward();
		
		sleep(500);
		
		rotate();
		
		sleep(500);
		
		moveToSide();
		
		sleep(500);
		
		moveForwardToColumn();
		
		sleep(500);
		
		lowerAndRelease();
		
		sleep(500);
		
		backup();
		
	}
	
	private void moveForward() {
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(-24 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void rotate() {
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > -85 && !isStopRequested()) {
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
				driveDistance = 18;
				break;
			case CENTER:
				driveDistance = 9;
				break;
			case LEFT:
			default:
				driveDistance = 0;
			
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
		setTargets(8 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
}
