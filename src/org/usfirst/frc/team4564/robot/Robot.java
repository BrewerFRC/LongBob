package org.usfirst.frc.team4564.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Robot extends SampleRobot {
	public static Xbox j = new Xbox(0);
	public static Xbox j1 = new Xbox(1);
	public static NetworkTable table;
	DriveTrain dt;
	IntakeArm intake;
	

    public void robotInit() {
    	table = NetworkTable.getTable("dashTable");
    	dt = new DriveTrain();
    	intake = new IntakeArm();
    }

    public void autonomous() {
    	long delay = 0;
    	dt.setHeadingHold(true);
    	while (isOperatorControl() && isEnabled()) {
        	long time = Common.time();
    		delay = (long)(time + (1000/Constants.REFRESH_RATE));
    		dt.autoDrive();
    		

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
    	boolean a = false;
    	long delay = 0;
    	while (isOperatorControl() && isEnabled()) {
        	long time = Common.time();
    		delay = (long)(time + (1000/Constants.REFRESH_RATE));
        	
			dt.baseDrive(-j.leftY(), j.leftX());
			if(j.whenA())
			{
				
				a = !a;
				
			}
			if(a){
				intake.intake();
			}else{
				intake.stop_motor();
			}
    		Common.dashNum("encoderA", dt.encoder.getRaw());
    		//Loop wait
    		double wait = (delay-Common.time())/1000.0;
    		if (wait < 0) {
    			wait = 0;
    		}
    		Timer.delay(wait);
    	}
    }
}