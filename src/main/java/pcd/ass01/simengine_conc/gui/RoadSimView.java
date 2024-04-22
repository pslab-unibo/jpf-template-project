package pcd.ass01.simengine_conc.gui;

import java.util.List;
import pcd.ass01.simengine_conc.*;

public class RoadSimView implements SimulationListener {

	
	public RoadSimView() {
	}
	
	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		System.out.println("step done");
	}
	
}
