package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by MerrittAM on 11/14/2017.
 */
@Autonomous(name = "Auto_Blue_Backside", group = "Autonomous")
public class Auto_Blue_BackSide extends Auto {
	@Override
	public void runOpMode() throws InterruptedException {
		
		
		initialize();
		
		waitForStart();
		
		grabAndLift();
		
		blueKnockOff();
		
		//moveToFirstColumn();
		
		lowerAndRelease();
		
		sleep(500);
		
		
	}
}
