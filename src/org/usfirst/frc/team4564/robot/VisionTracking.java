package org.usfirst.frc.team4564.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionTracking {
	NetworkTable visionTable;
	DriveTrain dt;
	
	public static final double OUTER_TARGET_DISTANCE = 58;
	public static final double TARGET_DISTANCE = 49-3;		// Inches from tower for shot alignment, as measured by utlrasonic, batter distance - bumper to wheel distance
	long counter = 0;
	boolean backward = false;
	
	public VisionTracking(DriveTrain dt) {
		this.dt = dt;
		this.visionTable = NetworkTable.getTable("visionTracking");
	}

	public boolean autoAim() {
		dt.setHeadingHold(false);		//Aiming requires that heading hold be off
		double distance;				//Distance from tower, measured by ultrasonic.
		double speed;					//The speed at which to move to/from tower to get proper distance for shot
		boolean shoot;					//True when targeting computer issues '999' response
		double turn;					//Turn speed, provided by targeting computer and adjusted for pulse drive
		
		// Process targeting computer data
		double targetingData = visionTable.getNumber("targetTurn", 0);  
		if (targetingData == 999) {
			shoot = true;
			turn = 0;
		} else {
			shoot = false;
			if (counter % 6 < 2) { 		//If not aligned for shot, pulse the turn rate between full power and 1/3 power
				turn = targetingData;
			} else {
				turn = targetingData * 0.3;
			}
			counter++;			
		}
		
		// Drive forward/backward until proper distance from tower
		// ***** Insert Ultrasonic logic here
		// Is it time to shoot or keep aiming?
		speed = -0.3;
		boolean thrown = false;
		if (shoot ) {    // If we are aligned and not driving and...
				Robot.getInstance().getCatapult().fire();
				thrown = true;

		}
		
		// Move the robot as necessary
		dt.setDriveSpeed(speed);
		dt.setTurnSpeed(turn);

		return thrown;
	}
	
	public void takePicture() {
		Common.debug("AutoAim: taking picture");
		visionTable.putNumber("takePicture", 1);
	}
}
