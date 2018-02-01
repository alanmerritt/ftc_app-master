package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 11/13/2017.
 * Red autonomous for the back side (side away from the relic drop zone).
 */
@Autonomous(name = "Auto_Red_BackSide", group = "Autonomous")
public class Auto_Red_BackSide extends Auto {
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize("RedBacksideCalibration");
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		sleep(1000);
		
		grabAndLift();
		
		redKnockOff();
		
		//moveToFirstColumn();
		firstMove();
		
		maintainHeight();
		sleep(500);
		maintainHeight();
		
		//Move to the side depending on which VuMark is seen.
		moveToSide();
		
		maintainHeight();
		sleep(500);
		maintainHeight();
		
		moveForwardToColumn();
		
		lowerAndRelease();
		
		backup();
		
		sleep(500);
		
		//TODO: Rotate to face blocks.
		
	}
	
	private void firstMove() {
		
		int dist = (int)Double.parseDouble(specificManager.get("DriveOffPlatformDistance"));
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(dist * INCH); //18
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void moveToSide() {
		
		int driveDistance;
		switch(vuMark) {
			
			case LEFT:
				driveDistance = (int)Double.parseDouble(specificManager.get("LeftColDistance")); //24
				break;
			case CENTER:
				driveDistance = (int)Double.parseDouble(specificManager.get("CenterColDistance")); //12
				break;
			case RIGHT:
			default:
				driveDistance = (int)Double.parseDouble(specificManager.get("RightColDistance")); //5
			
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
		setTargets(dist * INCH); //9
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
}

