
package org.usfirst.frc.team4564.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Robot extends SampleRobot {
	public static Xbox j = new Xbox(0);
	public static Xbox j1 = new Xbox(1);
	public static Solenoid sol0 = new Solenoid(0);
	public static Solenoid sol1 = new Solenoid(1);
	public static Spark motor1 = new Spark(1);
	public static NetworkTable table;
	DriveTrain dt;
	

    public void robotInit() {
    	Common.debug("error?");
    	dt = new DriveTrain();
    	table = NetworkTable.getTable("dashTable");
    	Common.debug("hello");
    }

    public void autonomous() {
    	long delay = 0;
    	dt.setHeadingHold(true);
    	while (isOperatorControl() && isEnabled()) {
        	long time = Common.time();
    		delay = (long)(time + (1000/Constants.REFRESH_RATE));
        	
    		dt.autoDrive();
    		dt.rotateTo(30);
    		//Loop wait
    		double wait = (delay-Common.time())/1000.0;
    		if (wait < 0) {
    			wait = 0;
    		}
    		Timer.delay(wait);
    	}
    }

    public void operatorControl() {
    	dt.init();
    	long delay = 0;
    	while (isOperatorControl() && isEnabled()) {
        	long time = Common.time();
    		delay = (long)(time + (1000/Constants.REFRESH_RATE));
        	
			dt.baseDrive(-j.leftY(), j.leftX());
    		//Loop wait
    		double wait = (delay-Common.time())/1000.0;
    		if (wait < 0) {
    			wait = 0;
    		}
    		Timer.delay(wait);
    	}
    }
}