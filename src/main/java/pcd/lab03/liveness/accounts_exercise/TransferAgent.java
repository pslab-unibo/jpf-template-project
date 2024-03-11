package pcd.lab03.liveness.accounts_exercise;

import java.util.Random;

public class TransferAgent extends Thread {
	
	private static final int MAX_AMOUNT = 10;
	private AccountManager man;
	private int numTransactions;

	TransferAgent(AccountManager man, int numTransactions){
		this.man = man;
		this.numTransactions = numTransactions;
	}
	
	public void run() {
		
		Random gen = new Random();
		int numAccounts = man.getNumAccounts();
		
		for (int i = 0; i < numTransactions; i++){
			
			/* select the source account */
			int fromAcc = gen.nextInt(numAccounts);
			
			/* select the dest account */
			int toAcc = 0;
			do {
				toAcc = gen.nextInt(numAccounts);
			} while (toAcc == fromAcc);

			/* select an amount of money to transfer */
			int amount = gen.nextInt(MAX_AMOUNT);				

			/* try to transfer */
			try {
				log("Transferring from "+fromAcc+" to "+toAcc+" amount "+amount+"...");
				man.transferMoney(fromAcc,toAcc,amount);
				log("done.");
			} catch (InsufficientBalanceException ex){
				log("Not enough money.");
			}
		}
	}
	
	private void log(String msg){
		synchronized(System.out){
			System.out.println("["+this+"] "+msg);
		}
	}
}