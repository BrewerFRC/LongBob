package org.usfirst.frc.team4564.robot;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class IntakeArm 
{
	
	Solenoid Forward;
	Solenoid Backward;
	Spark Intake;
	
	private static final double ARM_POT_LOW = 1;
	private static final double ARM_POT_HIGH = 3;
	public AnalogInput potentiometer = new AnalogInput(Constants.ANA_POT_ARM);
	public double target;

	
	public double getPotentiometerVoltage() {
		return potentiometer.getVoltage();
	}
	
	public IntakeArm()
	{
		Forward = new Solenoid(Constants.SOL_INTAKE_FORWARD);
		Backward = new Solenoid(Constants.SOL_INTAKE_BACKWARD);
		Intake = new Spark(Constants.PWM_INTAKE_MOTOR);
		
	}
	public void up()
	{
		Forward.set(true);
		Backward.set(false);
	}
	public void down()
	{
		Forward.set(false);
		Backward.set(true);
	}
	public void intake()
	{
		Intake.set(.4);
	}
	public void stop_motor()
	{
		Intake.set(0);
	}
	public void stop()
	{
		Forward.set(false);
		Backward.set(false);
	}
	public void setIntakePosition(int position)
	{
		//autoControl = true
		if (position < 0){
		position = 0;
		}
		if (position > 6){
			position =6;
		}
		target = ARM_POT_LOW + (ARM_POT_HIGH - ARM_POT_LOW) / 6 * position;
		Common.debug("IntakeArm: SetIntakePosition: Set arm target" +target);
	}
	
}
