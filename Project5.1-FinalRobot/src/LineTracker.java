import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

/**
 * this class runs the line tracking is a child to the grid navigation. It
 * follows lines and alerts the higher up controls of obstacles.
 * 
 * @author peter
 * 
 */
public class LineTracker {

	/**
	 * general speed of motions. This controls line tracking speed, turn speed,
	 * and scan speed. it is currently 40% of max power.
	 */
	public static final int moveSpeed = 40;

	private static final EV3 ev3 = (EV3) BrickFinder.getLocal();
	public static final TextLCD lcd = ev3.getTextLCD();
	public static final Audio audio = ev3.getAudio();
	public static final Keys keys = ev3.getKeys();

	private LineTrackSensor leftSensor, rightSensor;
	private MyPilot pilot;
	private ObjectSensor objSensor;

	/**
	 * Initializes the two motors, which are then passed to the MyPilot object
	 * 
	 * @param objSensor
	 *            An object sensor, which is used in determining when to stop
	 *            turning
	 */
	LineTracker(ObjectSensor objSensor) {
		this.objSensor = objSensor;
		NXTMotor leftDrive = new NXTMotor(MotorPort.B);
		NXTMotor rightDrive = new NXTMotor(MotorPort.C);
		leftSensor = new LineTrackSensor(SensorPort.S2);
		rightSensor = new LineTrackSensor(SensorPort.S3);
		pilot = new MyPilot(leftDrive, rightDrive);
	}

	/**
	 * instructs the robot to follow the line it's on until it reaches a
	 * 'cross', or finds an obstacle in it's way.
	 */
	public boolean investigatePath() {
		while (!onCross()) {
			if (!trackClearPath(false)) {
				pilot.stop();
				return false;
			}
		}
		pilot.stop();
		return true;
	}

	/**
	 * instructs the robot to follow the line in reverse it's on until it reaches a
	 * 'cross'
	 */
	public boolean reverseInvestigatePath() {
		while (!onCross()) {
			if (!trackClearPath(true)) {
				pilot.stop();
				return false;
			}
		}
		pilot.stop();
		return true;
	}

	/**
	 * instructs the robot to drive until neither sensor is on the line, which
	 * results in the robot driving forward off the cross
	 */
	public void driveOverCross() {
		while (!offCross()) {
			pilot.drive(moveSpeed, moveSpeed);
		}
		pilot.stop();
	}

	/**
	 * instructs thr robot to calibrate the left and right sensors by calling
	 * the calibrate method of the MyPilot class given the left and right sensor
	 * objects
	 */
	public void calibrate() {
		pilot.calibrate(leftSensor, rightSensor);
	}

	/**
	 * turn right
	 */
	public void turnRight() {
		while (!rightSensor.onCross()) {
			pilot.drive(moveSpeed, -moveSpeed);
		}

		while (!rightSensor.offCross()) {
			pilot.drive(moveSpeed, -moveSpeed);
		}
	}

	/**
	 * turn left
	 */
	public void turnLeft() {
		while (!leftSensor.onCross()) {
			pilot.drive(-moveSpeed, moveSpeed);
		}

		while (!leftSensor.offCross()) {
			pilot.drive(-moveSpeed, moveSpeed);
		}
	}

	private boolean onCross() {
		return leftSensor.onCross() && rightSensor.onCross();
	}

	private boolean offCross() {
		return leftSensor.offCross() || rightSensor.offCross();
	}

	private boolean trackClearPath(boolean reverse) {
		int leftSpeed = (int) (leftSensor.getValue() * moveSpeed);
		int rightSpeed = (int) (rightSensor.getValue() * moveSpeed);
		if (reverse) {
			pilot.drive(-leftSpeed, -rightSpeed);
			return true;
		} else {
			pilot.drive(leftSpeed, rightSpeed);
			return objSensor.pathClear();
		}

	}
}