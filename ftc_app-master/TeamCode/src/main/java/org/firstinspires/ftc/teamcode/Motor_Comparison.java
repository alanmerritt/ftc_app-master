package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MerrittAM on 10/27/2017.
 */
@TeleOp(name = "Motor_Comparison", group = "TeleOp")
public class Motor_Comparison extends OpMode {
	
	private DcMotor nr40;
	private DcMotor nr20;
	
	public void init() {
		
		nr40 = hardwareMap.dcMotor.get("nr40");
		nr20 = hardwareMap.dcMotor.get("nr20");
		
	}
	
	public void loop() {
		
		nr40.setPower(1);
		nr20.setPower(.5);
		
	}
	
}
