package com.revature;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public final class MainMenu implements Showable{
	static Logger log = Logger.getRootLogger();
	
	private Controller controller;
	
	public MainMenu(Controller c) {
		controller = c;
	}
	
	public String show() {
		int choice = -1;
		while(choice == -1) {
			System.out.println("0)\tLogout");
			System.out.println("1)\tAccounts");
			System.out.println("2)\tPersonal Information");
			System.out.println("9)\tExit");
			System.out.print("Select from the menu above by entering the corrisponding number: ");
			choice = input();
		}
		log.info("input function returned "+choice);
		switch (choice){
		case 0:
			return "welcome";
		case 1:
			return "account";
		case 2:
			return "personal";
		case 9:
			return "exit";
		default:
			log.error("In main menu, chosen option was not 0, 1, or 2 but got to case switch");
			System.out.println("Sorry something went wrong. Please try again.");
			return "main";
		}
	}
	
	/*
	 * Insures a valid choice is made from the given options.
	 */
	private int input() {
		Scanner s = new Scanner(System.in);
		int result;
		try{
			result = s.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\nThat is not a valid selection.");
			s.nextLine();
			return -1;
		}
		if(result != 0 && result != 1 && result != 2 && result != 9) {
			System.out.println("\nThat is not a valid selection.");
			s.nextLine();
			return -1;
		}
		//flushes anything extra still in the buffer.
		s.nextLine();
		return result;
	}

}
