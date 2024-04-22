package pcd.ass01.simengine_conc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MasterAgent extends Thread {
	
	private int numSteps;	
	private AbstractSimulation sim;
	private Flag stopFlag;
	private Semaphore done;
	private int nWorkers;
	
	public MasterAgent(AbstractSimulation sim, int numSteps, int nWorkers, Flag stopFlag, Semaphore done) {
		this.sim = sim;
		this.stopFlag = stopFlag;
		this.numSteps = numSteps;
		this.done = done;
		this.nWorkers = nWorkers;
	}

	public void run() {
		
		log("booted");
		
		AbstractEnvironment simEnv = sim.getEnvironment();
		List<AbstractAgent> simAgents = sim.getAgents();
		
		simEnv.init();
		for (AbstractAgent a: simAgents) {
			a.init(simEnv);
		}

		int t = sim.getInitialTime();
		int dt = sim.getTimeStep();
		
		sim.notifyReset(t, simAgents, simEnv);
		
		
		int nAssignedAgentsPerWorker = simAgents.size()/nWorkers;

		Trigger canDoStep = new Trigger(nWorkers);
		CyclicBarrier jobDone = new CyclicBarrier(nWorkers + 1);
		
		log("creating workers...");
		
		int index = 0;
		List<WorkerAgent> workers = new ArrayList<>();
		for (int i = 0; i < nWorkers - 1; i++) {
			List<AbstractAgent> assignedSimAgents = new ArrayList<>();
			for (int j = 0; j < nAssignedAgentsPerWorker; j++) {
				assignedSimAgents.add(simAgents.get(index));
				index++;
			}
			
			WorkerAgent worker = new WorkerAgent("worker-"+i, assignedSimAgents, dt, canDoStep, jobDone, stopFlag);
			worker.start();
			workers.add(worker);
		}
		log("workers created.");
		
		List<AbstractAgent> assignedSimAgents = new ArrayList<>();
		while (index < simAgents.size()) {
			assignedSimAgents.add(simAgents.get(index));
			index++;
		}

		WorkerAgent worker = new WorkerAgent("worker-"+(nWorkers-1), assignedSimAgents, dt, canDoStep, jobDone, stopFlag);
		worker.start();
		workers.add(worker);
		
		int step = 0;

		try {
			while (!stopFlag.isSet() &&  step < numSteps) {
				
				simEnv.step(dt);
				simEnv.cleanActions();

				/* trigger workers to do their work in this step */	
				canDoStep.trig();
				
				/* wait for workers to complete */
				jobDone.await();

				/* executed actions */
				simEnv.processActions();
								
				sim.notifyNewStep(t, simAgents, simEnv);
				
				log("step " + step + " done");
					
				t += dt;
				step++;
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		log("done");
		stopFlag.set();
		canDoStep.trig();

		done.release();
	}
	
	private void log(String msg) {
		System.out.println("[MASTER] " + msg);
	}
	
	
}
