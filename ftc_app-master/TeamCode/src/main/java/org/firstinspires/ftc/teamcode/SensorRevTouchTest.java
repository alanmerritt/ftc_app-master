package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by MerrittAM on 9/30/2017.
 */

public class SensorRevTouchTest extends OpMode {

    TouchSensor touchSensor;

    @Override
    public void init() {

        touchSensor = hardwareMap.touchSensor.get("");

    }

    @Override
    public void loop() {

    }
}
