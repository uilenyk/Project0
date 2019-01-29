package com.revature;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class WelcomeMenu implements Showable {
	static Logger log = Logger.getRootLogger();
	
	public String show() {
		System.out.print("Welcome to the bank. Do you have an existing account?(yes/no) ");
		boolean input = haveAccount();
		if (input) {
			return login();
		} else {
			return createAccount();
		}

	}

	private boolean haveAccount() {
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
			System.out.print("Sorry, that was not a valid input.\nDo you have an existing account?(yes/no) ");
			input = s.nextLine();
		}
		//s.close();
		if (input.equalsIgnoreCase("yes"))
			return true;
		else
			return false;
	}

	private String login() {
		log.info("login function");
		Scanner s = new Scanner(System.in);
		int attempts = 0;
		String user = "";
		String pass = "";
		log.info("user currently = " + user + "and pass = "+pass);
		while (!auth(user, pass) && attempts < 5) {
			if(user != null && pass != null)
				System.out.println("Invlaid email or password.");
			System.out.print("Please enter your email: ");
			//log.info("right before getting next input");
			user = s.nextLine();
			while (user.length() > 64 || !validEmail(user)) {
				System.out.print("That is not a valid email address.\nPlease enter your email: ");
				user = s.nextLine();
			}
			System.out.print("Please enter your password");
			pass = s.nextLine();
			attempts++;
		}
		//s.close();
		if(attempts > 4) {
			System.out.println("Too many wrong attempts. Exitting the program.");
			return "exit";
		}
		return "main";
	}

	private boolean validEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	private boolean auth(String user, String pass) {
		log.info("in auth");
		if (user.isEmpty() || user == null || pass.isEmpty() || pass == null)
			return false;
		//TO DO: go to data base and check hash
		if(user.equals("user@gmail.com") && pass.equals("password")) return true;
		else return false;
	}
	
	private String createAccount() {
		String pass = "";
		pass = passwordHash(pass);
		return "main";
	}
	
	private String passwordHash(String pass) {
		return "";
	}
}
