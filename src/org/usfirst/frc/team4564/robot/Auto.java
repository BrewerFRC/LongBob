package org.usfirst.frc.team4564.robot;

public class Auto {
	DriveTrain dt;

	//autoRun parameters loaded from network tables
	public int paramStartingPlatform;
	public int paramDefenseType;	
	public int driveState;
	public int autoRunState = 1;
		
	//autoRunDefense parameter constants
	private static final int DEFENSE_LOWBAR = 1;
	private static final int DEFENSE_CHEVAL_DE_FRISE = 2;
	private static final int DEFENSE_MOAT = 3;
	private static final int DEFENSE_RAMPARTS = 4;
	private static final int DEFENSE_DRAWBRIDGE = 5;
	private static final int DEFENSE_SALLY_PORT = 6;
	private static final int DEFENSE_ROCK_WALL = 7;
	private static final int DEFENSE_ROUGH_TERRAIN = 8;
	private static final int DEFENSE_PORTCULLIS = 9;
	
	private static final int AUTO_SETUP = 1;					//Prep start of autoRun
	private static final int AUTO_DRIVE = 2;					//Drive through the defense, if one is selected
	private static final int AUTO_ALIGN = 3;
	private static final int AUTO_SHOOT = 6;
	private static final int AUTO_ROTATE_TO_GOAL = 4;
	private static final int AUTO_AIM_TOWARD_GOAL = 5;
	private static final int AUTO_COMPLETE = 7;

	
	public static final int NOT_DRIVING = 0;					//Ready to start
	public static final int ARM_DOWN = 8;
	public static final int DRIVING = 1;						//Move robot through or to defense, if moving through defense, wait to see exit.
	public static final int DEFENSE_CROSSED = 2;				//Robot cleared shield and now stopped.
	public static final int LOWER_ARM = 3;						//Wait for arm to reach a position
	public static final int DRIVE_STEP_ONE = 4;		
	public static final int DRIVE_STEP_TWO = 5;		
	public static final int PRE_DEFENSE_ALIGN = 6;		
	public static final int POST_DEFENSE_ALIGN = 7;	

	//Field Dimension constants.
	public static final double PLATFORM_WIDTH = 53.0;
	public static final double ABSOLUTE_CASTLE_X = 170.6113;  // X position of the center of the castle
	public static final double ABSOLUTE_OUTERWORKS_Y = 191.0;  // Y position of the outerworks on courtyard side
	public static final double RIGHT_GOAL_LINE_Y = ABSOLUTE_OUTERWORKS_Y - 151.0; // From the center of 5th platform
	public static final double LEFT_GOAL_LINE_Y = ABSOLUTE_OUTERWORKS_Y - 105.0; // From the center of 1st platform
	public static final double BATTER_DEPTH = 68.0;
	public static final double GOAL_DEPTH = 36.6;
	public static final double DISTANCE_TO_BATTER = ABSOLUTE_OUTERWORKS_Y - BATTER_DEPTH; // Distance of outerworks from batter
	public static final double ROBOT_WIDTH = 35.0;
	public static final double ROBOT_LENGTH = 36.5;

	//Misc variables
	public long driveTime; 					//drive time
	public long lowerTime;
	public double xAbs;              	    //Absolute x position of the robot after clearing a defense. 
//	public double yAbs = 56;			    //Inches from defensive line to center of robot, courtyard side
	public double xTargetCenter;      		//Inches to center of target platform relative to left wall.
	public double xDistanceToTarget;   		//Inches to target platform relative from center of robot.
	public double xDistanceToCastleCenter;  //Inches to castle center relative to robot center
	public double distance;                 //Temporary variable for calculating drive distances;
	public double heading;					//Temporary variable for calculating heading
	
	//Auto constructor
	public Auto(DriveTrain dt) {	
		this.dt = dt;
	}
	
	// Prepare for auto run
	public void init() {
		// Setup drivetrain
		dt.init();
    	dt.setSafetyEnabled(false);
        dt.setHeadingHold(true);
		// Get initial auto parameters from network tables.  They'll default to zero if not specified.
		paramStartingPlatform = (int) Robot.getInstance().getDashTable().getNumber("platform", 0);
		paramDefenseType = (int) Robot.getInstance().getDashTable().getNumber("defense", 0);
	}

				
	// Using the ultrasonic sensor, this logic determines when the robot is within a defense.
	// Use this for the u-turn action
	// Returns true when within a shield.
	//***We may not need this function***
	
	// Given a defenseType, this method will drive the robot through a defense.
	// It will return true when the defense is cleared.
	// This methods is used to autoRun().
	public boolean driveDefense(int defenseType) {
		
		boolean defenseCleared = false;
	
		switch(defenseType) {
		//ROUGH TERRAIN
			case DEFENSE_ROUGH_TERRAIN:
				switch(driveState) {
				case NOT_DRIVING:
					//Input arm move down when applicable
					Robot.getInstance().getIntake().down();
					driveState = DRIVING;
					this.driveTime = Common.time() + 3000;
					break;
				case DRIVING:
					//if (arm.moveCompleted()) {
					if (Common.time() >= driveTime) {
						driveState = DEFENSE_CROSSED;
					}
					break;
				case DEFENSE_CROSSED:
					defenseCleared = true;
					break;				}
				break;
			
			//LOWBAR
			case DEFENSE_LOWBAR: 
				switch(driveState) {
					case NOT_DRIVING:
						//Input arm move down when applicable
						Robot.getInstance().getIntake().down();
						driveState = ARM_DOWN;
						this.lowerTime = Common.time() + 3000;
						break;
					case ARM_DOWN:
						if (Common.time() >= this.lowerTime) {
							Robot.getInstance().getIntake().stop();
							dt.driveDistance(-(84.5+48+3));
							driveState = DRIVING;
						}
						break;
					case DRIVING:
						//if (arm.moveCompleted()) {
						if (dt.driveComplete()) {
							driveState = DEFENSE_CROSSED;
						}
						break;
					case DEFENSE_CROSSED:
						defenseCleared = true;
						break;
				}
				break;

				
			//MOAT
			case DEFENSE_MOAT:
				switch(driveState) {
					case NOT_DRIVING:
						//Input arm move down when applicable
						Robot.getInstance().getIntake().down();
						driveState = DRIVING;
						this.driveTime = Common.time() + 3000;
						break;
					case DRIVING:
						//if (arm.moveCompleted()) {
						if (Common.time() >= driveTime) {
							driveState = DEFENSE_CROSSED;
						}
						break;
					case DEFENSE_CROSSED:
						defenseCleared = true;
						break;
				}
				break;
				
			//ROCK WALL
			case DEFENSE_ROCK_WALL:
				switch(driveState) {
				case NOT_DRIVING:
					//Input arm move down when applicable
					dt.setDriveSpeed(-0.70);
					driveTime = Common.time() + 3000;
					driveState = DRIVING;
					break;
				
				case DRIVING:
					//if (arm.moveCompleted()) {
					if (Common.time() >= driveTime) {
						driveState = DEFENSE_CROSSED;
					}
					break;
				case DEFENSE_CROSSED:
					defenseCleared = true;
					break;
				}
				
				break;
				
			//RAMPARTS
			case DEFENSE_RAMPARTS:
				switch(driveState) {
				case NOT_DRIVING:
					//Input arm move down when applicable
					dt.setDriveSpeed(-0.70);
					driveTime = Common.time() + 3000;
					driveState = DRIVING;
					break;
				
				case DRIVING:
					//if (arm.moveCompleted()) {
					if (Common.time() >= driveTime) {
						driveState = DEFENSE_CROSSED;
					}
					break;
				case DEFENSE_CROSSED:
					defenseCleared = true;
					break;
				} 
				break;
		} return defenseCleared;
	}
					
	//Execute a full auto routine based on the user provided parameters.
	//Call this method from a robot loop, multiple times per second.
	public void autoRun() {
		// TESTING ONLY *****************
		/*paramStartingPlatform = 1;
		paramDefenseType = DEFENSE_LOWBAR;
		paramTargetPlatform = 2;
		paramSelectedAction = ACTION_UTURN;
		*/
		//***********************
		switch (autoRunState) {
			case AUTO_SETUP:
				Common.debug("autoRun: AUTO_SETUP");
				if (paramStartingPlatform > 0 && paramDefenseType > 0) {
					Common.debug("autoRun: AUTO_DRIVE - Starting Drive Defense" + paramDefenseType);
					autoRunState = AUTO_DRIVE;
				}
				break;
			case AUTO_DRIVE:
				if (driveDefense(paramDefenseType)) {
					Common.debug("autoRun: AUTO_DRIVE - Completed Drive Defense");
					autoRunState = AUTO_ALIGN;
				}	
				break;
			
			case AUTO_ALIGN:
				if (paramStartingPlatform == 1) {
					distance = 18*12-135;
				} else if (paramStartingPlatform == 2) {
					distance = 18*12-135;
				} else if (paramStartingPlatform == 3) {
					distance = 0;
				} else if (paramStartingPlatform == 4) {
					distance = 0;
				} else {
					distance = 151;
				}
				distance *= -1;
				Common.debug("autoRun: Starting goal line approach "  + distance);
				dt.driveDistance(distance);
				autoRunState = AUTO_ROTATE_TO_GOAL;
				break;
				
			case AUTO_ROTATE_TO_GOAL:
				Common.debug("-------------------------");
				Common.debug("Starting Auto rotate");
				Common.debug("Encoder Distance :" + dt.getEncoder().get() + ":" + dt.getEncoder().getDistance());
				Common.debug("PID target :" + dt.distancePID.getTarget());
				Common.debug("-------------------------");
				if (dt.driveComplete()) {
					if (paramStartingPlatform == 1) {
						heading = 20;
					} else if (paramStartingPlatform == 2) {
						heading = 30;
					} else if (paramStartingPlatform == 3) {
						heading = 15;
					} else if (paramStartingPlatform == 4) {
						heading = 168; //was 168 
					} else {
						heading = 300;
					}
					Common.debug("autoRun: Completed drive, starting turn to goal, and lowering arms "  + heading);
					dt.rotateTo(heading);
					autoRunState = AUTO_AIM_TOWARD_GOAL;
				} 
				break;
				
			case AUTO_AIM_TOWARD_GOAL:
				if (dt.driveComplete()) {
					Common.debug("autoRun: Completed turn to goal, starting auto aim");
					autoRunState = AUTO_SHOOT;
				}
				break;
				
			case AUTO_SHOOT:
					if (Robot.vision.autoAim()) {
						Common.debug("autoRun: AUTO_SHOOT Ball thrown");
						autoRunState = AUTO_COMPLETE;
					}
				break;
				
				
			case AUTO_COMPLETE:
				dt.setDriveSpeed(0.0);
				dt.setTurnSpeed(0.0);
				break;
				
		}
		dt.autoDrive();
	}

	public double absoluteTurnLogic(double target, double current) {
		if (target > current) {
			return 90;
		} else {
			return 270;
		}
	}
}
