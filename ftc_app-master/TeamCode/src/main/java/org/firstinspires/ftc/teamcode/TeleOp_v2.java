package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
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
	
	@Override
	public void init() {
		
		frontLeft = hardwareMap.dcMotor.get("fl");
		frontRight = hardwareMap.dcMotor.get("fr");
		backRight = hardwareMap.dcMotor.get("br");
		backLeft = hardwareMap.dcMotor.get("bl");
		
		frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		
		rightLift = hardwareMap.dcMotor.get("rightLift");
		leftLift = hardwareMap.dcMotor.get("leftLift");
		
		leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
		
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		rightGripper = hardwareMap.servo.get("rightGripper");
		leftGripper = hardwareMap.servo.get("leftGripper");
		upperRightGripper = hardwareMap.servo.get("upperRightGripper");
		upperLeftGripper = hardwareMap.servo.get("upperLeftGripper");
		
		gyro = new Gyro(this);
		
		telemetry.addLine("Hardware mapping complete.");
		
		CalibrationManager calibrationManager = new CalibrationManager(telemetry);
		lServoOpen = Double.parseDouble(calibrationManager.get("lServoOpen"));
		lServoClose = Double.parseDouble(calibrationManager.get("lServoClose"));
		rServoOpen = Double.parseDouble(calibrationManager.get("rServoOpen"));
		rServoClose = Double.parseDouble(calibrationManager.get("rServoClose"));
		
		ulServoOpen = Double.parseDouble(calibrationManager.get("upperlServoOpen"));
		ulServoClose = Double.parseDouble(calibrationManager.get("upperlServoClose"));
		urServoOpen = Double.parseDouble(calibrationManager.get("upperrServoOpen"));
		urServoClose = Double.parseDouble(calibrationManager.get("upperrServoClose"));
		
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
		
		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;
		double r = gamepad1.right_stick_x;
		
		if(returnToPlacementOrientation) {
			r = 1;
			telemetry.addLine("Returning to placement.");
		}
		
		if(gamepad1.y) {
			returnToPlacementOrientation = true;
		}
		double min = placementOrientation - 5;
		double max = placementOrientation + 5;
		double position = gyro.getYaw();
		
		if(position >= min && position <= max) {
			
			returnToPlacementOrientation = false;
			
		}
		
		telemetry.addData("Returning to placement location", returnToPlacementOrientation);
		
		frontLeft.setPower(Range.clip(y - x - r, -1, 1));
		backLeft.setPower(Range.clip(y + x - r, -1, 1));
		frontRight.setPower(Range.clip(y + x + r, -1, 1));
		backRight.setPower(Range.clip(y - x + r, -1, 1));
		
	}
	
	private void runLift() {
		
		if(gamepad1.right_trigger != 0 || gamepad2.right_trigger != 0) {
			rightLift.setPower(liftUpPower);
			leftLift.setPower(liftUpPower);
		} else if(gamepad1.left_trigger != 0 || gamepad2.left_trigger != 0) {
			
			rightLift.setPower(liftDownPower);
			leftLift.setPower(liftDownPower);
		} else {
			rightLift.setPower(LIFT_STOP_POWER);
			leftLift.setPower(LIFT_STOP_POWER);
		}
		
		telemetry.addLine();
		telemetry.addData("Gamepad1 left", gamepad1.left_trigger);
		telemetry.addData("Gamepad2 left", gamepad2.left_trigger);
		telemetry.addLine();
		telemetry.addData("Gamepad1 right", gamepad1.right_trigger);
		telemetry.addData("Gamepad2 right", gamepad2.right_trigger);
		
	}
	
	private void runGripper() {
		
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
		
		// --- Upper Gripper ---
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
		
	}
	
}
