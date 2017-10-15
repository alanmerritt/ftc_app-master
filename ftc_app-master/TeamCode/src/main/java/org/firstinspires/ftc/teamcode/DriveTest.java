package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by MerrittAM on 10/4/2017.
 * Testing the drive system.
 */
@TeleOp(name = "DriveTest", group = "TeleOp")
public class DriveTest extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    private enum DriveMode { TANK, HOLONOMIC }

    private DriveMode driveMode = DriveMode.TANK;

    //Edge detection for drive mode switching.
    private boolean modeSwitchOld;

    @Override
    public void init() {

        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        backRight = hardwareMap.dcMotor.get("br");
        backLeft = hardwareMap.dcMotor.get("bl");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("Initialized.");
        telemetry.update();

    }

    @Override
    public void loop() {

        switch(driveMode) {

            //Tank drive mode.
            case TANK:
                tankDrive();
                if(gamepad1.right_trigger != 0 && !modeSwitchOld) driveMode = DriveMode.HOLONOMIC;
                break;

            //Holonomic drive mode.
            case HOLONOMIC:
                holonomicDrive();
                if(gamepad1.right_trigger != 0 && !modeSwitchOld) driveMode = DriveMode.TANK;
                break;

            default:

                break;

        }


        modeSwitchOld = gamepad1.right_trigger != 0;
        telemetry.addData("Drive Mode", driveMode.toString());
        telemetry.update();

    }

    /**
     * Drives the robot in tank drive mode.
     */
    private void tankDrive() {

        double leftY = gamepad1.left_stick_y;
        double rightY = gamepad1.right_stick_y;


        frontLeft.setPower(-leftY);
        backLeft.setPower(-leftY);
        frontRight.setPower(-rightY);
        backRight.setPower(-rightY);

    }

    private void holonomicDrive() {

        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;

        frontLeft.setPower(Range.clip(y - x - r, -.5, .5));
        backLeft.setPower(Range.clip(y + x - r, -.5, .5));
        frontRight.setPower(Range.clip(y + x + r, -.5, .5));
        backRight.setPower(Range.clip(y - x + r, -.5, .5));

    }

}
