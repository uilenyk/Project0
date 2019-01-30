package com.revature;

import org.apache.log4j.Logger;

public class Controller {
	static Logger log = Logger.getRootLogger();
	
	private WelcomeMenu wm;
	private MainMenu mm;
	private AccountMenu am;
	private PersonalMenu pm;
	
	private String currentUser;
	
	public Controller() {
		wm = new WelcomeMenu(this);
		mm = new MainMenu(this);
		am = new AccountMenu(this);
		pm = new PersonalMenu(this);
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
				log.info("run in controller = "+run);
				break;
			case "personal":
				run = pm.show();
				break;
			}
		}
		System.out.println("Thank you for using our Banking app. Have a nice day.");
	}
	
	public String getUser() {
		return currentUser;
	}
	
	public void setUser(String u) {
		currentUser = u;
	}
}
