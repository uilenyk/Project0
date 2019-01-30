package com.revature;

import java.math.BigDecimal;
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
				System.out.println("2)\tLogout");
				System.out.println("9)\tExit");
				System.out.print("What would you like to do: ");
				choice = input(accounts);
				log.info("chosen option currently is " + choice);
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
					log.info("result in acount menu = "+result);
				}
			}

		} while (result == "account");
		log.info("outside while loop of account menu. result = "+result);
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
		BigDecimal tempBalance = new BigDecimal("100.00");
		Account result = new Account(id, tempType, tempBalance);
		return result;
	}

	/*
	 * creates an account and returns the account id number
	 */
	private int createAccount() {
		int id = 0;
		// TODO: got to database and add account
		//dont forget to add the number of people on the account
		return id;
	}

	/*
	 * Private class that helps display account details and actually makes changes
	 * to existing accounts in the database
	 */
	private class Account implements Showable {
		private int id;
		private String type;
		private BigDecimal balance;
		//in the data table, be sure the keep track of the number of people who are on the account and manually delete accounts.

		public Account(int id, String type, BigDecimal balance) {
			super();
			this.id = id;
			this.type = type;
			this.balance = balance;
		}

		@Override
		public String show() {
			String result = "current";
			do {
				int choice = -1;
				while (choice == -1) {
					System.out.println("\nAccount #: " + this.getId() + "\t|Account Balance: " + this.getBalance()
							+ "\t|Account Type: " + this.getType());
					System.out.println(
							"0)\tTransfer money to another account(you must know the account number of the account you wish to "
									+ "transfer money to)");
					System.out.println("1)\tMake a widthdraw.");
					System.out.println("2)\tMake a deposit");
					System.out.println("3)\tClose this account. (The balance must be 0 for the account to be closed)");
					System.out.println("4)\tGo back to the accounts menu.");
					System.out.println("5)\tGo back to the main menu.");
					System.out.println("6)\tLogout");
					System.out.println("9)\tExit");
					System.out.print("What would you like to do: ");
					choice = input();
				}

				switch (choice) {
				case 0:
					transferBalance();
					break;
				case 1:
					widthdrawMoney();
					break;
				case 2:
					depositMoney();
					break;
				case 3:
					// account is
					if (closeAccount()) {
						return "account";
					} else {
						break;
					}
				case 4:
					return "account";
				case 5:
					return "main";
				case 6:
					return "welcome";
				case 9:
					return "exit";
				default:
					log.error(
							"In specific account, chosen option was not 0, 1, 2, 3, 4, 5, 6, or 9 and got to case switch");
					System.out.println("Sorry, something went wrong. Please try again.");
					result = "current";
				}
			} while (result == "current");
			log.error("Somehow got out of the do while loop without return the account menu");
			return "account";
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

		public BigDecimal getBalance() {
			return balance;
		}

		private int input() {
			Scanner s = new Scanner(System.in);
			int result;
			try {
				result = s.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nThat is not a valid selection. Please enter a number.\n");
				s.nextLine();
				return -1;
			}
			if (result != 0 && result != 1 && result != 2 && result != 3 && result != 4 && result != 5 &&
					result != 6 && result != 9) {
				System.out.println("\nThat is not a valid selection. Please choose from the given menu.\n");
				s.nextLine();
				return -1;
			}
			// flushes anything extra still in the buffer.
			s.nextLine();
			return result;
		}

		private synchronized void transferBalance() {
			// double add, int to_id should be scanned in here and checked
			// also needs a way to go back to the accounts menu
			BigDecimal amount = new BigDecimal("-1");
			while(amount.compareTo(new BigDecimal("-1")) == -1) {
				amount = checkAmount();
			}
			if (balance.compareTo(amount) == -1) {// if balance is less than given bigint
				System.out.println("The selected account does not have the funds to send that amount.");
				return;
			}
			// TODO: go make the change on the database here
			this.balance.subtract(amount);
		}

		/*
		 * returns true if account was successfully clsoed and false if the account is
		 * still open
		 */
		private boolean closeAccount() {
			if (balance.compareTo(new BigDecimal("0")) != 0) {
				System.out.println("You cannot close this account because there is still money on this account");
				return false;
			}
			// Scanner s = new Scanner(System.in);
			System.out.print("Are you sure you want to close this account (yes/no)? ");
			boolean close = yesOrNo();
			if (close) {
				// TODO: close account and delete account from tables
				return true;
			} else {
				return false;
			}
		}

		private boolean yesOrNo() {
			Scanner s = new Scanner(System.in);
			String input = s.nextLine();
			// checks for a valid input
			while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
				System.out.print("Sorry, that was not a valid input.\nDo you have an existing account?(yes/no) ");
				input = s.nextLine();
			}
			if (input.equalsIgnoreCase("yes"))
				return true;
			else
				return false;
		}

		private void widthdrawMoney() {
			System.out.print("How much money would you like to widthdraw? ");
			BigDecimal amount = new BigDecimal("-1");
			while (amount.compareTo(new BigDecimal("-1")) == 0) {
				amount = checkAmount();
			}
			if (balance.compareTo(amount) == -1) {
				System.out.println("You do not have enough in this account to widthdraw that amount.");
				return;
			} else {
				// TODO: change database amount
				balance = balance.subtract(amount);
				return;
			}
		}

		private void depositMoney() {
			System.out.print("How much money would you like to deposit? ");
			BigDecimal amount = new BigDecimal("-1");
			while (amount.compareTo(new BigDecimal("-1")) == 0) {
				amount = checkAmount();
			}
			// TODO: change database amount
			balance = balance.add(amount);
		}

		private BigDecimal checkAmount() {
			Scanner s = new Scanner(System.in);
			BigDecimal result;
			try {
				result = new BigDecimal(s.next());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid dollar amount.");
				return new BigDecimal("-1");
			}
			s.nextLine();
			return result;
		}
	}
}
