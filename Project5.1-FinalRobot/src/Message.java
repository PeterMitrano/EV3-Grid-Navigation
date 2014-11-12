import java.awt.Point;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The class defines the content of messages being sent to the computer from the
 * robot since only this class has access to the DataOutputStream, only
 * instances of this class can send data to the computer. This ensures that all
 * data is sent in a predictable and consistent format.
 * 
 * @author peter
 * 
 */
public class Message {

	public static DataOutputStream out;
	private boolean done;
	private int x, y, l;
	private ArrayList<Point> obstacles;

	/**
	 * 
	 * @param done
	 *            Tells the computer if the robot has reached its position.
	 *            Sending this will cause the computer application to stop
	 *            listening for position updates, and be again ready to send a
	 *            new position command
	 * @param x
	 *            The current x position of the robot
	 * @param y
	 *            The current Y position of the robot
	 * @param obstacles
	 *            A list of Point objects listing any newly found obstacles.
	 *            These will be added to the pathfinder's grid so that they can
	 *            be planned around in future navigations
	 */
	Message(boolean done, int x, int y, ArrayList<Point> obstacles) {
		this.done = done;
		this.x = x;
		this.y = y;
		this.l = obstacles.size();
		this.obstacles = obstacles;
	}

	/**
	 * send the constructed message to the computer via bluetooth connection
	 * 
	 * @throws IOException
	 *             This will error out if the DataOuputStream given to the
	 *             message is no longer valid.
	 */
	public void send() throws IOException {
		out.writeBoolean(done);
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(l);
		for (int i = 0; i < l; i++) {
			out.writeInt(obstacles.get(i).x);
			out.writeInt(obstacles.get(i).y);
		}
	}
}
