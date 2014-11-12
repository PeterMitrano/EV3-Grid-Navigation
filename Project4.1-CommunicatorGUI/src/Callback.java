import java.awt.Point;
import java.util.ArrayList;

/**
 * Allows the {@link ComputerCommunicator} to return data within the scope of the {@link World} class
 * 
 * @author peter
 *
 */
public interface Callback {

	/**
	 * 
	 * @param x				X position of update
	 * @param y				Y position of update
	 * @param obstacles		list of obstacles to add to the display
	 */
	public void run(int x, int y, ArrayList<Point> obstacles);
}
