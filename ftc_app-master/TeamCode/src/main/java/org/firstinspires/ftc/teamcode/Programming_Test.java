package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 9/28/2017.
 */

@TeleOp(name = "Programming_Test", group = "TeleOp")
public class Programming_Test extends OpMode {

    DcMotor motor;

    Gyro imu;

    public void init() {

        motor = hardwareMap.dcMotor.get("motor");

        imu = new Gyro(this);

    }

    public void loop() {


        telemetry.addLine(Float.toString(imu.getOrientation().firstAngle));

//        telemetry.addLine("Hello world!");
        telemetry.update();
        


    }

}
