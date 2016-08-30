package org.usfirst.frc.team4564.robot;

public class Constants {
	
	//PWM inputs
	public static final int PWM_DRIVE_L = 0;
	public static final int PWM_DRIVE_R = 1;
	
	//Digital IO
	
	//Turn PID constants
	public static final double TURN_P = 0.1;
	public static final double TURN_I = 0;
	public static final double TURN_D = 0;
	
	public static final double REFRESH_RATE = 50;

	public static final int DIO_DRIVE_FR_ENCODER_A = 1;
	public static final int DIO_DRIVE_FR_ENCODER_B = 2;
	
	//Drive PID constants
	public static final double DRIVE_P = 0.05;
	public static final double DRIVE_I = 0.0;
	public static final double DRIVE_D = -0.05;
	public static final double COUNTS_PER_INCH = 88.95; //83.39;
	
	//Gyro constants
		public static double GYRO_P = 0.08; 
		public static double GYRO_I = 0.0001;
		public static double GYRO_D = 0.003;
		public static final double GYRO_SENSITIVITY = 0.00669;

}
