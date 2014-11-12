import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.port.SensorPort;

public class GridNavigator {

	private LineTracker lineTracker;
	private int x, y;
	private Pose pose;
	private ObjectSensor objSensor;
	private ArrayList<Point> obstacles;

	GridNavigator() {
		objSensor = new ObjectSensor(SensorPort.S4);
		lineTracker = new LineTracker(objSensor);
		obstacles = new ArrayList<Point>();
		x = 0;
		y = 0;
		pose = Pose.N;
	}

	public void calibrate() {
		lineTracker.calibrate();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private void addObstacleToMessage(Point p) {
		obstacles.add(p);
	}

	/**
	 * tells the robot to navigate to a location
	 * 
	 * @param x
	 *            The x location on the map to travel to (1-9)
	 * @param y
	 *            The y location on the map to travel to (1-5)
	 * @return Either the OccupancyUpdate.SUCCESS object or one containing
	 *         information on the obstacle that was found.
	 * @throws IOException
	 * 
	 */
	public OccupancyUpdate navigateTo(int x, int y) throws IOException {
		LineTracker.lcd.clear();
		LineTracker.lcd.drawString("Navigating to " + x + "," + y, 0, 0);
		if (!attemptNavigateToX(x) || !attemptNavigateToY(y)) {
			Point blockedPoint = getPointInFrontOfMe();
			addObstacleToMessage(blockedPoint);
			sendData();
			return new OccupancyUpdate(false, blockedPoint);
		}
		return OccupancyUpdate.SUCCESS;
	}

	/**
	 * send x and y position to computer
	 * 
	 * @throws IOException
	 */
	private void sendData() throws IOException {
		new Message(false, x, y, obstacles).send();
	}

	private void face(Pose p) {
		while (pose != p) {
			if (p.leftOf(pose)) {
				lineTracker.turnLeft();
				pose = pose.getLeft();
			} else {
				lineTracker.turnRight();
				pose = pose.getRight();
			}
		}
	}

	private boolean attemptNavigateToX(int x) throws IOException {
		int dx = 0;
		if (x > this.x) {
			face(Pose.E);
			dx = 1;
		} else if (x < this.x) {
			face(Pose.W);
			dx = -1;
		}

		while (Math.abs(this.x - x) > 0) {
			if (!attemptPath()) {
				return false;
			} else {
				this.x += dx;
				sendData();
			}
		}
		return true;
	}

	private boolean attemptNavigateToY(int y) throws IOException {
		int dy = 0;
		if (y > this.y) {
			face(Pose.N);
			dy = 1;
		} else if (y < this.y) {
			face(Pose.S);
			dy = -1;
		}

		while (Math.abs(this.y - y) > 0) {
			if (!attemptPath()) {
				return false;
			} else {
				this.y += dy;
				sendData();
			}
		}

		return true;
	}

	private boolean attemptPath() {
		boolean completedPath = lineTracker.investigatePath();
		if (completedPath) {
			lineTracker.driveOverCross();
		} else {
			lineTracker.reverseInvestigatePath();
		}
		return completedPath;
	}

	private Point getPointInFrontOfMe() {
		Point p;
		if (pose == Pose.N) {
			p = new Point(x, y + 1);
		} else if (pose == Pose.S) {
			p = new Point(x, y - 1);
		} else if (pose == Pose.E) {
			p = new Point(x + 1, y);
		} else {
			p = new Point(x - 1, y);
		}
		return p;
	}
}
