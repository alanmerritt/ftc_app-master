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
		
		initialize();
		
		relicTrackables.activate();
		
		waitForStart();
		
		scanVuMark(1000);
		
		// --- Knock off balls. ---
		
		grabAndLift();
		
		redKnockOff();
		
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
		setTargets(22 * INCH);
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
			
			case LEFT:
				driveDistance = 14;
				break;
			case CENTER:
				driveDistance = 5;
				break;
			case RIGHT:
			default:
				driveDistance = -3;
			
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
		setTargets(8 * INCH);
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
