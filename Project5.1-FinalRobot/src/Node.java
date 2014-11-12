/**
 * A Node is a position on the map as used by the pathfinder class.
 * 
 * @author peter
 * 
 * 
 */
public class Node {

	/** x location of Node in grid */
	public int x;

	/** y location of Node in grid */
	public int y;

	/** distance between node and start location */
	public int distance;

	/**
	 * cost to move to this node. This is 1 for any unoccupied node, and
	 * {@link Pathfinder#INFINITY} for occupied node
	 */
	public int weight;

	/** boolean to mark node as visited */
	public boolean visited;

	/**
	 * creates a new node at that position. The node is given a distance of
	 * {@link Pathfinder#INFINITY} and weight of 1. it is intialized as not
	 * visited.
	 * 
	 * @param x		the x position of the new new
	 * @param y 	the y position of the new new
	 * */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.distance = Pathfinder.INFINITY;
		this.weight = 1;
		this.visited = false;
	}

	/** returns the location of the node in the form x,y
	 * 
	 * */
	public String toString() {
		return x + "," + y;
	}
}