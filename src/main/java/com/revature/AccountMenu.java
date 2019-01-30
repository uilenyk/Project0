package com.revature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

public class AccountMenu implements Showable {
	static Logger log = Logger.getRootLogger();

	public String show() {
		String result = "account";
		do {
			List<Account> accounts = listAccounts();
			int choice = -1;
			while (choice == -1) {
				if (accounts.isEmpty()) {
					System.out.println("You currently have no open accounts.");
				} else {
					System.out.println("Accounts #\t|\tBalance");
					for (Account a : accounts) {
						System.out.println(a.getId() + "\t\t|\t" + a.getBalance());
					}
					System.out.println(
							"Enter the account number of the account you want to access or an option from below:");
				}
				System.out.println("0)\tCreate a new account");
				System.out.println("1)\tBack to main menu");
				System.out.println("2)\tLogout of your account");
				System.out.println("9)\tExit");
				System.out.print("What would you like to do: ");
				choice = input(accounts);
				log.info("chosen option currently is "+choice);
			}

			switch (choice) {
			case 0:
				int id = createAccount();
				result = getAccount(id).show();
				break;
			case 1:
				result = "main";
				break;
			case 2:
				result = "welcome";
				break;
			case 9:
				result = "exit";
				break;
			default:
				Account a = getAccount(choice);
				if (a == null) {
					log.error(
							"In account menu, chosen option was not 0, 1, 2, 9, or an account held by the user but got to case switch");
					System.out.println("Sorry, something went wrong. Please try again.");
					result = "account";
				} else {
					result = a.show();
				}
			}

		} while (result == "account");
		return result;

	}

	private int input(List<Account> account) {
		Scanner s = new Scanner(System.in);
		int result;
		try {
			result = s.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\nThat is not a valid selection.\n");
			s.nextLine();
			return -1;
		}
		Set<Integer> id = new HashSet<>();
		if (!account.isEmpty()) {
			for (Account a : account)
				id.add(a.getId());
		}
		if (result != 0 && result != 1 && result != 2 && result != 9) {
			if (!id.isEmpty() && !id.contains(result)) {
				System.out.println("\nThat is not a valid selection.\n");
				s.nextLine();
				return -1;
			}
		}
		// flushes anything extra still in the buffer.
		s.nextLine();
		return result;
	}
	/*
	 * public void response() { int select = getInput(); switch (select) { default:
	 * 
	 * } }
	 */

	private List<Account> listAccounts() {
		List<Account> accounts = new ArrayList<>();
		// TODO: get account id list that belong to current user
		List<Integer> id = new ArrayList<>();
		id.add(10);
		id.add(12);
		id.add(15);
		for (Integer i : id) {
			accounts.add(getAccount(i.intValue()));
		}
		return accounts;
	}

	private Account getAccount(int id) {
		// TODO go to psql and get account from id
		String tempType = "checkings";
		double tempBalance = 100.00;
		Account result = new Account(id, tempType, tempBalance);
		return result;
	}

	/*
	 * creates an account and returns the account id number
	 */
	private int createAccount() {
		int id = 0;
		// TODO: got to database and add account
		return id;
	}

	
	/*
	 * Private class that helps display account details and actually makes changes to existing accounts in the database
	 */
	private class Account implements Showable {
		private int id;
		private String type;
		private double balance;

		public Account(int id, String type, double balance) {
			super();
			this.id = id;
			this.type = type;
			this.balance = balance;
		}

		@Override
		public String show() {
			System.out.print("Account #: "+this.getId()+"\tAccount Balance: "+this.getBalance()+"\tAccount Type: "+this.getType());
			return null;
		}

		public int getId() {
			return id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public double getBalance() {
			return balance;
		}

		private synchronized void transferBalance(double add, int to_id) {
			if (add > balance) {
				System.out.println("The selected account does not have the funds to send that amount.");
				return;
			}
			// TODO: go make the change on the database here
			this.balance -= add;
		}

	}
}
