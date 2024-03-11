package pcd.lab03.jpfmix;

import java.util.Random;

import gov.nasa.jpf.vm.Verify;

public class TestVerifyAPIRandom {

	public static void main(String[] args) {

		/* behaviour of random */

		Random rand = new Random(0);

		int simulatedInput = rand.nextInt();		
		
		System.out.println("Value: " + simulatedInput);

	}

}
