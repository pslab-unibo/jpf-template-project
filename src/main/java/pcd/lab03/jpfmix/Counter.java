package pcd.lab03.jpfmix;

public class Counter {
	private int count;

	public Counter() {
		count = 0;
	}

	public void inc() {
		count++;
	}

	public int getValue() {
		return count;
	}
}