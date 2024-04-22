package pcd.ass01.simengine_conc.gui;

import pcd.ass01.simengine_conc.*;

public class SimulationController {

	private Flag stopFlag;
	private AbstractSimulation simulation;
	private int nSteps;
	private int nWorkers;
	
	public SimulationController(AbstractSimulation simulation, int nSteps, int nWorkers) {
		this.simulation = simulation;
		this.stopFlag = new Flag();
		this.nSteps = nSteps;
	}
		
	public void notifyStarted() {
		new Thread(() -> {
			simulation.setup();
			
			RoadSimView view = new RoadSimView();
			simulation.addSimulationListener(view);		

			stopFlag.reset();
			simulation.run(nSteps, nWorkers, stopFlag);
			
		}).start();
	}
	
	public synchronized void notifyStopped() {
		stopFlag.set();
	}

}
