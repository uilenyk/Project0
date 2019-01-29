package com.revature;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public final class MainMenu implements Showable{
	static Logger log = Logger.getRootLogger();
	
	public String show() {
		int choise = -1;
		while(choise == -1) {
			System.out.println("0.\tExit");
			System.out.println("1.\tAccounts");
			System.out.println("2.\tPersonal Information");
			System.out.print("Select from the menu above by entering the corrisponding number: ");
			choise = input();
		}
		log.info("input function returned "+choise);
		switch (choise){
		case 0:
			return "exit";
		case 1:
			return "account";
		case 2:
			return "personal";
		default:
			log.error("In main menu, chosen option was not 0, 1, or 2 but got to case switch");
			return "main";
		}
	}
	
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
		if(result != 0 && result != 1 && result != 2) {
			System.out.println("\nThat is not a valid selection.");
			s.nextLine();
			return -1;
		}
		s.nextLine();
		return result;
	}
	
}
