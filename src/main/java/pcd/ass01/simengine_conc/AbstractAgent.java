package pcd.ass01.simengine_conc;

public abstract class AbstractAgent {
	
	private String myId;
	private AbstractEnvironment env;
	
	protected AbstractAgent(String id) {
		this.myId = id;
	}
	
	public void init(AbstractEnvironment env) {
		this.env = env;
	}
	
	abstract public void step(int dt);
	
	public String getId() {
		return myId;
	}
	
	protected AbstractEnvironment getEnv() {
		return this.env;
	}
}
