package pcd.lab03.liveness.deadlock_obs;

public interface Observer {
	void notifyStateChanged(Observed obs);
}
