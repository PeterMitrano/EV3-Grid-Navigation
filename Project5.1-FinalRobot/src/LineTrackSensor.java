import lejos.hardware.motor.NXTMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

/**
 * handles initialization and reading of color sensor. Uses the color sensor in
 * RGB mode and sums up the RGB values coming from the sensor. This allows for
 * proportional line tracking.
 * 
 * @author peter
 * 
 */
public class LineTrackSensor {

	private static float BLACK_THREASHOLD = 0.3f, WHITE_THREASHOLD = 0.7f;

	private EV3ColorSensor sensor;
	private SampleProvider sampleProvider;
	private float[] samples;
	private float max = 0, min = Float.MAX_VALUE;

	/**
	 * intialize the color sensor in RGB mode, and create the sample provider
	 * 
	 * @param sensorPort
	 *            The port of the sensor, like SensorPort.S4
	 */
	public LineTrackSensor(Port sensorPort) {
		sensor = new EV3ColorSensor(sensorPort);
		sampleProvider = sensor.getRGBMode();
		samples = new float[sampleProvider.sampleSize()];
	}

	/**
	 * 
	 * @return the normalized sensor value. the return is capped between -1 and
	 *         1
	 */
	public float getValue() {
		float val = 2 * (readIntensity() - min) / (max - min) - 1;
		val = val < -1 ? -1 : val > 1 ? 1 : val;
		return val;
	}

	/**
	 * 
	 * @return returns true if the color sensor is less than 30% towards white
	 */
	public boolean onCross() {
		return getValue() < BLACK_THREASHOLD;
	}

	/**
	 * 
	 * @return returns true if the color sensor is over a 70% towards white
	 */
	public boolean offCross() {
		return getValue() > WHITE_THREASHOLD;
	}

	/**
	 * Calibrates the line sensor on the same side as the given motor object
	 * 
	 * @param motor
	 *            The motor which is one the side of the sensor you want to
	 *            calibrate
	 */
	public void calibrate(NXTMotor motor) {
		motor.setPower(LineTracker.moveSpeed);
		motor.forward();
		int t0 = motor.getTachoCount();
		while (Math.abs(motor.getTachoCount() - t0) < 120) {
			float intensity = readIntensity();
			compareToMin(intensity);
			compareToMax(intensity);
		}
		motor.backward();
		t0 = motor.getTachoCount();
		while (Math.abs(motor.getTachoCount() - t0) < 120) {
		}
		motor.stop();
	}

	private void compareToMin(float n) {
		min = n < min ? n : min;
	}

	private void compareToMax(float n) {
		max = n > max ? n : max;
	}

	private float readIntensity() {
		float intensity = 0;

		sampleProvider.fetchSample(samples, 0);

		for (int i = 0; i < sampleProvider.sampleSize(); i++) {
			intensity += samples[i];
		}

		return intensity;
	}
}
