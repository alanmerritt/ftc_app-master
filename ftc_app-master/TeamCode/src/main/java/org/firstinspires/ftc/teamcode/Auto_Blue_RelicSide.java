package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 11/14/2017.
 */
@Autonomous(name = "Auto_Blue_RelicSide", group = "Autonomous")
public class Auto_Blue_RelicSide extends Auto {
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
