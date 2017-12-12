package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.calibration.CalibrationManager;

import java.util.ArrayList;

/**
 * Created by MerrittAM on 11/9/2017.
 * Base class for autonomous programs.
 */

public abstract class Auto extends LinearOpMode {
	
	final int ENCODER_TICKS_PER_REVOLUTION = 1120;
	final int WHEEL_DIAMETER = 4; //in.
	final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; //in
	final int INCH = (int)(ENCODER_TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE);
	
	DcMotor frontLeft;
	DcMotor frontRight;
	DcMotor backRight;
	DcMotor backLeft;
	
	DcMotor rightLift;
	DcMotor leftLift;
	
	Servo rightGripper;
	Servo leftGripper;
	Servo upperRightGripper;
	Servo upperLeftGripper;
	
	double lServoOpen;
	double lServoClose;
	double rServoOpen;
	double rServoClose;
	
	double ulServoOpen;
	double ulServoClose;
	double urServoOpen;
	double urServoClose;
	
	Gyro gyro;
	
	final boolean GRIPPER_CLOSED = true;
	final boolean GRIPPER_OPEN = false;
	
	
	Servo knockerOffer;
	double knockerOfferRaised;
	double knockerOfferLowered;
	
	final int LIFT_RAISE_POSITION = (1120/3)*2;
	
	CalibrationManager calibrationManager = new CalibrationManager(telemetry);
	
	ColorSensor colorSensor;
	
	private void setupDrive() {
		
		//Hardware map drive motors.
		frontLeft = hardwareMap.dcMotor.get("fl");
		frontRight = hardwareMap.dcMotor.get("fr");
		backRight = hardwareMap.dcMotor.get("br");
		backLeft = hardwareMap.dcMotor.get("bl");
		
		//Set the mode to brake.
		frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		//Reverse the left motors.
		frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
		backRight.setDirection(DcMotorSimple.Direction.REVERSE);
		
	}
	
	private void setupLift() {
		
		//Lift motors.
		rightLift = hardwareMap.dcMotor.get("rightLift");
		leftLift = hardwareMap.dcMotor.get("leftLift");
		
		//Reverse one of the lift motors.
		rightLift.setDirection(DcMotorSimple.Direction.REVERSE);
		
		//Set the lift motors to brake mode.
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
	}
	
	private void setupGrippers() {
		
		//Get the gripper servos.
		rightGripper = hardwareMap.servo.get("rightGripper");
		leftGripper = hardwareMap.servo.get("leftGripper");
		upperRightGripper = hardwareMap.servo.get("upperRightGripper");
		upperLeftGripper = hardwareMap.servo.get("upperLeftGripper");
		
		//Get the lower servo open/close values.
		lServoOpen = Double.parseDouble(calibrationManager.get("lServoOpen"));
		lServoClose = Double.parseDouble(calibrationManager.get("lServoClose"));
		rServoOpen = Double.parseDouble(calibrationManager.get("rServoOpen"));
		rServoClose = Double.parseDouble(calibrationManager.get("rServoClose"));
		
		//Get the upper servo open/close values.
		ulServoOpen = Double.parseDouble(calibrationManager.get("upperlServoOpen"));
		ulServoClose = Double.parseDouble(calibrationManager.get("upperlServoClose"));
		urServoOpen = Double.parseDouble(calibrationManager.get("upperrServoOpen"));
		urServoClose = Double.parseDouble(calibrationManager.get("upperrServoClose"));
		
	}
	
	private void setupKnockerOffer() {
		
		knockerOffer = hardwareMap.servo.get("knockerOffer");
		knockerOfferRaised = Double.parseDouble(calibrationManager.get("knockerOfferRaised"));
		knockerOfferLowered = Double.parseDouble(calibrationManager.get("knockerOfferLowered"));
		
		//Initialize knocker-offer to raised position.
		knockerOffer.setPosition(knockerOfferRaised);
		
		colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
		
	}
	
	
	protected ArrayList<String> messages;
	
	protected VuforiaLocalizer vuforia;
	protected VuforiaTrackables relicTrackables;
	protected VuforiaTrackable relicTemplate;
	
	/**
	 * Prepares the Vuforia computer vision
	 * for object detection.
	 */
	private void setupVuforia() {
		
		
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
		
		relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
		
		messages.add("Trackables loaded.");
		writeMessages();
		
		relicTemplate = relicTrackables.get(0);
		
		messages.add("Templates gotten.");
		writeMessages();
		
		relicTemplate.setName("relicVuMarkTemplate");
		
		messages.add("Template named.");
		writeMessages();
		
	}
	
	RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
	void scanVuMark(int timeout) {
		
		if(vuMark == RelicRecoveryVuMark.UNKNOWN) {
			
			ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
			time.reset();
			vuMark = RelicRecoveryVuMark.from(relicTemplate);
			while (vuMark == RelicRecoveryVuMark.UNKNOWN &&
					!isStopRequested() && time.time() < timeout && !isStarted()) {
				
				vuMark = RelicRecoveryVuMark.from(relicTemplate);
				telemetry.addLine("Searching for VuMark.");
				telemetry.update();
				
			}
			
			if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
				telemetry.addLine("VuMark found: " + vuMark + ".");
			} else {
				telemetry.addLine("No VuMark found.");
			}
			
		} else {
			telemetry.addLine("VuMark already found.");
		}
		
		telemetry.update();
		
	}
	
	void scanVuMark() {
		
		if(vuMark == RelicRecoveryVuMark.UNKNOWN) {
			
			vuMark = RelicRecoveryVuMark.from(relicTemplate);
			while (vuMark == RelicRecoveryVuMark.UNKNOWN &&
					!isStopRequested() && !isStarted()) {
				
				vuMark = RelicRecoveryVuMark.from(relicTemplate);
				telemetry.addLine("Searching for VuMark.");
				telemetry.update();
				
			}
			
			if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
				telemetry.addLine("VuMark found: " + vuMark + ".");
			} else {
				telemetry.addLine("No VuMark found.");
			}
			
		} else {
			telemetry.addLine("VuMark already found.");
		}
		
		telemetry.update();
		
	}
	
	protected void initialize() {
		
		messages = new ArrayList<>();
		
		setupDrive();
		
		messages.add("Drive initialized.");
		writeMessages();
		
		setupLift();
		
		messages.add("Lift initialized.");
		writeMessages();
		
		setupGrippers();
		
		messages.add("Grippers initialized.");
		writeMessages();
		
		setupKnockerOffer();
		
		messages.add("Knocker-offer initialized.");
		writeMessages();
		
		//Initialize the gyroscope.
		gyro = new Gyro(this);
		
		messages.add("Gyro initialized.");
		writeMessages();
		
		setupVuforia();
		
		messages.add("Vuforia setup complete.");
		writeMessages();
		
		
		
		messages.add("Initialization complete.");
		writeMessages();
		
	}
	
	protected void driveMotors(double fl, double fr, double br, double bl) {
		
		frontLeft.setPower(fl);
		frontRight.setPower(fr);
		backRight.setPower(br);
		backLeft.setPower(bl);
		
	}
	
	protected void stopDriveMotors() {
		driveMotors(0, 0, 0, 0);
	}
	
	protected void setLowerGripper(boolean closed) {
		
		if(closed) {
			leftGripper.setPosition(lServoClose);
			rightGripper.setPosition(rServoClose);
		} else {
			leftGripper.setPosition(lServoOpen);
			rightGripper.setPosition(rServoOpen);
		}
		
	}
	
	protected void grabAndLift() {
		
		
		telemetry.addLine("Grabbing.");
		telemetry.update();
		setLowerGripper(GRIPPER_CLOSED);
		sleep(700);
		
		
		leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
		leftLift.setPower(.2);
		rightLift.setPower(.2);
		while(leftLift.getCurrentPosition() < LIFT_RAISE_POSITION && !isStopRequested());
		leftLift.setPower(0);
		rightLift.setPower(0);
		
	}
	
	protected void lowerAndRelease() {
		
		setLowerGripper(GRIPPER_OPEN);
		
		sleep(500);
		
		backup(-5);
		
		leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
		leftLift.setPower(-.1);
		rightLift.setPower(-.1);
		while(leftLift.getCurrentPosition() > -LIFT_RAISE_POSITION && !isStopRequested());
		leftLift.setPower(0);
		rightLift.setPower(0);
		
		backup(4);
		
	}
	
	protected void setModes(DcMotor.RunMode mode) {
		frontLeft.setMode(mode);
		frontRight.setMode(mode);
		backRight.setMode(mode);
		backLeft.setMode(mode);
	}
	
	protected void setTargets(int target) {
		frontLeft.setTargetPosition(target);
		frontRight.setTargetPosition(target);
		backRight.setTargetPosition(target);
		backLeft.setTargetPosition(target);
	}
	
	private void driveForwardBackward(double power) {
		driveMotors(power, power, power, power);
	}
	
	final double PLATFORM_MOVEMENT_SPEED = .15;
	
	/**
	 * Used in red autonomous.
	 * Knocks off the blue ball.
	 */
	protected void redKnockOff() {
		
		knockerOffer.setPosition(knockerOfferLowered);
		
		sleep(1000);
		
		telemetry.addData("Blue", colorSensor.blue());
		telemetry.addData("Red", colorSensor.red());
		telemetry.update();
		
		if(colorSensor.blue() > colorSensor.red()) {
			//If blue is seen, drive directly off the block toward the back side of the field.
			
			//Drive forward.
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			
			setTargets(1120/4);
			while(frontLeft.isBusy() && !isStopRequested()) {
				driveForwardBackward(PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			
			sleep(1000);
			knockerOffer.setPosition(knockerOfferRaised);
			sleep(1500);
			
			
			
		} else {
			//If red is seen, drive toward the relic side and back to the middle.
			
			//Drive backward, then move back forward.
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(-1120/4);
			while(frontLeft.isBusy() && !isStopRequested()) {
				driveForwardBackward(-PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			sleep(1000);
			knockerOffer.setPosition(knockerOfferRaised);
			sleep(1500);
			
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(1120/2);
			while(frontLeft.isBusy() && !isStopRequested()) {
				driveForwardBackward(PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
	}
	
	/**
	 * Used in blue autonomous.
	 * Knocks off the red ball.
	 */
	protected void blueKnockOff() {
		
		knockerOffer.setPosition(knockerOfferLowered);
		
		sleep(1000);
		
		telemetry.addData("Blue", colorSensor.blue());
		telemetry.addData("Red", colorSensor.red());
		telemetry.update();
		
		if(colorSensor.blue() > colorSensor.red()) { //If blue is seen.
			
			//Drive forward.
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(-1120/4);
			while(frontLeft.isBusy() && !isStopRequested()) {
				driveForwardBackward(-PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			sleep(1000);
			knockerOffer.setPosition(knockerOfferRaised);
			sleep(1500);
			
			
			
		} else { //If red is seen.
			
			//Drive backward, then move back forward.
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(1120/4);
			while(frontLeft.isBusy() && !isStopRequested()) {
				driveForwardBackward(PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			sleep(1000);
			knockerOffer.setPosition(knockerOfferRaised);
			sleep(1500);
			
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(-1120/2);
			while(frontLeft.isBusy() && !isStopRequested()) {
				driveForwardBackward(-PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
	}
	
	void backup(int distance) {
		
		//Backward movement.
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_TO_POSITION);
		setTargets(distance * INCH);
		driveMotors(.5, .5, .5, .5);
		while(frontLeft.isBusy() && !isStopRequested());
		stopDriveMotors();
	
	}
	
	@Deprecated
	void backup() {
		
		backup(-5);
		
	}
	
	private void writeMessages() {
		
		for(String s : messages) {
			
			telemetry.addLine(s);
			
		}
		
		telemetry.update();
		
	}
	
}
