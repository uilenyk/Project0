package com.revature;

import org.apache.log4j.Logger;

public class Controller {
	static Logger log = Logger.getRootLogger();
	
	private WelcomeMenu wm;
	private MainMenu mm;
	private AccountMenu am;
	private PersonalMenu pm;
	
	private String currentUser;
	
	public void run() {
		
		String run = "welcome";
		while (!run.equalsIgnoreCase("exit")) {
			switch (run) {
			case "welcome":
				wm = new WelcomeMenu(this);
				run = wm.show();
				break;
			case "main":
				mm = new MainMenu(this);
				run = mm.show();
				break;
			case "account":
				am = new AccountMenu(this);
				run = am.show();
				log.info("run in controller = "+run);
				break;
			case "personal":
				pm = new PersonalMenu(this);
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
