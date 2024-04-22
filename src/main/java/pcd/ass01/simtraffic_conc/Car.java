package pcd.ass01.simtraffic_conc;

import java.util.Optional;

import pcd.ass01.simengine_conc.*;

public class Car extends AbstractAgent {
	
	protected CarPercept currentPercept;
	protected Optional<Action> selectedAction;
		
	public Car(String id) {
		super(id);
	}

	public void step(int dt) {
		AbstractEnvironment env = this.getEnv();		
		currentPercept = (CarPercept) env.getCurrentPercepts(getId());			
		env.submitAction(getId(),new MoveForward());
		
	}
	
	
	protected void log(String msg) {
		System.out.println("[CAR " + this.getId() + "] " + msg);
	}

	
}
