package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Auto;

/**
 * Created by MerrittAM on 11/13/2017.
 */
@Autonomous(name = "Auto_Red_BackSide", group = "Autonomous")
public class Auto_Red_BackSide extends Auto {
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize();
		
		waitForStart();
		
		grabAndLift();
		
		redKnockOff();
		
		moveToFirstColumn();
		
		lowerAndRelease();
		
		sleep(500);
		
	}
	
	private void moveToFirstColumn() {
	
	
	
	}
	
}

