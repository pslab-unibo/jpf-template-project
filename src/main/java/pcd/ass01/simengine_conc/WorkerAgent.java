package pcd.ass01.simengine_conc;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class WorkerAgent extends Thread {
	
	private List<AbstractAgent> assignedSimAgents;
	private Trigger canDoStep;
	private int dt;
	private Flag stopFlag;
	// private CyclicLatch jobDone;
	private CyclicBarrier jobDone;
	
	public WorkerAgent(String id, List<AbstractAgent> assignedSimAgents, int dt, Trigger canDoStep, CyclicBarrier jobDone, Flag flag) {
	// public WorkerAgent(String id, List<AbstractAgent> assignedSimAgents, int dt, Trigger canDoStep, CyclicLatch jobDone, Flag flag) {
		super(id);
		this.assignedSimAgents = assignedSimAgents;
		this.dt = dt;
		this.canDoStep = canDoStep;
		this.stopFlag = flag;
		this.jobDone = jobDone;
	}
	
	public void run() {
		log("running.");
		while (!stopFlag.isSet()) {
			try {
				canDoStep.await();
				if (!stopFlag.isSet()) {
					for (AbstractAgent ag: assignedSimAgents) {
						ag.step(dt);
					}
					// jobDone.countDown();
					jobDone.await();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		log("done");
	}
	
	private void log(String msg) {
		System.out.println("[" + getName() +"] " + msg);
	}
	
	
}
