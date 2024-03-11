package pcd.lab03.liveness.accounts_exercise;

class Account {

	private int balance;
	
	public Account(int amount){
		balance = amount;
	}

	public int getBalance(){
		return balance;
	}

	public void debit(int amount){
		balance-=amount;
	}

	public void credit(int amount){
		balance+=amount;
	}
}
