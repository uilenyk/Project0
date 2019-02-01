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
import org.mindrot.jbcrypt.BCrypt;

public class WelcomeMenu implements Showable {
	static Logger log = Logger.getRootLogger();
	private static final Set<String> badpass = new HashSet<>();

	private Controller controller;

	public WelcomeMenu(Controller c) {
		controller = c;
	}

	/*
	 * sets up the set that holds common passwords to prevent people from setting
	 * their password to something that is too common
	 */
	public WelcomeMenu() {
		if (badpass.isEmpty()) {
			try (BufferedReader br = new BufferedReader(
					new FileReader(new File("./src/main/resources/badpassword.txt")))) {
				String line;
				while ((line = br.readLine()) != null) {
					log.info("The bad password that was just read: " + line);
					badpass.add(line);
				}

			} catch (IOException e) {
				log.error(e.toString());
			}
		}
	}

	/*
	 * Runs the welcome menu and returns the next destination back to the controller
	 * class as a string
	 * 
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
	 * Returns true if user wants to login and false if user wants to go to account
	 * creation
	 */
	private boolean haveAccount() {
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

	/*
	 * Logs the user in if the correct email and password is provided. The user has
	 * 5 tries before the program exits because of too many failed attempts.
	 */
	private String login() {
		log.info("login function");
		Scanner s = new Scanner(System.in);
		// attempt 1 is outside the loop so attempts starts at 1
		int attempts = 1;
		System.out.print("Please enter your email: ");
		String user = s.nextLine();
		user = user.trim();
		System.out.print("Please enter your password: ");
		String pass = s.nextLine();
		log.info("user currently = " + user + "and pass = " + pass);
		// tries to authenticate the user giving them 5 attempts
		while (!auth(user, pass) && attempts < 5) {
			if (user != null && pass != null)
				System.out.println("Invlaid email or password.");
			System.out.print("Please enter your email: ");
			// log.info("right before getting next input");
			user = s.nextLine();
			System.out.print("Please enter your password");
			pass = s.nextLine();
			attempts++;
		}
		// fails to authenticate and exits the program
		if (attempts > 4) {
			System.out.println("Too many wrong attempts. Exitting the program.");
			return "exit";
		}
		// successful authentication and brings user to the main menu
		controller.setUser(user);
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
		if (!validEmail(user))
			return false;
		// TO DO: go to data base to get salt and hashed password
		String hash = BCrypt.hashpw("password", "salt");
		String password = BCrypt.hashpw(pass, "salt");
		if (hash.equals(password))
			return true;
		if (user.equals("uilenyk@gmail.com") && pass.equals("password"))
			return true;
		else
			return false;
	}

	/*
	 * creates leads the user through account creation and pushes data to the db.
	 * Then automatically logs the new user in and brings them to the main menu.
	 * fields: -user(email) -salt -password -first name -last name -phone number
	 * -date of birth -mailing address
	 */
	private String createAccount() {
		Scanner s = new Scanner(System.in);
		String user = newEmail();
		//if user enters a email that is already in the db, the program will return user to the welcome menu
		if (user.equalsIgnoreCase("login")) {
			return "welcome";
		}
		String pass = newPassword();
		String salt = BCrypt.gensalt(12);
		String hash = BCrypt.hashpw(pass, salt);
		System.out.print("Please enter your first name: ");
		String firstName = s.nextLine();
		//s.nextLine();
		System.out.print("Please enter your last name: ");
		String lastName = s.nextLine();
		//s.nextLine();
		System.out.print("Please enter your mailing address: ");
		String address = s.nextLine();
		System.out.print("Please enter your phone number (please enter the number without dashes or parentheses): ");
		String number;
		while((number = checkPhoneInput()) == "false") {
			System.out.println("That is not a valid US phone number.");
			System.out.print("Pleae enter your phone number (please enter the number without dashes or parentheses): ");
		}
		
		// TODO: create new row and save all data to db
		// checks for a valid email patter to be entered
		while (user.length() > 64 || !validEmail(user)) {
			System.out.print("That is not a valid email address.\nPlease enter your email: ");
			user = s.nextLine();
		}
		controller.setUser(user);
		return "main";
	}
	
	private String checkPhoneInput() {
		Scanner s = new Scanner(System.in);
		String number = s.next();
		s.nextLine();
		if(number.matches("[0-9]+") && number.length() == 10) {
			return number;
		} else {
			return "false";
		}
	}

	private String newPassword() {
		Scanner s = new Scanner(System.in);
		String pass;
		boolean match;
		do {
			System.out.println(
					"The password must be at least 8 characters long and contain one number, one lowercase letter, and one"
							+ " uppercase letter.");
			do {
				System.out.print("Please enter your password: ");
				pass = s.nextLine();
				pass = pass.trim();
			} while (!validPass(pass));
			System.out.print("Please retype your password: ");
			String passMatch = s.nextLine();
			if (pass.equals(passMatch))
				match = true;
			else {
				match = false;
				System.out.println("Your passwords did not match. Please try again.");
			}
		} while (!match);
		return pass;
	}

	private boolean validPass(String pass) {
		String checkNumberRegex = ".*[0-9].*";
		String checkLowerRegex = ".*[a-z].*";
		String checkUpperRegex = ".*[A-Z].*";
		if(badpass.contains(pass)) {
			System.out.println("This password is too common. Please try again.");
			return false;
		} else if ((!pass.matches(checkNumberRegex) || !pass.matches(checkLowerRegex) || !pass.matches(checkUpperRegex)) || pass.length() < 9) {
			System.out.println("This password does not meet the password requirements. Please try again.");
			return false;
		}
		return true;
	}

	private String newEmail() {
		String email;
		boolean valid;
		Scanner s = new Scanner(System.in);
		do {
			System.out.print("Enter your password: ");
			email = s.nextLine();
			if (!validEmail(email)) {
				System.out.println("That is not a valid email.");
				valid = false;
			} else {/* if (check if email is in database) */
				// System.out.println("This email is already in use. Please login or use another
				// email");
				// return "login";
				valid = true;
			}
		} while (valid);
		return email;
	}

}
