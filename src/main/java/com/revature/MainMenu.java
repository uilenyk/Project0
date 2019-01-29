package com.revature;

import java.util.Scanner;

public final class MainMenu implements Showable{
	public String show() {
		System.out.println("Select from the menu below by entering the corrisponding number:");
		System.out.println("0./tExit");
		System.out.println("1./tAccounts");
		System.out.println("2./tPersonal Information");
		return "";
	}
	
	private int input() {
		Scanner s = new Scanner(System.in);
		int result = 0;
		return result;
	}
	
}
