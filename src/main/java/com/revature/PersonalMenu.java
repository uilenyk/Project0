package com.revature;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PersonalMenu implements Showable {
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
				System.out.println("Name: " + name + "\tEmail: " + email + "\nMailing Address: " + address
						+ "\nPhone Number: " + phone + "\tBirthday: " + birthday);
				System.out.println("0)\tEdit mailing address.");
				System.out.println("1)\tEdit phone number.");
				System.out.println("2)\tBack to main menu.");
				System.out.println("3)\tLogout");
				System.out.println("9)\tExit");
				choice = input();
			}
		} while (result == "personal");
		return null;
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
		if (result != 0 && result != 1 && result != 2 && result != 3 && result != 9) {
			System.out.println("\nThat is not a valid selection. Please choose from the given menu.\n");
			s.nextLine();
			return -1;
		}
		// flushes anything extra still in the buffer.
		s.nextLine();
		return result;
	}

	private void setUser(String user) {
		// get user using email
		name = "Temp Name";
		email = user;
		address = "Temp Address";
		phone = "8583817405";
		birthday = new Date(5, 2, 1992);
	}
}
