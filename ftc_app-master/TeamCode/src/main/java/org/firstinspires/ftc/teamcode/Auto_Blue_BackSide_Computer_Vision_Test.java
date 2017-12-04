package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import java.util.ArrayList;

/**
 * Created by MerrittAM on 11/14/2017.
 * Version 2 of the blue, back autonomous which implements
 * the computer vision to place blocks in the correct column.
 */
@Autonomous(name = "Auto_Blue_Backside_Computer_Vision_Test", group = "Autonomous")
public class Auto_Blue_BackSide_Computer_Vision_Test extends Auto {
	
	
	//Vuforia object thing.
	private VuforiaLocalizer vuforia;
	private ArrayList<String> messages;
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		messages = new ArrayList<>();
		
		
		ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
		
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		
		messages.add("Camera monitor set up.");
		writeMessages();
		
		parameters.vuforiaLicenseKey = "ARS60u//////AAAAGatg5bzrIElJruCZEPDqKr8mksRb99R0GEdJMfM4xVotZyXhiShn+ToKcAK2foRmNGNekn6uvxjmkdjbOlFvoQhDYJVBYvFF3afgz8aWcqo+WkdT3pXqnEcrPtMd4bz/CuC65ajgco231Ca7iUjqk7tuzv5Zg5gUpAfE2FulF0GIq6sXboe5OqrDxCLG+tA6oF24zuzFCEGZHUs8PDL3NwoA2KKbZttdoE13Kvqq9+AgBrqWeIYwefx9nkzWmn81QXHFd68APHaKyKT1PNxWKEK9aDL5vp4LRiG17AaBaGpXedpKVN4/o6GAWJm2zCOLwOb1aSMP8u7hDTXxsax0iMyEFYpzhshDar3HwD4xNy28";
		
		messages.add("License key applied.");
		writeMessages();
		
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
		
		messages.add("Camera direction set.");
		writeMessages();
		
		this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
		
		messages.add("Localizer created.");
		writeMessages();
		
		VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
		
		messages.add("Trackables loaded.");
		writeMessages();
		
		VuforiaTrackable relicTemplate = relicTrackables.get(0);
		relicTemplate.setName("relicVuMarkTemplate");
		
		
		
		initialize();
		
		
		waitForStart();
		
		//Start searching for one of the vision targets.
		relicTrackables.activate();
		
		messages.add("Trackable activated.");
		writeMessages();
		
		//Save the vuMark that is found.
		RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
		telemetry.addData("VuMark found", vuMark.toString());
		sleep(1000);
		
		time.reset();
		while(vuMark == RelicRecoveryVuMark.UNKNOWN && !isStopRequested()) {
			vuMark = RelicRecoveryVuMark.from(relicTemplate);
			telemetry.addData("VuMark found", vuMark.toString());
			telemetry.update();
		}
		
		sleep(1000);
		
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
		
		//Rotate.
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		double yaw = gyro.getTotalYaw();
		while(yaw > -175 && !isStopRequested()) {
			gyro.updateYaw();
			yaw = gyro.getTotalYaw();
			driveMotors(.3, -.3, -.3, .3);
		}
		stopDriveMotors();
		
		//Rightward movement.
		int distanceToMove;
		switch (vuMark) {
			case RIGHT:
				distanceToMove = 25; //TODO: Adjust right movement values.
				break;
			case CENTER:
				distanceToMove = 12; //TODO: Adjust center movement values.
				break;
			case LEFT:
			default:
				distanceToMove = 6;
				break;
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		frontLeft.setTargetPosition(distanceToMove * INCH);
		frontRight.setTargetPosition(-distanceToMove * INCH);
		backRight.setTargetPosition(distanceToMove * INCH);
		backLeft.setTargetPosition(-distanceToMove * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
		sleep(1000);
		
		//Forward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(5 * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
		
		sleep(1000);
		
		lowerAndRelease();
		
		sleep(1000);
		
		backup();
		
		sleep(500);
		
		
	}
	
	private void writeMessages() {
		
		for(String s : messages) {
			
			telemetry.addLine(s);
			
		}
		
		telemetry.update();
		
	}
	
}
