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
		
		waitForStart();
		
		grabAndLift();
		
		blueKnockOff();
		
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(-23 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
		sleep(1000);
		
		
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > -175 && !isStopRequested()) {
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			driveMotors(.3, -.3, -.3, .3);
		}
		stopDriveMotors();
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition(6 * INCH);
		frontRight.setTargetPosition(-6 * INCH);
		backRight.setTargetPosition(6 * INCH);
		backLeft.setTargetPosition(-6 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
		sleep(1000);
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(7 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
		sleep(1000);
		
		lowerAndRelease();
		
		sleep(1000);
		
		backup();
		
		sleep(500);
		
		
	}
}
