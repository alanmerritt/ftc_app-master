package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.calibration.CalibrationManager;

/**
 * Created by MerrittAM on 11/9/2017.
 * Base class for autonomous programs.
 */

public abstract class Auto extends LinearOpMode {
	
	protected final int ENCODER_TICKS_PER_REVOLUTION = 1120;
	protected final int WHEEL_DIAMETER = 4; //in.
	protected final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; //in
	protected final double TICKS_PER_INCH = ENCODER_TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE;
	
	protected DcMotor frontLeft;
	protected DcMotor frontRight;
	protected DcMotor backRight;
	protected DcMotor backLeft;
	
	private DcMotor rightLift;
	private DcMotor leftLift;
	
	private Servo rightGripper;
	private Servo leftGripper;
	private Servo upperRightGripper;
	private Servo upperLeftGripper;
	
	private double lServoOpen;
	private double lServoClose;
	private double rServoOpen;
	private double rServoClose;
	
	private double ulServoOpen;
	private double ulServoClose;
	private double urServoOpen;
	private double urServoClose;
	
	Gyro gyro;
	
	final boolean GRIPPER_CLOSED = true;
	final boolean GRIPPER_OPEN = false;
	
	
	protected Servo knockerOffer;
	protected double knockerOfferRaised;
	protected double knockerOfferLowered;
	
	protected ColorSensor colorSensor;
	
	protected void initialize() {
		
		//Drive motors.
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
		
		//Lift motors.
		rightLift = hardwareMap.dcMotor.get("rightLift");
		leftLift = hardwareMap.dcMotor.get("leftLift");
		
		//Reverse one of the lift motors.
		rightLift.setDirection(DcMotorSimple.Direction.REVERSE);
		
		//Set the lift motors to brake mode.
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		//Get the gripper servos.
		rightGripper = hardwareMap.servo.get("rightGripper");
		leftGripper = hardwareMap.servo.get("leftGripper");
		upperRightGripper = hardwareMap.servo.get("upperRightGripper");
		upperLeftGripper = hardwareMap.servo.get("upperLeftGripper");
		
		//Initialize the gyroscope.
		gyro = new Gyro(this);
		
		knockerOffer = hardwareMap.servo.get("knockerOffer");
		
		colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
		
		CalibrationManager calibrationManager = new CalibrationManager(telemetry);
		
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
		
		knockerOfferRaised = Double.parseDouble(calibrationManager.get("knockerOfferRaised"));
		knockerOfferLowered = Double.parseDouble(calibrationManager.get("knockerOfferLowered"));
		
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
		while(leftLift.getCurrentPosition() < 1120/2);
		leftLift.setPower(0);
		rightLift.setPower(0);
		
	}
	
	protected void lowerAndRelease() {
		
		leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
		leftLift.setPower(-.1);
		rightLift.setPower(-.1);
		while(leftLift.getCurrentPosition() > -1120/2);
		leftLift.setPower(0);
		rightLift.setPower(0);
		
		sleep(700);
		setLowerGripper(GRIPPER_OPEN);
		
	}
	
	private void setModes(DcMotor.RunMode mode) {
		frontLeft.setMode(mode);
		frontRight.setMode(mode);
		backRight.setMode(mode);
		backLeft.setMode(mode);
	}
	
	private void setTargets(int target) {
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
			while(frontLeft.isBusy()) {
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
			setTargets(-1120/5);
			while(frontLeft.isBusy()) {
				driveForwardBackward(-PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			sleep(1000);
			knockerOffer.setPosition(knockerOfferRaised);
			sleep(1500);
			
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(1120/4);
			while(frontLeft.isBusy()) {
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
			while(frontLeft.isBusy()) {
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
			setTargets(1120/5);
			while(frontLeft.isBusy()) {
				driveForwardBackward(PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
			sleep(1000);
			knockerOffer.setPosition(knockerOfferRaised);
			sleep(1500);
			
			setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			setModes(DcMotor.RunMode.RUN_TO_POSITION);
			setTargets(-1120/4);
			while(frontLeft.isBusy()) {
				driveForwardBackward(-PLATFORM_MOVEMENT_SPEED);
			}
			stopDriveMotors();
			
		}
		
		setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
	}
	
}
