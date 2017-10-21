package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by MerrittAM on 10/19/2017.
 * TeleOp v.1
 */
@TeleOp(name = "TeleOp_v1", group = "TeleOp")
public class TeleOp_v1 extends OpMode {

    private final double LIFT_UP_POWER = .4;
    private final double LIFT_DOWN_POWER = -.4;
    private final double LIFT_STOP_POWER = 0;

    private double lservoOpen;
    private double lservoClose;
    private double rservoOpen;
    private double rservoClose;

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

        try {

            FileInputStream filein = new FileInputStream(CALIBRATIOIN_FILEPATH);
            String data = "";

            int c;
            while((c = filein.read()) != -1) {

                data += (char)c;

            }

            String[] datalist = data.split("\n");

            if(datalist.length >= 4) {

                lservoOpen = Double.parseDouble(datalist[0]);
                lservoClose = Double.parseDouble(datalist[1]);
                rservoOpen = Double.parseDouble(datalist[2]);
                rservoClose = Double.parseDouble(datalist[3]);

            }

        } catch(FileNotFoundException e) {
            telemetry.addLine("Error: Calibration file not found.");
            telemetry.addLine(e.toString());
        } catch(IOException e) {
            telemetry.addLine("Error: Problem reading file.");
            telemetry.addLine(e.toString());
        }

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

        if(gamepad2.right_trigger != 0) {
            rightLift.setPower(LIFT_UP_POWER);
            leftLift.setPower(LIFT_UP_POWER);
        } else if(gamepad2.left_trigger != 0) {
            rightLift.setPower(LIFT_DOWN_POWER);
            leftLift.setPower(LIFT_DOWN_POWER);
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
            leftGripper.setPosition(lservoClose);
            rightGripper.setPosition(rservoClose);
        } else {
            leftGripper.setPosition(lservoOpen);
            rightGripper.setPosition(rservoOpen);
        }

        gripperPressedLast = gamepad1.a;

    }

}
