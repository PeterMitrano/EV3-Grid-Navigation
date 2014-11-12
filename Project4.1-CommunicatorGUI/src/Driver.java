import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Contains the main. window won't pop-up until connection to the robot is
 * established.
 * 
 * @author peter
 * 
 */
public class Driver {

	public static void main(String[] args) throws IOException {
		ComputerCommunicator.connect();
		JFrame frame = new JFrame("Controller");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new World());
		frame.setPreferredSize(new Dimension(800, 500));
		frame.pack();
		frame.setVisible(true);
	}
}
