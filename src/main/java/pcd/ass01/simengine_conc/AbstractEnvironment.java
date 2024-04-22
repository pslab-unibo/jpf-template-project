package pcd.ass01.simengine_conc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 *   
 * Base class to define the environment of the simulation
 *   
 */
public abstract class AbstractEnvironment {

	private String id;

	private HashMap<String, AbstractAgent> registeredAgents;

	protected ConcurrentHashMap<String, Action> submittedActions;

	protected AbstractEnvironment(String id) {
		this.id = id;		
		this.submittedActions = new ConcurrentHashMap<>();
		this.registeredAgents = new HashMap<>();	
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * Called at the beginning of the simulation
	 */
	public abstract void init();
	
	/**
	 * 
	 * Called at each step of the simulation
	 * 
	 * @param dt
	 */
	public abstract void step(int dt);

	/**
	 * 
	 * Called by an agent to get its percepts 
	 * 
	 * @param agentId - identifier of the agent
	 * @return agent percept
	 */
	public abstract Percept getCurrentPercepts(String agentId);

	/**
	 * 
	 * Called by agent to submit an action to the environment
	 * 
	 * @param act - the action
	 */
	public void submitAction(String id, Action act) {
		submittedActions.put(id, act);
	}
	
	
	public void registerNewAgent(AbstractAgent agent) {
		registeredAgents.put(agent.getId(), agent);
	}
	
	public AbstractAgent getAgent(String agentId) {
		return registeredAgents.get(agentId);
	}
	
	public Stream<AbstractAgent> getRegisteredAgents(){
		return registeredAgents.entrySet().stream().map(el -> el.getValue())
;
	}
	/**
	 * 
	 * Called at each simulation step to clean the list of actions
	 * submitted by agents
	 * 
	 */
	public void cleanActions() {
		submittedActions.clear();
	}

	/**
	 * 
	 * Called at each simulation step to process the actions 
	 * submitted by agents. 
	 * 
	 */
	public void processActions() {
		Iterator<Entry<String,Action>> it = submittedActions.entrySet().iterator();		
		while (it.hasNext()) {			
			Action act = it.next().getValue();
			act.exec(this);
		}
	}

}
