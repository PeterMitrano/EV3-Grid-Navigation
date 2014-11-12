EV3-Grid-Navigation
===================

EV3 project for grid navigation &amp; path planning

===================

EV3 project written with the lejos library. The robot has 2 color sensors and an ultrasonic sensor. 
The robot receives commands from the GUI program running on the laptop, and navigates to those points in the shortest path possible, given that the grid contains obstacles. These obstacles are discovered and remebered as the robot navigates. 

A* was used for path planning, with manhattan method for the heuristic component and a weight of 1 for open nodes and 1000 for node with obstacles.

java socket connection was used for communication between the robot and computer over the bluetooth PAN network
