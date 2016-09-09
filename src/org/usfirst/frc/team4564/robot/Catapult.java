package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Catapult {
	
	Solenoid sol0;
	Solenoid sol1;
	
	public Catapult(){
		sol0 = new Solenoid(Constants.SOL_CATAPULT_0);
		sol1 =  new Solenoid(Constants.SOL_CATAPULT_1);
		
	}
	public void fire()
	{
		sol0.set(true);
		sol1.set(true);
	}
	public void reset()
	{
		sol0.set(false);
		sol1.set(false);
	}
}
