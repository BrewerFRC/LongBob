package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	public static VisionTracking vision;
	private static Robot instance;
	private static Xbox j0 = new Xbox(0);
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
			// Loop delay timer
			long time = Common.time();
			long delay = (long) (time + (1000 / Constants.REFRESH_RATE));

			// Perform autorun
			auto.autoRun();

			// Delay timer
			double wait = (delay - Common.time()) / 1000.0;
			if (wait < 0) {
				wait = 0;

			}
			Timer.delay(wait);
			Common.dashNum("encoderA", dt.getEncoder().getRaw());
			Common.dashNum("encoderINCHES", dt.getEncoder().getDistance());
		}
		dt.setHeadingHold(false);
	}

	public void operatorControl() {
		dt.init();
		boolean a = false, b = false, firstVision = false;
		long delay = 0;
		while (isOperatorControl() && isEnabled()) {
			Xbox j;
			long time = Common.time();
			delay = (long) (time + (1000 / Constants.REFRESH_RATE));

			// Auto aim
			if (j0.leftTriggerPressed() || j1.leftTriggerPressed()) {
				if(!firstVision) vision.Tracking.reset();
				firstVision = true;
				vision.autoAim();
				dt.autoDrive();
			} else {
				firstVision = false;
				// driving
				if (Math.abs(j0.leftX()) > 0 || Math.abs(j0.leftY()) > 0) {

					j = j0;
				} else {
					j = j1;
				}

				if (j == j0) {
					dt.baseDrive(-j.leftY(), j.leftX());
				} else {
					dt.baseDrive(j.leftY(), j.leftX());
				}

				// intake
				if (j0.A() || j0.B()) {
					j = j0;
				} else {
					j = j1;
				}

				if (j.A()) {
					intake.intake(.9);
				} else if (j.B()) {
					intake.intake(-.9);
				} else {
					intake.stopMotor();
				}
				// Catapult
				if ((j1.Y() && j1.rightTriggerPressed()) || (j0.Y() && j0.rightTriggerPressed())) {
					cat.fire();
				}
				cat.update();

				// Intake arm
				if (Math.abs(j0.rightY()) > .7) {
					j = j0;
				} else {
					j = j1;
				}
				if (j.rightY() < -.7) {
					intake.up();
					intake.intake(0.4);
				} else if (j.rightY() > .7) {
					intake.down();
					intake.intake(0);
				} else {
					intake.stop();
				}
				Common.dashNum("encoderA", dt.getEncoder().getRaw());
				Common.dashNum("encoderINCHES", dt.getEncoder().getDistance());
				Common.dashNum("Heading", dt.heading.getHeading());
			}
			// Loop wait
			double wait = (delay - Common.time()) / 1000.0;
			if (wait < 0) {
				wait = 0;
			}
			Timer.delay(wait);

		}
	}

	public NetworkTable getDashTable() {

		return table;

	}
	public DriveTrain getDriveTrain(){
		return dt;
	}

	public Catapult getCatapult() {
		return cat;
	}

	public IntakeArm getIntake() {
		return intake;
	}

	public static Robot getInstance() {
		return instance;
	}
}