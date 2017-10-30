package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.calibration.CalibrationManager;

/**
 * Created by MerrittAM on 10/19/2017.
 * TeleOp v.1
 */
@TeleOp(name = "TeleOp_v1", group = "TeleOp")
public class TeleOp_v1 extends OpMode {
	
	private double liftUpPower = .35;
	private double liftDownPower = -.35;
	private final double LIFT_STOP_POWER = 0;
	
	private double lServoOpen;
	private double lServoClose;
	private double rServoOpen;
	private double rServoClose;
	
	private boolean gripperPressedLast = false;
	private boolean gripperOn = false;
	
	private DcMotor frontLeft;
	private DcMotor frontRight;
	private DcMotor backRight;
	private DcMotor backLeft;
	
	private DcMotor rightLift;
	private DcMotor leftLift;
	
	private Servo rightGripper;
	private Servo leftGripper;
	
	//Looks like someone can't spell...
//	private final String CALIBRATIOIN_FILEPATH = "/sdcard/FIRST/Calibration.txt";
	
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
		
		telemetry.addData("Default runmode", frontLeft.getMode());
		
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

		rightLift = hardwareMap.dcMotor.get("rightLift");
		leftLift = hardwareMap.dcMotor.get("leftLift");
		
		leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
		
		leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		
		rightGripper = hardwareMap.servo.get("rightGripper");
		leftGripper = hardwareMap.servo.get("leftGripper");
		telemetry.addLine("Hardware mapping complete.");
		
		CalibrationManager calibrationManager = new CalibrationManager(telemetry);
		lServoOpen = Double.parseDouble(calibrationManager.get("lServoOpen"));
		lServoClose = Double.parseDouble(calibrationManager.get("lServoClose"));
		rServoOpen = Double.parseDouble(calibrationManager.get("rServoOpen"));
		rServoClose = Double.parseDouble(calibrationManager.get("rServoClose"));
		liftUpPower = -Double.parseDouble(calibrationManager.get("liftPower"));
		liftDownPower = Double.parseDouble(calibrationManager.get("liftPower"));
		telemetry.addLine("Calibration data loaded.");
		
		telemetry.update();

	}

	@Override
	public void loop() {

		drive();
		runLift();
		runGripper();

	}

	private void drive() {

		double x = gamepad1.left_stick_x;
		double y = gamepad1.left_stick_y;
		double r = gamepad1.right_stick_x;

		
		frontLeft.setPower(Range.clip(y - x - r, -1, 1));
		backLeft.setPower(Range.clip(y + x - r, -1, 1));
		frontRight.setPower(Range.clip(y + x + r, -1, 1));
		backRight.setPower(Range.clip(y - x + r, -1, 1));

	}

	private void runLift() {

		if(gamepad1.right_trigger != 0) {
			rightLift.setPower(liftUpPower);
			leftLift.setPower(liftUpPower);
		} else if(gamepad1.left_trigger != 0) {
			rightLift.setPower(liftDownPower);
			leftLift.setPower(liftDownPower);
		} else {
			rightLift.setPower(LIFT_STOP_POWER);
			leftLift.setPower(LIFT_STOP_POWER);
		}

	}

	private void runGripper() {

		if(gamepad1.a && !gripperPressedLast) {
			gripperOn = !gripperOn;
		}

		if(gripperOn) {
			leftGripper.setPosition(lServoClose);
			rightGripper.setPosition(rServoClose);
		} else {
			leftGripper.setPosition(lServoOpen);
			rightGripper.setPosition(rServoOpen);
		}

		gripperPressedLast = gamepad1.a;

	}

}
