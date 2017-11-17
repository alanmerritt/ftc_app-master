package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.calibration.CalibrationManager;

/**
 * Created by MerrittAM on 10/31/2017.
 * Second TeleOp version. Hope to include field centric drive
 * and secondary driver.
 */
@TeleOp(name = "TeleOp_v2", group = "TeleOp")
public class TeleOp_v2 extends OpMode {
	
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
	
	private double liftUpPower = .35;
	private double liftDownPower = -.35;
	private final double LIFT_STOP_POWER = 0;
	
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
	
	private Gyro gyro;
	
	private double placementOrientation = 0;
	private boolean returnToPlacementOrientation = false;
	
	private DigitalChannel liftDetector;
	private boolean lBumperPressedLast = false;
	private boolean autoLowerSlide = false;
	
	@Override
	public void init() {
		
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
		
		//Lift motors.
		rightLift = hardwareMap.dcMotor.get("rightLift");
		leftLift = hardwareMap.dcMotor.get("leftLift");
		
		//Reverse one of the lift motors.
		leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
		
		//Set the lift motors to brake mode.
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		//Get the gripper servos.
		rightGripper = hardwareMap.servo.get("rightGripper");
		leftGripper = hardwareMap.servo.get("leftGripper");
		upperRightGripper = hardwareMap.servo.get("upperRightGripper");
		upperLeftGripper = hardwareMap.servo.get("upperLeftGripper");
		
		liftDetector = hardwareMap.get(DigitalChannel.class, "btnLiftDetector");
		liftDetector.setMode(DigitalChannel.Mode.INPUT);
		
		//Initialize the gyroscope.
		gyro = new Gyro(this);
		
		telemetry.addLine("Hardware mapping complete.");
		
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
		
		//Get the lift power values.
		liftUpPower = -Double.parseDouble(calibrationManager.get("liftPower"));
		liftDownPower = Double.parseDouble(calibrationManager.get("liftPower"));
		telemetry.addLine("Calibration data loaded.");
		
		telemetry.update();
		
	}
	
	@Override
	public void loop() {
		
		gyro.updateYaw();
		
		telemetry.addData("Yaw", gyro.getYaw());
		telemetry.addData("Total yaw", gyro.getTotalYaw());
		
		
		
		drive();
		runLift();
		runGripper();
		
		telemetry.update();
		
	}
	
	private void drive() {
		
		//Get the joystick values.
		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;
		double r = gamepad1.right_stick_x;
		
		telemetry.addData("X", x);
		telemetry.addData("Y", y);
		telemetry.addData("R", r);
		
		// --- Auto-turning function. ---
		
		//Rotate toward the box.
		if(returnToPlacementOrientation) {
			r = 1;
		}
		
		//Activate the auto-turn.
		if(gamepad1.y) {
			returnToPlacementOrientation = true;
		}
		
		//Turning range.
		double min = placementOrientation - 5;
		double max = placementOrientation + 5;
		double position = gyro.getYaw();
		
		//If the robot is within the range, stop the function.
		if(position >= min && position <= max) {
			returnToPlacementOrientation = false;
		}
		
		telemetry.addData("Returning to placement location", returnToPlacementOrientation);
		
		// ------------------------------
		
		//Drive the motors in robot-centric mode.
		frontLeft.setPower(Range.clip(y - x - r, -1, 1));
		backLeft.setPower(Range.clip(y + x - r, -1, 1));
		frontRight.setPower(Range.clip(y + x + r, -1, 1));
		backRight.setPower(Range.clip(y - x + r, -1, 1));
		
	}
	
	private void runLift() {
		
		//TODO: Test motor speed scaling.
		
		double liftPower;
		
		//Operate the lift when the triggers are pressed.
		if(gamepad1.right_trigger != 0) { //First right trigger - raises lift.
			
			liftPower = gamepad1.right_trigger;
			
		} else if(gamepad1.left_trigger != 0) { //First left trigger - lowers lift.
			
			liftPower = -gamepad1.left_trigger;
			
		} else if(gamepad2.right_trigger != 0) { //Second right trigger - raises lift.
			
			liftPower = gamepad2.right_trigger;
			
		} else if(gamepad2.left_trigger != 0) { //Second left trigger - lowers lift.
			
			liftPower = -gamepad2.left_trigger;
			
		} else { //Deactivates lift.
			
			liftPower = 0;
			
		}
		/*else {
			
			telemetry.addLine();
			telemetry.addLine("No manual control.");
			telemetry.addData("Auto-lower", autoLowerSlide);
			
			//Turn on the auto-lowering when a bumper is clicked.
			if((gamepad1.left_bumper || gamepad2.left_bumper) && !lBumperPressedLast) {
				autoLowerSlide = true;
			}
			
			//If the button is hit, turn off the auto-lowering function.
			if(!liftDetector.getState()) {
				telemetry.addLine("Lift stop button pressed.");
				autoLowerSlide = false;
			}
			
			//If auto-lowering is activated, lower the slide.
			if(autoLowerSlide) {
				rightLift.setPower(liftDownPower);
				leftLift.setPower(liftDownPower);
			} else { //Otherwise, turn off the slide.
				rightLift.setPower(LIFT_STOP_POWER);
				leftLift.setPower(LIFT_STOP_POWER);
			}
			
		}
		}
		
		//Update bumper pressed last.
		lBumperPressedLast = gamepad1.left_bumper || gamepad2.left_bumper;
		
		*/
		
		//Decrease the motor power.
		liftPower /= 2;
		
		leftLift.setPower(liftPower);
		rightLift.setPower(liftPower);
		
		telemetry.addLine();
		telemetry.addData("Gamepad1 left", gamepad1.left_trigger);
		telemetry.addData("Gamepad2 left", gamepad2.left_trigger);
		telemetry.addLine();
		telemetry.addData("Gamepad1 right", gamepad1.right_trigger);
		telemetry.addData("Gamepad2 right", gamepad2.right_trigger);
		
	}
	
	private void runGripper() {
		
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
		
	}
	
}
