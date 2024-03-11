package pcd.lab03.jpfmix;

import gov.nasa.jpf.vm.Verify;

public class TestVerifyAPIInput {

	public static void main(String[] args) {

		/* simulating the input of an int value from 1 to 5 */
		int simulatedInput = Verify.getInt(1, 5);		
		
		System.out.println("Value: " + simulatedInput);

	}

}
