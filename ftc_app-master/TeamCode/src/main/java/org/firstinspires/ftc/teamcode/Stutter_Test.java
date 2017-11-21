package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by MerrittAM on 11/20/2017.
 * Used to test the robot power stutter.
 */
@Autonomous(name = "Stutter_Test", group = "Autonomous")
public class Stutter_Test extends Auto {
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize();
		
		waitForStart();
		
		driveMotors(1, 1, 1, 1);
		
		sleep(5000);
		
	}
}
