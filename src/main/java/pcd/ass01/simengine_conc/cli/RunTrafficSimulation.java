package pcd.ass01.simengine_conc.cli;

import pcd.ass01.simengine_conc.*;
import pcd.ass01.simtraffic_conc_examples.*;

/**
 * 
 * Main class to create and run a simulation
 * 
 */
public class RunTrafficSimulation {

	private static int N_STEPS = 2;
	private static int N_WORKERS = 2;
	
	public static void main(String[] args) {		

		AbstractSimulation simulation = new FakeTrafficSimulation();
		simulation.setup();
		
		RoadSimView view = new RoadSimView();	
		simulation.addSimulationListener(view);		
		
		Flag stopFlag = new Flag();
		simulation.run(N_STEPS, N_WORKERS, stopFlag);
	}
}
