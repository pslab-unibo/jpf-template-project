package pcd.ass01.simengine_conc;

import java.util.List;

public abstract class SimulatorView {

	abstract public void updateView(int t, AbstractEnvironment env, List<AbstractAgent> agents);
}
