import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * Handles all sending and parsing of message with the robot. This class
 * inmplements runnable so it can be started in a new thread.
 * 
 * @author peter
 * 
 */
public class ComputerCommunicator implements Runnable {

	private static ServerSocket socket;
	private static Socket conn;
	private static DataInputStream in;
	private static DataOutputStream out;

	private int x, y;
	private Callback c;

	/**
	 * 
	 * @param x
	 *            the x position of the robot
	 * @param y
	 *            the y position of the robot
	 * @param c
	 *            a class implementing the callback interface
	 */
	public ComputerCommunicator(int x, int y, Callback c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}

	/**
	 * this si the run method required by the runnable interface. it sends a
	 * command to the robot, which interprets it as a new destination.
	 */
	public void run() {
		try {
			sendCommand(x, y);
		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
		}
	}

	private void sendCommand(int x, int y) throws IOException {
		out.writeInt(x);
		out.writeInt(y);
		catchResponses();
	}

	private void catchResponses() throws IOException {
		boolean done = false;
		System.out.println("catching responses...");
		while (!done) {

			done = in.readBoolean();
			int x = in.readInt();
			int y = in.readInt();
			int l = in.readInt();
			ArrayList<Point> obstacles = new ArrayList<Point>();
			for (int i = 0; i < l; i++) {
				obstacles.add(new Point(in.readInt(), in.readInt()));
			}

			c.run(x, y, obstacles);
		}

		System.out.println("destinatino reached");
	}

	/**
	 * initializes the connection between the robot and the computer
	 * @throws IOException
	 */
	public static void connect() throws IOException {
		socket = new ServerSocket(1234);
		System.out.println("waiting...");
		conn = socket.accept();
		System.out.println("accepted");

		in = new DataInputStream(conn.getInputStream());
		out = new DataOutputStream(conn.getOutputStream());
	}
}
