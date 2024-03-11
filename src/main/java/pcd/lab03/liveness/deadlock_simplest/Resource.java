package pcd.lab03.liveness.deadlock_simplest;

public class Resource {
	private final Object left = new Object();
	private final Object right = new Object();
	
	public void leftRight(){
		synchronized(left){
			synchronized(right){
				doSomething();
			}
		}
	}
	
	public void rightLeft(){
		synchronized(right){
			synchronized(left){
				doSomethingElse();
			}
		}
		/*
		synchronized(left){
			synchronized(right){
				doSomethingElse();
			}
		}*/
	}

	private void doSomething(){
		System.out.println("something.");
	}

	private void doSomethingElse(){
		System.out.println("something else.");
	}
}
