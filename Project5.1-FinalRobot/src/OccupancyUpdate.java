import java.awt.Point;

/**
 * object containing data about the success of a move. This is what
 * {@link GridNavigator#navigateTo(int, int)} method returns
 * 
 * @author peter
 * 
 */
public class OccupancyUpdate {

	/** this represents a successful completion of a navigation task */
	public static final OccupancyUpdate SUCCESS = new OccupancyUpdate(true,
			new Point());

	/** holds weather or not the next intersection in front of the robot has an obstacle */
	public boolean pathClear;
	
	/** holds the x and y position at which the obstacle was found */
	public int x, y;

	/**
	 * 
	 * @param pathClear		holds if the intersection in front is clear
	 * @param p				holds the point at which the obstacle is found. This is an empty Point() for {@link OccupancyUpdate#SUCCESS}
	 */
	OccupancyUpdate(boolean pathClear, Point p) {
		this.pathClear = pathClear;
		this.x = p.x;
		this.y = p.y;
	}
}
