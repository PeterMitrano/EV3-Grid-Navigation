import lejos.hardware.motor.NXTMotor;

/**
 * this class controls the motion of the robot drive train. this is the only
 * class whose methods have access to the motors, so all motion in the wheels is
 * guaranteed to come from here. Even with EV3 motors, the NXTMotor class is
 * used because the EV3 regulated motors have unpredictable behavior. The
 * limitation is that tachometer count cannot be reset.
 * 
 * @author peter
 * 
 */
public class MyPilot {

	private NXTMotor leftDrive, rightDrive;
	private static final int checkAngle = 40;

	/**
	 * instantiate the pilot given the two motors
	 * 
	 * @param leftDrive
	 *            the left NXTMotor object
	 * @param rightDrive
	 *            the right NXTMotor object
	 */
	MyPilot(NXTMotor leftDrive, NXTMotor rightDrive) {
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
	}

	/**
	 * calibrates each line sensor. calls the calibrate method of each sensor
	 * given the corresponding motor.
	 * 
	 * @param leftSensor
	 *            the left LineTrackSensor object
	 * @param rightSensor
	 *            the right LineTrackSensor object
	 */
	public void calibrate(LineTrackSensor leftSensor,
			LineTrackSensor rightSensor) {
		leftSensor.calibrate(leftDrive);
		rightSensor.calibrate(rightDrive);
	}

	//
	// public void settle() {
	// long t0 = System.currentTimeMillis();
	// while (System.currentTimeMillis() - t0 < 300) {
	// stop();
	// }
	// }
	//
	// public void driveExtra() {
	// int t0 = leftDrive.getTachoCount();
	// while (leftDrive.getTachoCount() - t0 < 25) {
	// drive(LineTracker.moveSpeed, LineTracker.moveSpeed);
	// }
	// }

	/**
	 * determines if the next intersection has an obstacle.
	 * 
	 * @param objSensor
	 *            the ObjectSensor object
	 * @return returns true if there is nothing in the way.
	 */

	public boolean pathClearAfterTurn(ObjectSensor objSensor) {
		boolean pathClear = true;
		int t0 = leftDrive.getTachoCount();
		while (Math.abs(leftDrive.getTachoCount() - t0) < checkAngle) {
			drive(-LineTracker.moveSpeed / 2, LineTracker.moveSpeed / 2);
			pathClear = pathClear && objSensor.pathClear();
		}
		t0 = leftDrive.getTachoCount();
		while (Math.abs(leftDrive.getTachoCount() - t0) < 2 * checkAngle) {
			drive(LineTracker.moveSpeed / 2, -LineTracker.moveSpeed / 2);
			pathClear = pathClear && objSensor.pathClear();
		}
		t0 = leftDrive.getTachoCount();
		while (Math.abs(leftDrive.getTachoCount() - t0) < checkAngle) {
			drive(-LineTracker.moveSpeed / 2, LineTracker.moveSpeed / 2);
			pathClear = pathClear && objSensor.pathClear();
		}
		return pathClear;
	}

	/**
	 * stop the robot
	 */
	public void stop() {
		leftDrive.stop();
		rightDrive.stop();
	}

	/**
	 * drive the robot at a given speed
	 * 
	 * @param leftSpeed
	 *            drive left motor at this speed (0-100)
	 * @param rightSpeed
	 *            drive right motor at this speed (0-100)
	 */
	public void drive(int leftSpeed, int rightSpeed) {
		setSpeed(leftSpeed, rightSpeed);
		leftDrive.forward();
		rightDrive.forward();
	}

	private void setSpeed(int ls, int rs) {
		leftDrive.setPower(ls);
		rightDrive.setPower(rs);
	}
}
