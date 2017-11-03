package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by MerrittAM on 9/30/2017.
 * Class used for easily accessing the imu
 * builtin to the Rev Expansion Hub.
 *
 * It assumes that the imu is named "imu" in
 * the robot configuration file.
 */

public class Gyro {

    BNO055IMU imu;
	
	private double totalYaw = 0;
	private double lastYaw = 0;
	
    public Gyro(OpMode opMode) {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }
	
	/**
	 * Gets the rotational orientation of the robot.
	 * @return The rotation of the robot. Angles are in ZYX order.
	 */
	public Orientation getOrientation() {

        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

    }
	
	/**
	 *
	 * @return
	 */
	public double getYaw() {
	    
	    return getOrientation().firstAngle;
	    
    }
    
    public void updateYaw() {
	    
	    double yaw = getYaw();
	    
	    double diff = yaw - lastYaw;
	    
	    if(diff >= 180) {
		    diff -= 360;
	    }
	    if(diff < -180) {
		    diff += 360;
	    }
	    
	    totalYaw += diff;
	    
	    lastYaw = yaw;
	    
    }
    
    public double getTotalYaw() {
	    
	    return totalYaw;
	    
    }
    
}
