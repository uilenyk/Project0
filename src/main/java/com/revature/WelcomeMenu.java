package com.revature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class WelcomeMenu implements Showable {
	static Logger log = Logger.getRootLogger();
	private static final Set<String> badpass = new HashSet<>();
	
	/*
	 * sets up the set that holds common passwords to prevent people from setting their password to something that is too common
	 */
	public WelcomeMenu() {
		if (badpass.isEmpty()) {
			try(BufferedReader br = new BufferedReader(new FileReader(new File("./src/main/resources/badpassword.txt")))) {
				String line;
				while((line = br.readLine()) != null) {
					log.info("The bad password that was just read: "+line);
					badpass.add(line);
				}
				
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
	}
	
	/*
	 * Runs the welcome menu and returns the next destination back to the controller class as a string
	 * @see com.revature.Showable#show()
	 */
	public String show() {
		System.out.print("Welcome to the bank. Do you have an existing account?(yes/no) ");
		boolean input = haveAccount();
		if (input) {
			return login();
		} else {
			return createAccount();
		}

	}

	/*
	 * Returns true if user wants to login and false if user wants to go to account creation
	 */
	private boolean haveAccount() {
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		//checks for a valid input
		while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
			System.out.print("Sorry, that was not a valid input.\nDo you have an existing account?(yes/no) ");
			input = s.nextLine();
		}
		if (input.equalsIgnoreCase("yes"))
			return true;
		else
			return false;
	}

	/*
	 * Logs the user in if the correct email and password is provided. The user has 5 tries before the program exits because of
	 * too many failed attempts.
	 */
	private String login() {
		log.info("login function");
		Scanner s = new Scanner(System.in);
		//attempt 1 is outside the loop so attempts starts at 1
		int attempts = 1;
		System.out.print("Please enter your email: ");
		String user = s.nextLine();
		System.out.print("Please enter your password");
		String pass = s.nextLine();
		log.info("user currently = " + user + "and pass = "+pass);
		//tries to authenticate the user giving them 5 attempts
		while (!auth(user, pass) && attempts < 5) {
			if(user != null && pass != null)
				System.out.println("Invlaid email or password.");
			System.out.print("Please enter your email: ");
			//log.info("right before getting next input");
			user = s.nextLine();
			System.out.print("Please enter your password");
			pass = s.nextLine();
			attempts++;
		}
		//fails to authenticate and exits the program
		if(attempts > 4) {
			System.out.println("Too many wrong attempts. Exitting the program.");
			return "exit";
		}
		//successful authentication and brings user to the main menu
		return "main";
	}

	/*
	 * checks for a valid email address using regular expressions
	 */
	private boolean validEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/*
	 * Attempts to authenticate the user using the given user email and password
	 */
	private boolean auth(String user, String pass) {
		log.info("in auth");
		if (user.isEmpty() || user == null || pass.isEmpty() || pass == null)
			return false;
		if(!validEmail(user)) return false;
		//TO DO: go to data base and check hash
		if(user.equals("user@gmail.com") && pass.equals("password")) return true;
		else return false;
	}
	
	/*
	 * creates leads the user through account creation and pushes data to the db. Then automatically logs the new user in
	 * and brings them to the main menu.
	 * fields:
	 * -user(email)
	 * -password
	 * -first name
	 * -last name
	 * -phone number
	 * -date of birth
	 */
	private String createAccount() {
		Scanner s = new Scanner(System.in);
		String user = "";
		String pass = "";
		pass = passwordHash(pass);
		//checks for a valid email patter to be entered
		while (user.length() > 64 || !validEmail(user)) {
			System.out.print("That is not a valid email address.\nPlease enter your email: ");
			user = s.nextLine();
		}
		return "main";
	}
	
	/*
	 * Password hashing method
	 */
	private String passwordHash(String pass) {
		return "";
	}
}
