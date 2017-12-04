package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by MerrittAM on 11/22/2017.
 */
@Autonomous(name = "Race_Starter", group = "Autonomous")
public class Race_Starter extends Auto {

	@Override
	public void runOpMode() throws InterruptedException {
		
		initialize();
		
		ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
		
		waitForStart();
		
		time.reset();
		while(10 - time.time() > 1 && !isStopRequested()) {
			
			telemetry.addLine(Integer.toString((int)(10 - time.time())));
			telemetry.update();
			
		}
		
		knockerOffer.setPosition(knockerOfferLowered);
		telemetry.addLine("Go!");
		telemetry.update();
		
	}
}
