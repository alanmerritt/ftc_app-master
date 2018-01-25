package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.calibration.CalibrationManager;

/**
 * Created by MerrittAM on 10/31/2017.
 * Second TeleOp version. Hope to include field centric drive
 * and secondary driver.
 */
@TeleOp(name = "TeleOp_v3", group = "TeleOp")
public class TeleOp_v3 extends OpMode {
	
	private DcMotor frontLeft;
	private DcMotor frontRight;
	private DcMotor backRight;
	private DcMotor backLeft;
	
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
	
	private boolean gripperPressedLast = false;
	private boolean gripperOn = false;
	
	private boolean upperGripperPressedLast = false;
	private boolean upperGripperOn = false;
	
	private boolean grabbingStack = false;
	
	private enum GRAB_STACK_STATE {
	
		RELEASE,
		TRANSITION_TO_BACKUP,
		BACKUP,
		LOWER,
		FORWARD,
		GRAB,
		LIFT
	
	}
	GRAB_STACK_STATE grabState = GRAB_STACK_STATE.RELEASE;
	int grabStackTarget = (int)(1120*0.25);
	ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
	
	CalibrationManager calibrationManager = new CalibrationManager(telemetry);
	
	private DcMotor extender;
	private Servo wrist;
	private boolean wristLast;
	private boolean wristUp;
	private Servo claw;
	private boolean clawLast;
	private boolean clawUp;
	
	enum DriveMode {
	
		NORMAL,
		CURVE
	
	}
	
	private DriveMode driveMode = DriveMode.NORMAL;
	
	private void setupDrive() {
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
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
	}
	
	private void setupLift() {
		
		//Lift motors.
		rightLift = hardwareMap.dcMotor.get("rightLift");
		leftLift = hardwareMap.dcMotor.get("leftLift");
		
		//Reverse one of the lift motors.
		leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
		
		leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		
		//Set the lift motors to brake mode.
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
	}
	
	private void setupGripper() {
		
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
	
	private void setUpExtender() {
	
		extender = hardwareMap.dcMotor.get("extender");
		extender.setDirection(DcMotorSimple.Direction.REVERSE);
		
		wrist = hardwareMap.servo.get("wrist");
		claw = hardwareMap.servo.get("claw");
		
		wristLast = false;
		wristUp = false;
		clawLast = false;
		clawUp = false;
		
	}
	
	@Override
	public void init() {
		
		setupDrive();
		telemetry.addLine("Drive ready.");
		
		setupLift();
		telemetry.addLine("Lift ready.");
		
		setupGripper();
		telemetry.addLine("Gripper ready.");
		
		hardwareMap.servo.get("knockerOffer").setPosition(Double.parseDouble(calibrationManager.get("knockerOfferRaised")));
		telemetry.addLine("Knocker offer position set.");
		
		setUpExtender();
		
		telemetry.addLine("Initialization complete.");
		telemetry.update();
		
	}
	
	@Override
	public void loop() {
		
		
		drive();
		runLift();
		runGripper();
		runExtender();
		
		telemetry.update();
		
	}
	
	private void autoStack() {
		
		
		if(gamepad1.dpad_up) {
			grabbingStack = true;
		}
		
		if(grabbingStack) {
			
			double liftPower = 0;
			
			switch(grabState) {
				
				case RELEASE:
					
					gripperOn = false;
					upperGripperOn = false;
					
					//!!! May need short delay.
					grabState = GRAB_STACK_STATE.TRANSITION_TO_BACKUP;
					
					telemetry.addLine("Releasing.");
					
					break;
				
				case TRANSITION_TO_BACKUP:
					
					setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
					setModes(DcMotor.RunMode.RUN_TO_POSITION);
					setTargets(grabStackTarget);
					
					grabState = GRAB_STACK_STATE.BACKUP;
					
					telemetry.addLine("Transition to backup.");
					
					break;
				
				case BACKUP:
					
					if(frontLeft.isBusy()) {
						setPowers(1);
					} else {
						setPowers(0);
						setTargets(0);
						grabState = GRAB_STACK_STATE.LOWER;
					}
					
					telemetry.addLine("Backing up.");
					telemetry.addData("Position", frontLeft.getCurrentPosition());
					
					break;
				
				case LOWER:
					
					if(leftLift.getCurrentPosition() > 200) {
						liftPower = -.75;
					} else if(leftLift.getCurrentPosition() > 50) {
						liftPower = -.4;
					} else if(leftLift.getCurrentPosition() > 0) {
						liftPower = -.1;
					} else {
						liftPower = 0;
						grabState = GRAB_STACK_STATE.FORWARD;
					}
					
					leftLift.setPower(liftPower);
					rightLift.setPower(liftPower);
					
					telemetry.addLine("Lowering.");
					telemetry.addData("Lift position", leftLift.getCurrentPosition());
					
					break;
				
				case FORWARD:
					
					if(frontLeft.isBusy()) {
						setPowers(1);
					} else {
						setPowers(0);
						timer.reset();
						grabState = GRAB_STACK_STATE.GRAB;
					}
					
					telemetry.addLine("Moving forward.");
					telemetry.addData("Position", frontLeft.getCurrentPosition());
					
					break;
				
				case GRAB:
					
					gripperOn = true;
					upperGripperOn = true;
					
					if(timer.time() > 200) {
						grabState = GRAB_STACK_STATE.LIFT;
					}
					
					telemetry.addLine("Grabbing.");
					
					break;
				
				case LIFT:
					
					if(leftLift.getCurrentPosition() < 150) {
						liftPower = 1;
					} else {
						liftPower = 0;
					}
					
					leftLift.setPower(liftPower);
					rightLift.setPower(liftPower);
					
					telemetry.addLine("Lifting.");
					telemetry.addData("Lift position", leftLift.getCurrentPosition());
					
					break;
				
			}
			
			
		} else {
			grabState = GRAB_STACK_STATE.RELEASE;
			setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}
		
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
	
	private void setPowers(double power) {
		frontLeft.setPower(power);
		frontRight.setPower(power);
		backRight.setPower(power);
		backLeft.setPower(power);
	}
	
	private void drive() {
		
		//Get the joystick values.
		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;
		double r = gamepad1.right_stick_x;
		
		
		
		if(x != 0 || y != 0 || r != 0) {
			grabbingStack = false;
		}
		
		telemetry.addData("X", x);
		telemetry.addData("Y", y);
		telemetry.addData("R", r);
		
		if(!grabbingStack) {
			//Drive the motors in robot-centric mode.
			frontLeft.setPower(Range.clip(y - x - r, -1, 1));
			backLeft.setPower(Range.clip(y + x - r, -1, 1));
			frontRight.setPower(Range.clip(y + x + r, -1, 1));
			backRight.setPower(Range.clip(y - x + r, -1, 1));
		}
		
	}
	
	private void runLift() {
		
		if(gamepad1.right_trigger != 0 || gamepad1.left_trigger != 0 ||
				gamepad2.right_trigger != 0 || gamepad2.left_trigger != 0 ||
				gamepad1.right_bumper || gamepad1.left_bumper ||
				gamepad2.right_bumper || gamepad2.right_bumper) {
			grabbingStack = false;
		}
		
		double liftPower;
		
		//Operate the lift when the triggers are pressed.
		if(gamepad1.right_trigger != 0) { //First right trigger - raises lift.
			
			liftPower = gamepad1.right_trigger * 1;
			
		} else if(gamepad1.left_trigger != 0) { //First left trigger - lowers lift.
			
			liftPower = -gamepad1.left_trigger * 1;
			
		} else if(gamepad2.right_trigger != 0) { //Second right trigger - raises lift.
			
			liftPower = gamepad2.right_trigger * 1;
			
		} else if(gamepad2.left_trigger != 0) { //Second left trigger - lowers lift.
			
			liftPower = -gamepad2.left_trigger * 1;
			
		} else { //Deactivates lift.
			
			liftPower = 0;
			
			if(gamepad1.left_bumper || gamepad2.left_bumper) {
				
				
				if (leftLift.getCurrentPosition() > 200) {
					liftPower = -.5;
				} else if (leftLift.getCurrentPosition() > 0) {
					liftPower = -.1;
				}
				
			}
			
		}
		
		if(!grabbingStack) {
			leftLift.setPower(liftPower);
			rightLift.setPower(liftPower);
		}
		
	}
	
	private void runGripper() {
		
		if(gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
				gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y) {
			grabbingStack = false;
		}
		
		// --- Lower gripper ---
		if((gamepad1.a || gamepad2.a) && !gripperPressedLast) {
			gripperOn = !gripperOn;
		}
		
		if(gripperOn) {
			leftGripper.setPosition(lServoClose);
			rightGripper.setPosition(rServoClose);
		} else {
			leftGripper.setPosition(lServoOpen);
			rightGripper.setPosition(rServoOpen);
		}
		
		gripperPressedLast = gamepad1.a || gamepad2.a;
		// ---------------------
		
		
		// --- Upper gripper ---
		if((gamepad1.b || gamepad2.b) && !upperGripperPressedLast) {
			upperGripperOn = !upperGripperOn;
		}
		
		if(upperGripperOn) {
			upperLeftGripper.setPosition(ulServoClose);
			upperRightGripper.setPosition(urServoClose);
		} else {
			upperLeftGripper.setPosition(ulServoOpen);
			upperRightGripper.setPosition(urServoOpen);
		}
		
		upperGripperPressedLast = gamepad1.b || gamepad2.b;
		// ---------------------
		
		if(gamepad1.x || gamepad2.x) {
			gripperOn = true;
			upperGripperOn = true;
		}
		if(gamepad1.y || gamepad2.y) {
			gripperOn = false;
			upperGripperOn = false;
		}
		
	}
	
	private void runExtender() {
	
		double extenderSpeed = -gamepad2.left_stick_y;
		
		extender.setPower(extenderSpeed);
		
		if(gamepad2.dpad_up && !wristLast) {
			wristUp = !wristUp;
		}
		if(wristUp) {
			wrist.setPosition(1);
		} else {
			wrist.setPosition(0);
		}
		
		if(gamepad2.dpad_down && !clawLast) {
			clawUp = !clawUp;
		}
		if(clawUp) {
			claw.setPosition(0);
		} else {
			claw.setPosition(.75);
		}
		
		wristLast = gamepad2.dpad_up;
		clawLast = gamepad2.dpad_down;
		
	}
	
}
