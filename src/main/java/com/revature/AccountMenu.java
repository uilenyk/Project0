package com.revature;

public class AccountMenu implements Showable{
	public String show() {
		System.out.print("Enter the account number of the account you want to access or enter"
				+ " 0 to create a new account: ");
		return "";
	}
	
	private int getInput() {
		
		return 0;
	}

	public void response() {
		int select = getInput();
		switch(select) {
		default:
			
		}
	}
}
