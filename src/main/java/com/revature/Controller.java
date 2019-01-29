package com.revature;

public class Controller {
	private WelcomeMenu wm;
	private MainMenu mm;
	private AccountMenu am;
	//private PersonalMenu pm;
	
	public Controller() {
		wm = new WelcomeMenu();
		mm = new MainMenu();
		am = new AccountMenu();
		//pm = new PersonalMenu();
	}
	
	public void run() {
		
		String run = "welcome";
		while (!run.equalsIgnoreCase("exit")) {
			switch (run) {
			case "welcome":
				run = wm.show();
				break;
			case "main":
				run = mm.show();
				break;
			case "account":
				run = am.show();
				break;
			case "personal":
				//run = pm.show();
				break;
			}
		}
		System.out.println("Thank you for using our Banking app. Have a nice day.");
	}
}
