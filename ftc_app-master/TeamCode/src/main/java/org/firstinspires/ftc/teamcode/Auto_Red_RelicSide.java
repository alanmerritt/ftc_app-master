package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 11/9/2017.
 */
@Autonomous(name = "Auto_Red_RelicSide", group = "Autonomous")
public class Auto_Red_RelicSide extends Auto {
	
	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize();
		
		waitForStart();
		
		grabAndLift();
		
		redKnockOff();
		
		//moveToFirstColumn();
		
		lowerAndRelease();
		
		sleep(500);
		
	}
	
	private void moveToFirstColumn() {
		
		telemetry.addLine("Driving forward.");
		telemetry.update();
		driveMotors(.4, .4, .4, .4);
		sleep(1000);
		
		telemetry.addLine("Stopping.");
		telemetry.update();
		stopDriveMotors();
		sleep(500);
		
		double yaw = gyro.getYaw();
		while(yaw > -45) {
			
			telemetry.addLine("Rotating.");
			telemetry.addData("Yaw", yaw);
			telemetry.update();
			
			driveMotors(.35, -.35, -.35, .35);
			yaw = gyro.getYaw();
		}
		
		stopDriveMotors();
		
	}
	
}
