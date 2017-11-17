package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto;

/**
 * Created by MerrittAM on 11/13/2017.
 */
@Autonomous(name = "Auto_Red_BackSide", group = "Autonomous")
public class Auto_Red_BackSide extends Auto {
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize();
		
		waitForStart();
		
		grabAndLift();
		
		redKnockOff();
		
		moveToFirstColumn();
		
		lowerAndRelease();
		
		backup();
		
		sleep(500);
		
	}
	
	private void moveToFirstColumn() {
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(18 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy());
		stopDriveMotors();
		
		sleep(1000);
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition(-5 * INCH);
		frontRight.setTargetPosition(5 * INCH);
		backRight.setTargetPosition(-5 * INCH);
		backLeft.setTargetPosition(5 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy());
		stopDriveMotors();
		
		sleep(1000);
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(7 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy());
		stopDriveMotors();
		
		sleep(1000);
		
	}
	
}

