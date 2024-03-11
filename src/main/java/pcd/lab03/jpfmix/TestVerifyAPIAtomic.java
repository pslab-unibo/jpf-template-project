package pcd.lab03.jpfmix;

import gov.nasa.jpf.vm.Verify;

public class TestVerifyAPIAtomic {
	
	static class MyThread extends Thread {
		
		private Counter c;
		
		public MyThread(Counter c) {
			this.c = c;
		}
		
		public void run() {
			/* cutting the space state by enforcing a block to be atomic */
			
			Verify.beginAtomic();
			c.inc();
			Verify.endAtomic();
		}
	}
	
	static public void main(String[] args) {
		Counter c = new Counter();
		new MyThread(c).start();
		new MyThread(c).start();		
	}
}
