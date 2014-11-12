import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class handles all the GUI of the program.
 * 
 * @author peter
 * 
 */
public class World extends JPanel implements MouseListener, Callback,
		ActionListener {

	private static final int W = 9, H = 5;

	private static ArrayList<Point> obstacles;
	private static int robotX = 0, robotY = 0;
	private int s;
	private JButton reset;

	/**
	 * creates the GUI
	 */
	World() {
		super();
		reset = new JButton("Clear Obstacles");
		reset.addActionListener(this);
		this.add(reset);
		addMouseListener(this);
		s = Math.min(getWidth() / W, getHeight() / H);
		obstacles = new ArrayList<Point>();
	}

	private void setRobotPosition(int x, int y) {
		if (x >= 0 && x < W && y >= 0 && y < H) {
			robotX = x;
			robotY = y;
		} else {
			System.out.println("can't set robot location to: " + x + "," + y);
		}
	}

	private void addObstacles(ArrayList<Point> obstacles) {
		for (Point p : obstacles) {
			if (!World.obstacles.contains(p)) {
				World.obstacles.add(p);
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		s = Math.min(getWidth() / W, getHeight() / H);
		g.drawRect(0, 0, W * s, H * s);
		for (int i = 1; i <= W; i++) {
			g.drawLine(i * s, 0, i * s, s * H);
		}
		for (int j = 1; j <= H; j++) {
			g.drawLine(0, j * s, W * s, j * s);
		}

		drawRobot(g);
		drawObstacles(g);
	}

	private void drawRobot(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(robotX * s, (H - robotY - 1) * s, s, s);
	}

	private void drawObstacles(Graphics g) {
		for (Point p : obstacles) {
			g.setColor(Color.red);
			g.fillOval(p.x * s, (H - p.y - 1) * s, s, s);
		}
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		int x = me.getX() / s;
		int y = H - me.getY() / s - 1;
		new Thread(new ComputerCommunicator(x, y, this)).start();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	private class GraphicsUpdater implements Runnable {

		int x, y;
		ArrayList<Point> obstacles;

		GraphicsUpdater(int x, int y, ArrayList<Point> obstacles) {
			this.x = x;
			this.y = y;
			this.obstacles = obstacles;
		}

		public void run() {
			setRobotPosition(x, y);
			addObstacles(obstacles);
			repaint();
		}
	}

	@Override
	public void run(int x, int y, ArrayList<Point> obstacles) {
		SwingUtilities.invokeLater(new GraphicsUpdater(x, y, obstacles));
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == reset) {
			obstacles.clear();
			repaint();
		}
	}

}