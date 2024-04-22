package pcd.ass01.simengine_conc.gui;

public class SimulationGUI extends Thread {
	
	private SimulationController controller;
	
	public SimulationGUI(SimulationController contr){
		controller = contr;
	}
	
	public void run(){
		controller.notifyStarted();
		controller.notifyStopped();
	}
	
	public  void display() {
		start();
    }

		
}
