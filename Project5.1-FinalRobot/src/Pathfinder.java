import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementation of A* path finding algorithm in a grid
 * 
 * @author peter
 * 
 */
public class Pathfinder {

	private Node[][] grid;
	private ArrayList<Node> path;

	/**
	 * INFINITY is actually 1000, and represents an obstacle in the grid
	 */
	public static final int INFINITY = 1000;

	/**
	 * Create the path finder object at a certain width and height
	 * 
	 * @param worldX
	 *            the X width of the world
	 * @param worldY
	 *            the Y width of the world
	 */
	public Pathfinder(int worldX, int worldY) {
		path = new ArrayList<Node>();
		populateGrid(worldY, worldX);
	}

	public void addObject(int x, int y) {
		LineTracker.lcd.drawString("object at " + x + "," + y, 0, 2);
		grid[y][x].weight = INFINITY;
	}

	private void populateGrid(int rows, int cols) {
		// rowsi are y
		// cols,j are x
		grid = new Node[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new Node(j, i);
			}
		}
	}

	public ArrayList<Node> getPath(int startX, int startY, int goalX, int goalY) {
		resetGrid();
		path.clear();

		Node u;
		grid[startY][startX].distance = 0;

		while (!grid[goalY][goalX].visited) {
			u = closestUnvisited();
			u.visited = true;

			updateNeightbor(0, -1, u);
			updateNeightbor(0, 1, u);
			updateNeightbor(-1, 0, u);
			updateNeightbor(1, 0, u);
		}

		generatePathFrom(grid[goalY][goalX]);
		Collections.reverse(path);
		return path;
	}

	private void resetGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j].distance = Pathfinder.INFINITY;
				grid[i][j].visited = false;
			}
		}
	}

	private void generatePathFrom(Node n) {
		path.add(n);

		if (isPrevious(n, n.y - 1, n.x)) {
			generatePathFrom(grid[n.y - 1][n.x]);

		} else if (isPrevious(n, n.y + 1, n.x)) {
			generatePathFrom(grid[n.y + 1][n.x]);

		} else if (isPrevious(n, n.y, n.x - 1)) {
			generatePathFrom(grid[n.y][n.x - 1]);

		} else if (isPrevious(n, n.y, n.x + 1)) {
			generatePathFrom(grid[n.y][n.x + 1]);
		}
	}

	private boolean isPrevious(Node n, int i, int j) {
		return inBounds(i, j) && n.distance - 1 == grid[i][j].distance;
	}

	private Node closestUnvisited() {
		int min = INFINITY;
		Node minNode = new Node(-1, -1);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (!grid[i][j].visited) {
					int d = grid[i][j].distance;
					if (d < min) {
						min = d;
						minNode = grid[i][j];
					}
				}
			}
		}
		return minNode;
	}

	private void updateNeightbor(int dx, int dy, Node u) {
		int newx = u.x + dx;
		int newy = u.y + dy;

		if (newx >= 0 && newy >= 0) {
			if (inBounds(newy, newx)) {
				Node n = grid[newy][newx];
				if (!n.visited) {
					n.distance = Math.min(n.distance, u.distance + n.weight);
				}
			}
		}
	}

	private boolean inBounds(int i, int j) {
		return i >= 0 && i < grid.length - 1 && j >= 0
				&& j < grid[0].length - 1;
	}

	private void printGrid() {
		// print grid
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				int d = grid[i][j].distance;
				if (d >= Pathfinder.INFINITY) {
					LineTracker.lcd.drawChar('X', j, i);
				} else {
					LineTracker.lcd.drawInt(d, j, i);
				}
			}
		}
		LineTracker.keys.waitForAnyPress();
	}
}
