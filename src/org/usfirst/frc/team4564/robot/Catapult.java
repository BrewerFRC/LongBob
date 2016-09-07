package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Catapult {
	
	Solenoid Forward;
	Solenoid Backward;
	
	public Catapult(){
		Forward = new Solenoid(Constants.SOL_CATAPULT_FORWARD);
		Backward = new Solenoid(Constants.SOL_CATAPULT_BACKWARD);
		
	}
	public void fire()
	{
		Forward.set(true);
		Backward.set(false);
	}
	public void reset()
	{
		Forward.set(false);
		Backward.set(true);
	}
	public void stop()
	{
		Forward.set(false);
		Backward.set(false);
	}
	
}
