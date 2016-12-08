package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;


public class Cannon {
	private Relay can0;
	private Relay can1;
	private double fireTime;
	
	public Cannon() {
		can0 = new Relay(Constants.REL_CANNON_0);
		can1 = new Relay(Constants.REL_CANNON_1);
	}
	
	public void reset() {
		if(fireComplete())
			can0.set(Relay.Value.kOff);		
	}

	public void fire0() {
		can0.set(Relay.Value.kForward);
		fireTime = Common.time()+100;
	}
	
	public void fire1(){
		can0.set(Relay.Value.kReverse);
		fireTime = Common.time()+100;
	}
	public boolean fireComplete()
	{
		if(fireTime < Common.time())
			return true;
		return false;
	}

}
