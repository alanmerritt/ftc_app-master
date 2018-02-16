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
		
		faceRelic();
		
	}
	
	private void firstMove() {
		
		double dist = Double.parseDouble(specificManager.get("DriveOffPlatformDistance"));
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets((int)(dist * INCH)); //18
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
	}
	
	private void moveToSide() {
		
		double driveDistance;
		switch(vuMark) {
			
			case LEFT:
				driveDistance = Double.parseDouble(specificManager.get("LeftColDistance")); //24
				break;
			case CENTER:
				driveDistance = Double.parseDouble(specificManager.get("CenterColDistance")); //12
				break;
			case RIGHT:
			default:
				driveDistance = Double.parseDouble(specificManager.get("RightColDistance")); //5
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition(-(int)(driveDistance * INCH));
		frontRight.setTargetPosition((int)(driveDistance * INCH));
		backRight.setTargetPosition(-(int)(driveDistance * INCH));
		backLeft.setTargetPosition((int)(driveDistance * INCH));
		driveMotors(.5, .5, .5, .5);
		while (frontLeft.isBusy() && !isStopRequested()) ;
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
		while(yaw < -rot && !isStopRequested()) { //-85
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			driveMotors(-.3, .3, .3, -.3);
		}
		stopDriveMotors();
		
	}
	
}

