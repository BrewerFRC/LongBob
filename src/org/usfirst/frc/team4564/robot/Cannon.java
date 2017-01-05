package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;


public class Cannon {
	private Solenoid sol0;
	private Solenoid sol1;
	private double fireTime;
	
	public Cannon(int pin1) {
		sol0 = new Solenoid(pin1);
	}
	
	public void fire() {
		//if (!(Robot.getInstance().getIntake().ballLoaded())) {
			sol0.set(true);
			fireTime = Common.time() + 25;
		
	}

	public void update() {
		if (fireComplete()) {
			sol0.set(false);
		
		}
	}

	public boolean fireComplete() {
		return (fireTime < Common.time());
	}
}
