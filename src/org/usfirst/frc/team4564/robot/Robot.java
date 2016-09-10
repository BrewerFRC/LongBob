package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Robot extends SampleRobot {
	private static Robot instance;
	private static Xbox j = new Xbox(0);
	private static Xbox j1 = new Xbox(1);
	private static NetworkTable table;
	private DriveTrain dt;
	private IntakeArm intake;
	private Catapult cat;
	

    public void robotInit() {
    	instance = this;
    	table = NetworkTable.getTable("dashTable");
    	dt = new DriveTrain();
    	intake = new IntakeArm();
    	cat = new Catapult();
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
    	boolean a = false,b = false;
    	long delay = 0;
    	while (isOperatorControl() && isEnabled()) {
        	long time = Common.time();
    		delay = (long)(time + (1000/Constants.REFRESH_RATE));
        	
			dt.baseDrive(-j.leftY(), j.leftX());
			//Toggle intake
//			if(j.whenA())
//			{
//				a = !a;
//			}
//			if(j.whenB() && a == false)
//			{
//				b = !b;
//			}
			if(j.A()) {
				intake.intake(.8);
			}else if(j.B())
			{
				intake.intake(-.8);
			}
			else {
				intake.stopMotor();
			}
			
			//Catapult
			if(j.Y() && j.rightTriggerPressed())
			{
				cat.fire();
			} 
			else {
				cat.reset();
			}
			
			//Intake arm
			if (j.dpadUp()) {
				intake.up();
			}
			else if (j.dpadDown()) {
				intake.down();
			}
			else {
				intake.stop();
			}
    		Common.dashNum("encoderA", dt.getEncoder().getRaw());
    		//Loop wait
    		double wait = (delay-Common.time())/1000.0;
    		if (wait < 0) {
    			wait = 0;
    		}
    		Timer.delay(wait);
    	}
    }
    
    public NetworkTable getDashTable() {
    	return table;
    }
    
    public static Robot getInstance() {
    	return instance;
    }
}