package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Robot extends SampleRobot {
	public static VisionTracking vision;
	private static Robot instance;
	private static Xbox j = new Xbox(0);
	private static Xbox j1 = new Xbox(1);
	private static NetworkTable table;
	private DriveTrain dt;
	private IntakeArm intake;
	private Catapult cat;
	private Auto auto;
	

    public void robotInit() {
    	instance = this;
    	table = NetworkTable.getTable("dashTable");
    	dt = new DriveTrain();
    	vision = new VisionTracking(dt);
    	intake = new IntakeArm();
    	cat = new Catapult();
    	auto = new Auto(dt);
    }

    public void autonomous() {
    	Common.debug("Starting Auto...");
        auto.init();
        double matchTimer = Common.time();
        while (isAutonomous() && isEnabled()) {
           	//Loop delay timer
        	long time = Common.time();
    		long delay = (long)(time + (1000/Constants.REFRESH_RATE));
    		
    		//Perform autorun
        	auto.autoRun();
    		
    		//Delay timer
    		double wait = (delay-Common.time())/1000.0;
    		if (wait < 0) {
    			wait = 0;
    		
    		}
    		Timer.delay(wait);
        }
        dt.setHeadingHold(false);
    }

    public void operatorControl() {
    	dt.init();
    	boolean a = false,b = false;
    	long delay = 0;
    	while (isOperatorControl() && isEnabled()) {
        	long time = Common.time();
    		delay = (long)(time + (1000/Constants.REFRESH_RATE));
        	
			dt.baseDrive(-j.leftY(), j.leftX());

			if(j.A()) {
				intake.intake(.8);
			}else if(j.B())
			{
				intake.intake(-.8);
			}
			else {
				intake.stopMotor();
			}
			//intake.update();
			//Catapult
			if(j.Y() && j.rightTriggerPressed())
			{
				cat.fire();
			} 
			else {
				cat.reset();
			}
			//Intake arm
			if (j.rightY()<-.7) {
				intake.up();
				intake.intake(0.3);
			}
			else if (j.rightY()>.7) {
				intake.down();
				intake.intake(0);
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
    public Catapult getCatapult() {
    	return cat;
    }
    
    
    public static Robot getInstance() {
    	return instance;
    }
}