package com.revature;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class PersonalMenu implements Showable {
	static Logger log = Logger.getRootLogger();
	
	private Controller controller;

	private String name;
	private String email;
	private String address;
	private String phone;
	private Date birthday;

	public PersonalMenu(Controller c) {
		controller = c;
		// TODO: go to db and get all info and set them here
	}

	@Override
	public String show() {
		// TODO Auto-generated method stub
		String result = "personal";
		this.setUser(controller.getUser());
		do {
			int choice = -1;
			while (choice == -1) {
				System.out.println("Your personal info:");
				System.out.println("Name: " + name + "\t|\tEmail: " + email + "\nMailing Address: " + address
						+ "\nPhone Number: " + phone + "\t|\tBirthday: " + birthday);
				System.out.println("0)\tEdit mailing address.");
				System.out.println("1)\tEdit phone number.");
				System.out.println("2)\tBack to main menu.");
				System.out.println("3)\tLogout");
				System.out.println("9)\tExit");
				System.out.print("What do you want to do? ");
				choice = input();
			}
			
			switch(choice) {
			case 0:
				editMailingAddress();
				break;
			case 1:
				editPhoneNumber();
				break;
			case 2:
				return "main";
			case 3:
				controller.setUser(null);
				return "welcome";
			case 9:
				controller.setUser(null);
				return "exit";
			default:
				log.error("In personal menu, chosen option was not 0, 1, 2, 3, or 9 but got to case switch");
				System.out.println("Sorry something went wrong. Please try again.");
				return "personal";
			}
		} while (result == "personal");
		return null;
	}



	private void editPhoneNumber() {
		// TODO Auto-generated method stub
		//Scanner s = new Scanner(System.in);
		System.out.print("What is your new phone number? (please enter the number without dashes or parentheses) ");
		String number;
		while((number = checkPhoneInput()) == "false") {
			System.out.println("That is not a valid US phone number.");
			System.out.print("What is your new phone number? (please enter the number without dashes or parentheses) ");
		}
		//TODO: change in psql
		phone = number;
		System.out.println("Your phone number has been successfully updated");
	}
	
	//Makes sure the new phone number input is a possible valid number
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

	private void editMailingAddress() {
		Scanner s = new Scanner(System.in);
		System.out.print("Enter your new mailing address: ");
		//TODO: update on database
		address = s.nextLine();
		System.out.println("Your address has been successfully updated.");
	}

	private int input() {
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
		if (result != 0 && result != 1 && result != 2 && result != 3 && result != 9) {
			System.out.println("\nThat is not a valid selection. Please choose from the given menu.\n");
			return -1;
		}
		return result;
	}

	private void setUser(String user) {
		// get user using email
		name = "Temp Name";
		email = user;
		address = "Temp Address";
		phone = "8583817405";
		birthday = new Date(1992, 5, 2);
	}
}
