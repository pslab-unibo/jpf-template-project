package pcd.lab03.liveness.deadlock_simplest;

import java.util.Random;

public abstract class BaseAgent extends Thread {
 
	private Random gen;
	
	public BaseAgent(){
		gen = new Random();
	}
	
	protected void waitAbit() {
		try {
			Thread.sleep(gen.nextInt(200));
		} catch (Exception ex){}
	}

}
