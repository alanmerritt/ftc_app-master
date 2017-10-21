package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by MerrittAM on 10/19/2017.
 */
@TeleOp(name = "Gripper_Setter", group = "OpMode")
public class Gripper_Setter extends OpMode {

    private Servo rightGripper;
    private Servo leftGripper;

    private double rVal = .5;
    private double lVal = .5;

    @Override
    public void init() {

        rightGripper = hardwareMap.servo.get("rightGripper");
        leftGripper = hardwareMap.servo.get("leftGripper");

    }

    @Override
    public void loop() {

        rightGripper.setPosition(rVal);
        leftGripper.setPosition(lVal);

        rVal += gamepad1.right_stick_y/30;

        if(rVal > 1) rVal = 1;
        if(rVal < 0) rVal = 0;

        lVal += gamepad1.left_stick_y/30;

        if(lVal > 1) lVal = 1;
        if(lVal < 0) lVal = 0;

        telemetry.addData("Right", rVal);
        telemetry.addData("Left", lVal);
        telemetry.update();

    }
}
