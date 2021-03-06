package org.usfirst.frc.team4564.robot;

public class PID {
	private Robot robot;
	//Terms
	private double p;
	private double i;
	private double d;
	private String name;
	
	/* 
	 * Whether or not to cummulate values over time.
	 * True = cummulate, False = raw return value
	 */
	private boolean forward;
	private double target;
	private double Outmin = -1;
	private double Outmax = 1;
	private double min;
	private long deltaTime;
	private double previousError;
	private double sumError;
	private double output;
	private double lastCalc;
	
	public PID(double p, double i, double d, boolean forward, String name) {
		robot = Robot.getInstance();
		this.p = p;
		this.i = i;
		this.d = d;
		this.name = name;
		this.forward = forward;
		this.deltaTime = (long)(1.0/Constants.REFRESH_RATE*1000);
		
		robot.getDashTable().putNumber(name + "P", p);
		robot.getDashTable().putNumber(name + "I", i);
		robot.getDashTable().putNumber(name + "D", d);
	}
	
	public void update() {
		this.p = robot.getDashTable().getNumber(name + "P", this.p);
		this.i = robot.getDashTable().getNumber(name + "I", this.i);
		this.d = robot.getDashTable().getNumber(name + "D", this.d);
	}
	
	public void setOutputLimits(double min, double max){
		this.Outmin = min;
		this.Outmax = max;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getTarget() {
		return this.target;
	}
	public void setTarget(double target) {
		this.target = target;
	}
	public void setP(double p) {
		this.p = p;
	}
	public void setI(double i) {
		this.i = i;
	}
	public void setD(double d) {
		this.d = d;
	}
	public void reset() {
		sumError = 0;
		previousError = 0;
	}
	public double getLastCalc() {
		return this.lastCalc;
	}
	
	public double calc(double input) {
		
		//Integral calculation
		double error = input - target;
		sumError += error * deltaTime;
		int sign = -1;
		
		//Derivative calculation
		double derivative = (error - previousError) / deltaTime;
		previousError = error;
		
		//Calculate output
		double output = p*error + i*sumError + d*derivative;
		if(output>0) 
			sign = 1;
		output = Math.abs(output)+ min;
		output *= sign;
		output = Math.min(Math.max(output, Outmin), Outmax);
		

		if (forward) {
			this.output += output;
		}
		else {
			this.output = output;
		}
		lastCalc = this.output;
		//Common.debug(this.name + "PID_OUT" + this.output );
		return this.output;
	}
}
