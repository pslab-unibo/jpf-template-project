package pcd.ass01.simtraffic_conc_examples;

import pcd.ass01.simengine_conc.*;
import pcd.ass01.simtraffic_conc.*;

public class FakeTrafficSimulation extends AbstractSimulation {

	public FakeTrafficSimulation() {
		super();
	}
	
	public void setup() {

		this.setTimings(0, 1);
		
		RoadsEnv env = new RoadsEnv();
		this.setEnvironment(env);
				
		for (int i = 0; i < 4; i++) {
			this.addAgent(new Car("car-"+i));
		}
	}	
	
}
