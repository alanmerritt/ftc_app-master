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
		
		initialize();
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		sleep(1000);
		
		grabAndLift();
		
		redKnockOff();
		
		//moveToFirstColumn();
		firstMove();
		sleep(500);
		//Move to the side depending on which VuMark is seen.
		moveToSide();
		sleep(500);
		moveForwardToColumn();
		sleep(500);
		lowerAndRelease();
		
		backup();
		
		sleep(500);
		
		//TODO: Rotate to face blocks.
		
	}
	
	private void firstMove() {
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(18 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void moveToSide() {
		
		int driveDistance;
		switch(vuMark) {
			
			case LEFT:
				driveDistance = 24;
				break;
			case CENTER:
				driveDistance = 12;
				break;
			case RIGHT:
			default:
				driveDistance = 5;
			
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
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(9 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
}

