import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * Controls the ultrasonic sensor.
 * 
 * @author peter
 * 
 */
public class ObjectSensor {

	private EV3UltrasonicSensor sensor;
	private SampleProvider sampleProvider;
	private float[] sample;
	private static final float PATH_CLEAR_THREASHOLD = 0.15f;

	/**
	 * 
	 * @param sensorPort
	 *            initialize the sensor in DistanceMode and the sample provider
	 *            mode
	 */
	public ObjectSensor(Port sensorPort) {
		sensor = new EV3UltrasonicSensor(sensorPort);
		sampleProvider = sensor.getDistanceMode();
		sample = new float[sampleProvider.sampleSize()];
	}

	/**
	 * 
	 * @return returns true if the path is clear
	 */
	public boolean pathClear() {
		sampleProvider.fetchSample(sample, 0);
		boolean pathClear = sample[0] > PATH_CLEAR_THREASHOLD;
		if (!pathClear) {
			LineTracker.audio.playTone(600, 50);
		}
		return pathClear;
	}
}
