package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by MerrittAM on 10/20/2017.
 * OpMode used for calibrating parts of the robot without reinstalling the program.
 */

public class Calibration extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        
        boolean aPressedLast = false;
	    
        Servo leftGripper = hardwareMap.servo.get("leftGripper");
        double lGripperOpen = .5;
        double lGripperClosed = .5;
        double rGripperOpen = .5;
        double rGripperClosed = .5;
        //TODO: Replace variables with CalibrationManager.
        
        waitForStart();
        
        
        boolean upPressedLast = false;
        boolean downPressedLast = false;
        while(gamepad1.a && !aPressedLast) {
            
            if(gamepad1.dpad_up && !upPressedLast) lGripperOpen += .05;
            if(gamepad1.dpad_down && !downPressedLast) lGripperOpen -= .05;
            leftGripper.setPosition(lGripperOpen);
            
            telemetry.addLine("Set left servo gripper open position.");
            telemetry.addData("Position", lGripperOpen);
            telemetry.update();
            
            aPressedLast = gamepad1.a;
            
        }
        
        //TODO: Export data here.
        
    }
}
