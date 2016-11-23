package org.usfirst.frc.team4564.robot;


import edu.wpi.first.wpilibj.Solenoid;

public class Catapult {
	private Solenoid sol0;
	private Solenoid sol1;
	private double fireTime;
	

	

	public Catapult() {
		sol0 = new Solenoid(Constants.SOL_CATAPULT_0);
		sol1 = new Solenoid(Constants.SOL_CATAPULT_1);
	}

	public void fire() {
		if (!(Robot.getInstance().getIntake().ballLoaded())) {
			sol0.set(true);
			sol1.set(true);
			fireTime = Common.time() + 500;
		}
	}

	public void update() {
		if (fireComplete()) {
			sol0.set(false);
			sol1.set(false);
		}
	}

	public boolean fireComplete() {
		return (fireTime < Common.time());
	}
}
