import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the move abstract/overall class. It contains both a Pathfinder
 * object, a GridNavigator object, and the Socket connection to the computer
 * 
 * @author peter
 * 
 */
public class MainRobot {

	private GridNavigator bob;
	private Socket socket;
	private DataInputStream in;
	private Pathfinder pfinder;

	/**
	 * creates the pathfinder, and a socket. The socket connects to 10.0.1.12 on
	 * port 1234. It also gets the DataInputStream and DataOutputStream, and
	 * passes the DataOuput stream to the Message class. This prevents any use
	 * of the DataOutput stream that isn't of the format specified by the
	 * Message class
	 * 
	 */
	MainRobot() {
		pfinder = new Pathfinder(9, 5);

		try {
			socket = new Socket("10.0.1.12", 1234);
			Message.out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

		} catch (IOException e) {
			LineTracker.lcd.drawString("ERROR " + e.getMessage(), 0, 5);
			LineTracker.keys.waitForAnyPress();
		}

		bob = new GridNavigator();
		bob.calibrate();
	}

	/**
	 * The main loop of the program. It is an infinite loop that reads a x and y
	 * position from the DataInputStream, then navigates to that point, then
	 * sends an update to the computer, and repeats
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		while (true) {
			int x = in.readInt();
			int y = in.readInt();
			navigateTo(x, y);
			// tell GUI it's reach destination
			new Message(true, bob.getX(), bob.getY(), new ArrayList<Point>())
					.send();
		}
	}

	/**
	 * 
	 * @param x
	 *            The x position the robot should travel to
	 * @param y
	 *            The y position the robot should travel to
	 * @throws IOException
	 *             If the DataOutputStream is screwed up it will throw
	 *             IOException
	 */
	private void navigateTo(int x, int y) throws IOException {
		for (Node n : pfinder.getPath(bob.getX(), bob.getY(), x, y)) {
			OccupancyUpdate update = bob.navigateTo(n.x, n.y);
			if (!update.pathClear) {
				pfinder.addObject(update.x, update.y);
				navigateTo(x, y);
				return;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new MainRobot().run();
	}
}
