package com.revature;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.dao.QueryStatement;

public class AccountMenu implements Showable {
	static Logger log = Logger.getRootLogger();

	private Controller controller;

	private Map<Integer, Account> accounts = new HashMap<>();

	public AccountMenu(Controller c) {
		controller = c;
	}

	public String show() {
		String result = "account";
		do {
			listAccounts();
			int choice = -1;
			while (choice == -1) {
				if (accounts.isEmpty()) {
					System.out.println("You currently have no open accounts.");
				} else {
					System.out.println("Accounts #\t|\tBalance\t|\tType");
					for (Account a : accounts.values()) {
						System.out.println(a.getId() + "\t\t|\t" + a.getBalance() + "\t|\t" + a.getType());
					}
					System.out.println(
							"Enter the account number of the account you want to access or an option from below:");
				}
				System.out.println("0)\tCreate a new account");
				System.out.println("1)\tBack to main menu");
				System.out.println("2)\tLogout");
				System.out.println("9)\tExit");
				System.out.print("What would you like to do: ");
				choice = input(accounts.keySet());
				log.info("chosen option currently is " + choice);
			}
			Account a;
			switch (choice) {
			case 0:
				a = createAccount();
				accounts.put(a.getId(), a);
				result = a.show();
				break;
			case 1:
				result = "main";
				break;
			case 2:
				controller.setUser(null);
				result = "welcome";
				break;
			case 9:
				controller.setUser(null);
				result = "exit";
				break;
			default:
				// accounts is a hashmap of account id to accounts
				a = accounts.get(choice);
				if (a == null) {
					log.error(
							"In account menu, chosen option was not 0, 1, 2, 9, or an account held by the user but got to case switch");
					System.out.println("Sorry, something went wrong. Please try again.");
					result = "account";
				} else {
					result = a.show();
					log.info("result in acount menu = " + result);
				}
			}

		} while (result == "account");
		log.info("outside while loop of account menu. result = " + result);
		return result;

	}

	private int input(Set<Integer> account) {
		Scanner s = new Scanner(System.in);
		int result;
		try {
			result = s.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\nThat is not a valid selection. Please enter a number.\n");
			return -1;
		} finally {
			s.nextLine();
		}
		if (result != 0 && result != 1 && result != 2 && result != 9) {
			if (account.isEmpty() || !account.contains(result)) {
				System.out.println("\nThat is not a valid selection. Please choose from the menu provided.\n");
				return -1;
			}
		}
		return result;
	}
	/*
	 * public void response() { int select = getInput(); switch (select) { default:
	 * 
	 * } }
	 */

	private Map<Integer, Account> listAccounts() {
		// List<Account> accounts = new ArrayList<>();
		// TODO: get account id list that belong to current user
		String email = controller.getUser();
		List<Map<String, String>> id = QueryStatement.getAccounts(email);
		for (Map<String, String> r : id) {
			accounts.put(Integer.parseInt(r.get("id")),
					getAccount(Integer.parseInt(r.get("id")), r.get("type"), new BigDecimal(r.get("balance"))));
		}
		return accounts;
	}

	private Account getAccount(int id, String type, BigDecimal balance) {
		Account result = new Account(id, type, balance, this.controller);
		return result;
	}

	/*
	 * creates an account and returns the account id number
	 */
	private Account createAccount() {
		int id = 0;
		// TODO: got to database and add account
		// dont forget to add the number of people on the account
		String type = checkAccountType();
		log.info("Account type user wants to open: " + type);
		BigDecimal balance = new BigDecimal("0");
		id = QueryStatement.insertAccount(balance, type, 1, controller.getUser());
		Account result = getAccount(id, type, balance);
		return result;
	}

	private String checkAccountType() {
		String result;
		Scanner s = new Scanner(System.in);
		do {
			System.out.print("Do you want to open a savings or checkings account? ");
			result = s.nextLine();
			log.debug("account type got from user: " + result);
			result = result.trim();
			if (!result.equalsIgnoreCase("checkings") && !result.equalsIgnoreCase("savings")) {
				System.out.println("That is not a valid accout type. Please try again.");
			}
		} while (!result.equalsIgnoreCase("checkings") && !result.equalsIgnoreCase("savings"));
		return result;
	}

	/*
	 * Private class that helps display account details and actually makes changes
	 * to existing accounts in the database
	 */
	private class Account implements Showable {
		private int id;
		private String type;
		private BigDecimal balance;

		private Controller controller;
		// in the data table, be sure the keep track of the number of people who are on
		// the account and manually delete accounts.

		public Account(int id, String type, BigDecimal balance, Controller c) {
			super();
			this.id = id;
			this.type = type;
			this.balance = balance;
			controller = c;
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
					System.out.println("3)\tGo back to the accounts menu.");
					System.out.println("4)\tGo back to the main menu.");
					System.out.println("5)\tLogout");
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
					return "account";
				case 4:
					return "main";
				case 5:
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
			if (result != 0 && result != 1 && result != 2 && result != 3 && result != 4 && result != 5 && result != 6
					&& result != 9) {
				System.out.println("\nThat is not a valid selection. Please choose from the given menu.\n");
				s.nextLine();
				return -1;
			}
			// flushes anything extra still in the buffer.
			s.nextLine();
			return result;
		}

		private synchronized void transferBalance() {
			System.out.print("Enter the account number of the account you want to transfer to: ");
			Scanner s = new Scanner(System.in);
			int account;
			do {// checks for valid account id input
				try {
					account = s.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("\nThat is not a valid selection. Please enter a number.\n");
					s.nextLine();
					account = -1;
				}
				if (account < 100) {
					System.out.println("\nThat is not a valid selection. Please enter a number.\n");
					s.nextLine();
					account = -1;
				}
			} while (account == -1);
			if (!QueryStatement.checkAccount(account)) {// checks if the account id exists
				System.out.println("That account does not exist.");
				return;
			}
			BigDecimal amount;
			do {
				System.out.print("How much money would you like to transfer? ");
				amount = checkAmount();
				if (amount.equals(new BigDecimal("-1"))) {
					System.out.println("That is not a valid amount. Please try again.");
				}
			} while (amount.compareTo(new BigDecimal("-1")) == 0);

			if (balance.compareTo(amount) == -1) {// if balance is less than given bigint
				System.out.println("The selected account does not have the funds to send that amount.");
				return;
			}
			// TODO: go make the change on the database here
			QueryStatement.transfer(this.getId(), account, amount);
			this.balance = this.balance.subtract(amount);
		}

		private void widthdrawMoney() {
			BigDecimal amount = new BigDecimal("-1");
			do {
				System.out.print("How much money would you like to widthdraw? ");
				amount = checkAmount();
				if (amount.equals(new BigDecimal("-1"))) {
					System.out.println("That is not a valid amount. Please try again.");
				}
			} while (amount.compareTo(new BigDecimal("-1")) == 0);
			if (balance.compareTo(amount) == -1) {
				System.out.println("You do not have enough in this account to widthdraw that amount.");
				return;
			} else {
				// TODO: change database amount
				QueryStatement.widthdraw(this.getId(), amount);
				balance = balance.subtract(amount);
				return;
			}
		}

		private void depositMoney() {
			BigDecimal amount = new BigDecimal("-1");
			do {
				System.out.print("How much money would you like to deposit? ");
				amount = checkAmount();
			} while (amount.compareTo(new BigDecimal("-1")) == 0);
			// TODO: change database amount
			boolean success = QueryStatement.deposit(this.getId(), amount);
			if (success)
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
			} finally {
				s.nextLine();
			}
			return result;
		}
	}
}
