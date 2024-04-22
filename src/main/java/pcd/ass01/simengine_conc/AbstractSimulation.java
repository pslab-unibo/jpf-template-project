package pcd.ass01.simengine_conc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public abstract class AbstractSimulation {

	private AbstractEnvironment env;
	private List<AbstractAgent> agents;
	private List<SimulationListener> listeners;

	private int dt;
	private int t0;

	private int nSteps;
	
	protected AbstractSimulation() {
		agents = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	
	public abstract void setup();
	
	public void run(int nSteps, int nWorkers,  Flag stopFlag) {
		this.nSteps = nSteps;

		Semaphore done = new Semaphore(0);
		MasterAgent agent = new MasterAgent(this, nSteps, nWorkers, stopFlag, done);
		agent.start();
		
		try {
			done.acquire();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected void setTimings(int t0, int dt) {
		this.dt = dt;
		this.t0 = t0;
	}
			
	public void setEnvironment(AbstractEnvironment env) {
		this.env = env;
	}
		
	public AbstractEnvironment getEnvironment() {
		return env;
	}

	public void addAgent(AbstractAgent agent) {
		agents.add(agent);
		env.registerNewAgent(agent);
	}
	
	public  List<AbstractAgent> getAgents(){
		return agents;
	}
	
	public int getInitialTime() {
		return t0;
	}
	
	public int getTimeStep() {
		return dt;
	}
	
	public int getNumSteps() {
		return nSteps;
	}
		
	
	public void addSimulationListener(SimulationListener l) {
		this.listeners.add(l);
	}
	
	public List<SimulationListener> getListeners(){
		return this.listeners;
	}
	
	public void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (SimulationListener l: listeners) {
			l.notifyInit(t0, agents, env);
		}
	}

	public void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (SimulationListener l: listeners) {
			l.notifyStepDone(t, agents, env);
		}
	}
	
	
}
