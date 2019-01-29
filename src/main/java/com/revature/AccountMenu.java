package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountMenu implements Showable {
	public String show() {
		String result = "account";
		do {
			List<Account> accounts = listAccounts();
			if (accounts.isEmpty()) {
				System.out.println("You currently have no open accounts.");
			} else {
				System.out.println("Accounts #\t|\tBalance");
				for (Account a : accounts) {
					System.out.println(a.getId() + "\t|\t" + a.getBalance());
				}
				System.out.println("Enter the account number of the account you want to access or an option from below:");
			}
			System.out.println("0)\tCreate a new account");
			System.out.println("1)\tBack to main menu");
			System.out.println("2)\tLogout of your account");
			System.out.println("9)\tExit");
			System.out.print("What would you like to do: ");
			
		} while (result == "account");
		return result;
	}

	private int getInput() {

		return 0;
	}

	public void response() {
		int select = getInput();
		switch (select) {
		default:

		}
	}

	private List<Account> listAccounts() {
		List<Account> accounts = new ArrayList<>();
		// TODO: get account id list that belong to current user
		List<Integer> id = new ArrayList<>();
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

	private void createAccount(int balance) {
		// TODO: got to database and add account
	}

	private class Account {
		private int id;
		private String type;
		private double balance;

		public Account(int id, String type, double balance) {
			super();
			this.id = id;
			this.type = type;
			this.balance = balance;
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

		public synchronized void transferBalance(double add, int to_id) {
			if (add > balance) {
				System.out.println("The selected account does not have the funds to send that amount.");
				return;
			}
			// TODO: go make the change on the database here
			balance -= add;
		}
	}
}
