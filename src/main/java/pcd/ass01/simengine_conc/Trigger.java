package pcd.ass01.simengine_conc;

public class Trigger {

	private boolean triggered;
	private int nToBeTriggered;
	private int nTriggered;
	
	public Trigger(int n) {
		triggered = false;		
		this.nToBeTriggered = n;
		nTriggered = 0;
	}
	
	public synchronized void await() throws InterruptedException {
		while (!triggered) {
			wait();
		}
		nTriggered++;
		if (nTriggered == nToBeTriggered) {
			nTriggered = 0;
			triggered = false;
		}
	}
	
	public synchronized void trig() {
		triggered = true;
		notifyAll();
	}

}
