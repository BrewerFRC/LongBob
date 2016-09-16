package org.usfirst.frc.team4564.robot;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class IntakeArm 
{
	
	private Solenoid forward;
	private Solenoid backward;
	private Spark intake;
	
	private static final double ARM_POT_LOW = 1;
	private static final double ARM_POT_HIGH = 3;
	private AnalogInput potentiometer = new AnalogInput(Constants.ANA_POT_ARM);
	private double target;

	
	public double getPotentiometerVoltage() {
		return potentiometer.getVoltage();
	}
	
	public IntakeArm()
	{
		forward= new Solenoid(Constants.SOL_INTAKE_FORWARD);
		backward= new Solenoid(Constants.SOL_INTAKE_BACKWARD);
		intake = new Spark(Constants.PWM_INTAKE_MOTOR);
		
	}
	public void up()
	{
		forward.set(false);
		backward.set(true);
	}
	public void down()
	{
		forward.set(true);
		backward.set(false);
	}
	public void intake(double power)
	{
		intake.set(power);
	}
	public void stopMotor()
	{
		intake.set(0);
	}
	public void stop()
	{
		forward.set(false);
		backward.set(false);
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
