package pcd.lab03.liveness.accounts_exercise;

public class AccountManager {
	
	private final Account[] accounts;

	public AccountManager(int nAccounts, int amount){
		accounts = new Account[nAccounts];
		for (int i = 0; i < accounts.length; i++){
			accounts[i] = new Account(amount);
		}
	}
	
	public void transferMoney(int from,	int to, int amount) throws InsufficientBalanceException {
		synchronized (accounts[from]) {
			synchronized (accounts[to]) {
				if (accounts[from].getBalance() < amount)
					throw new InsufficientBalanceException();
				accounts[from].debit(amount);
				accounts[to].credit(amount);
			}
		}
	}
	
	public int getNumAccounts() {
		return accounts.length;
	}
}

