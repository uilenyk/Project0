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
			System.out.println("0)\tAccounts");
			System.out.println("1)\tPersonal Information");
			System.out.println("2)\tLogout");
			System.out.println("9)\tExit");
			System.out.print("What do you want to do: ");
			choice = input();
		}
		log.info("input function returned "+choice);
		switch (choice){
		case 0:
			return "account";
		case 1:
			return "personal";
		case 2:
			controller.setUser(null);
			return "welcome";
		case 9:
			controller.setUser(null);
			return "exit";
		default:
			log.error("In main menu, chosen option was not 0, 1, 2, or 9 but got to case switch");
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
