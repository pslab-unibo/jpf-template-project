package pcd.ass01.simengine_conc.gui;

import pcd.ass01.simengine_conc.*;
import pcd.ass01.simtraffic_conc_examples.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	private static int N_STEPS = 1;
	private static int N_WORKERS = 1;

	public static void main(String[] args) {		
		AbstractSimulation simulation = new FakeTrafficSimulation();		
		SimulationController controller = new SimulationController(simulation, N_STEPS, N_WORKERS);
        SimulationGUI gui = new SimulationGUI(controller);
        gui.display();
				
	}
}
