/**
 * The pose represents the direcition the robot is facing, either North, South,
 * East, or West
 * 
 * @author peter
 * 
 */
public class Pose {

	public static final Pose N = new Pose(), S = new Pose(), E = new Pose(),
			W = new Pose();

	Pose() {
	}

	/**
	 * Get the pose left of this pose
	 * @return 			the pose counter-clockwise to the current pose. For example, North returns West 
	 */
	public Pose getLeft() {
		if (this == Pose.N) {
			return Pose.W;
		} else if (this == Pose.S) {
			return Pose.E;
		} else if (this == Pose.E) {
			return Pose.N;
		}
		return Pose.S;
	}
	
	/**
	 * Get the pose right of this pose
	 * @return 			the pose clockwise to the current pose. For example, North returns East
	 */
	public Pose getRight() {
		if (this == Pose.N) {
			return Pose.E;
		} else if (this == Pose.S) {
			return Pose.W;
		} else if (this == Pose.E) {
			return Pose.S;
		}
		return Pose.N;
	}

	/**
	 * Determines if a pose is to the right of this pose 
	 * 
	 * @param b			the pose you want to test
	 * @return returns 	true if the pose is to the right, otherwise it returns false 
	 */
	public boolean rightOf(Pose b) {
		if (this == Pose.N) {
			return b == Pose.W;
		} else if (this == Pose.S) {
			return b == Pose.E;
		} else if (this == Pose.E) {
			return b == Pose.N;
		}
		return b == Pose.S;
	}

	/**
	 * Determines if a pose is to the left of this pose 
	 * 
	 * @param b			the pose you want to test
	 * @return returns 	true if the pose is to the left, otherwise it returns false 
	 */
	public boolean leftOf(Pose b) {
		if (this == Pose.N) {
			return b == Pose.E;
		} else if (this == Pose.S) {
			return b == Pose.W;
		} else if (this == Pose.E) {
			return b == Pose.S;
		}
		return b == Pose.N;
	}

	/**
	 * Returns the String version of the pose. For example, Pose.N returns "N"  
	 * 
	 * @return returns	the String version of the pose  
	 */
	public String toString() {
		if (this == Pose.N) {
			return "N";
		} else if (this == Pose.S) {
			return "S";
		} else if (this == Pose.E) {
			return "E";
		}
		return "N";
	}

}
