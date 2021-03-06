package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class Heading {
	private ADXRS450_Gyro gyro;
	//PID takes cumulative angles
	private PID pid;
	private boolean headingHold;
	
	public Heading(double p, double i, double d, double sensitivity) {
		pid = new PID(Constants.TURN_P, Constants.TURN_I, Constants.TURN_D, false, "gyro");
		//PID is dealing with error; an error of 0 is always desired.
		pid.setTarget(0.0);
		pid.setMin(0.39);
		pid.setOutputLimits(-1, 1);
		gyro = new ADXRS450_Gyro();
	}
	
	public void update() {
		pid.update();
	}
	
	//Resets PID and gyro
	public void reset() {
		gyro.reset();
		resetPID();
	}
	
	//Resets PID
	public void resetPID() {
		pid.reset();
	}
	
	
	public void setPID(double p, double i, double d) {
		pid.setP(p);
		pid.setI(i);
		pid.setD(d);
	}
	
	//This returns the targeted angle in degrees
	public double getTargetAngle() {
		return pid.getTarget(); 
	}
	
	//This returns the targeted heading in degrees
	public double getTargetHeading(){
		return angleToHeading(pid.getTarget());
	}
	
	//Convert angle to heading in partial degrees, 0.01 accuracy
	public double angleToHeading(double angle) {
		double heading = (angle * 100) % 36000 / 100;
		if (heading < 0) {
			heading += 360;
		}
		return heading; 
	}
	
	//Returns current heading
	public double getHeading() {
		return angleToHeading(getAngle());
	}
	
	//Returns current angle
	public double getAngle() {
		return gyro.getAngle();
	}
	
	// Sets target angle given a heading, and will turn left or right to target dependent on which is shortest
	public void setHeading(double heading) {
		//Find the short and long path to the desired heading.
		double changeLeft = (-360 + (heading - getHeading())) % 360;
		double changeRight = (360 - (getHeading() - heading)) % 360;
		double change = (Math.abs(changeLeft) < Math.abs(changeRight)) ? changeLeft : changeRight;
		pid.setTarget(getAngle() + change);
	}
	
	//Turn a number of degrees relative to current heading
	public void relTurn(double degrees){
		pid.setTarget(getAngle() + degrees);
	}
	
	// Activates or deactivates heading hold.  If setting heading hold, it will reset the PID and and set target heading to current heading
	public void setHeadingHold(boolean headingHold) {
		if (headingHold) {
			resetPID();
			this.headingHold = true;
			//Set target angle to current heading.
			setHeading(getHeading());
		}
		else {
			resetPID();
			this.headingHold = false;
		}
	}
	
	//Returns state of heading hold
	public boolean isHeadingHold() {
		return headingHold;
	}
	
	// This returns the turn power required to turn to target heading.  If heading hold is off, turn rate is always 0.
	public double turnRate() {
		if (headingHold) {
			//Return the PID calculation of the shorter path.
			return -1 * pid.calc(gyro.getAngle());
		}
		else {
			return 0.0;
		}
	}
}
