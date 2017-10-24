package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by MerrittAM on 10/19/2017.
 * TeleOp v.1
 */
@TeleOp(name = "TeleOp_v1", group = "TeleOp")
public class TeleOp_v1 extends OpMode {
	
    private double liftUpPower = .4;
    private double liftDownPower = -.4;
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
    private final String CALIBRATIOIN_FILEPATH = "/sdcard/FIRST/Calibration.txt";
	
    @Override
    public void init() {

        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        backRight = hardwareMap.dcMotor.get("br");
        backLeft = hardwareMap.dcMotor.get("bl");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap.dcMotor.get("leftLift");

        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);

        rightGripper = hardwareMap.servo.get("rightGripper");
        leftGripper = hardwareMap.servo.get("leftGripper");
        telemetry.addLine("Hardware mapping complete.");
        
        //TODO: Replace data loading with CalibrationManager.
	    
	    CalibrationManager calibrationManager = new CalibrationManager();
	    lServoOpen = Double.parseDouble(calibrationManager.get("lServoOpen"));
	    lServoClose = Double.parseDouble(calibrationManager.get("lServoClose"));
	    rServoOpen = Double.parseDouble(calibrationManager.get("rServoOpen"));
	    rServoClose = Double.parseDouble(calibrationManager.get("rServoClose"));
        liftUpPower = Double.parseDouble(calibrationManager.get("liftUpPower"));
        liftDownPower = Double.parseDouble(calibrationManager.get("liftDownPower"));
	    telemetry.addLine("Calibration data loaded.");
        
//        try {
//
//            FileInputStream filein = new FileInputStream(CALIBRATIOIN_FILEPATH);
//            String data = "";
//
//            int c;
//            while((c = filein.read()) != -1) {
//
//                data += (char)c;
//
//            }
//
//            String[] datalist = data.split("\n");
//
//            if(datalist.length >= 4) {
//
//                lServoOpen = Double.parseDouble(datalist[0]);
//                lServoClose = Double.parseDouble(datalist[1]);
//                rServoOpen = Double.parseDouble(datalist[2]);
//                rServoClose = Double.parseDouble(datalist[3]);
//
//            }
//
//        } catch(FileNotFoundException e) {
//            telemetry.addLine("Error: Calibration file not found.");
//            telemetry.addLine(e.toString());
//        } catch(IOException e) {
//            telemetry.addLine("Error: Problem reading file.");
//            telemetry.addLine(e.toString());
//        }

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

        //Temporarily reduce speed until we get the 40:1 motors.
        frontLeft.setPower(Range.clip(y - x - r, -.5, .5));
        backLeft.setPower(Range.clip(y + x - r, -.5, .5));
        frontRight.setPower(Range.clip(y + x + r, -.5, .5));
        backRight.setPower(Range.clip(y - x + r, -.5, .5));

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
