package pcd.ass01.simtraffic_conc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import pcd.ass01.simengine_conc.*;

public class RoadsEnv extends AbstractEnvironment {
	
	public RoadsEnv() {
		super("traffic-env");
	}
	
	public void init() {
	}
	
	public void step(int dt) {
	}


	@Override
	public Percept getCurrentPercepts(String agentId) {
		return new CarPercept();
	}

}
