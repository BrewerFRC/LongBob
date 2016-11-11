package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionTracking {
	NetworkTable visionTable;
	DriveTrain dt;
	int shoot_count = 0;
	public PID Tracking = new PID(0, 0, 0, false, "Tracking PID");

	public static final double OUTER_TARGET_DISTANCE = 58;
	public static final double TARGET_DISTANCE = 49 - 3; // Inches from tower
															// for shot
															// alignment, as
															// measured by
															// utlrasonic,
															// batter distance -
															// bumper to wheel
															// distance
	long counter = 0;
	boolean backward = false;

	public VisionTracking(DriveTrain dt) {
		this.dt = dt;
		this.visionTable = NetworkTable.getTable("visionTracking");
	}

	public boolean autoAim() {
		// dt.setHeadingHold(false); //Aiming requires that heading hold be off
		dt.setHeadingHold(true); // Aiming requires that heading hold be off
		double distance; // Distance from tower, measured by ultrasonic.
		double speed; // The speed at which to move to/from tower to get proper
		// distance for shot
		boolean shoot = false; // True when targeting computer issues '999'
								// response
		double turn = 0; // Turn speed, provided by targeting computer and adjusted
						// for pulse drive
		Tracking.setMin(0.45);
		Tracking.setTarget(0);
		Tracking.setOutputLimits(-0.7, 0.7);

		// Process targeting computer data
		// double targetingData = visionTable.getNumber("targetTurn", 0);
		double error = visionTable.getNumber("error", 0);
		double P_GAIN = visionTable.getNumber("P_GAIN", 0);
		double I_GAIN = visionTable.getNumber("I_GAIN", 0);
		double D_GAIN = visionTable.getNumber("D_GAIN", 0);
		Tracking.setP(P_GAIN);
		Tracking.setI(I_GAIN);
		Tracking.setD(D_GAIN);
		double heading = Robot.getInstance().getDriveTrain().heading.getHeading() - 180;
		SmartDashboard.putNumber("TargetingData", error);
		if (error != 800) {
			if (Math.abs(error) < 3) {
				shoot_count++;
			} else {
				shoot_count = 0;
			}
			if (shoot_count >10) {
				shoot = true;
				turn = 0;
				shoot_count = 0;
			} else {
				turn = Tracking.calc(error);
				shoot = false;
			}
		}

		// Drive forward/backward until proper distance from tower
		// ***** Insert Ultrasonic logic here
		// Is it time to shoot or keep aiming?
		SmartDashboard.putNumber("turning", turn);
		SmartDashboard.putNumber("shoot_count", shoot_count);
		speed = -0.4;
		boolean thrown = false;
		if (shoot) { // If we are aligned and not driving and...
			Robot.vision.takePicture();
			Robot.getInstance().getCatapult().fire();
			thrown = true;

		}

		// Move the robot as necessary
		dt.setDriveSpeed(speed);
//		if (turn > 0) {
//			turn *= 0.95;
//		}else{
//			turn *=1.125;
//		}
		dt.setTurnSpeed(turn);

		return thrown;
	}

	public void takePicture() {
		Common.debug("AutoAim: taking picture");
		visionTable.putNumber("takePicture", 1);
	}
}
