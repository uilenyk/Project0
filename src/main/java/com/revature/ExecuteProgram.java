package com.revature;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import org.mindrot.jbcrypt.BCrypt;

public class ExecuteProgram {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//Controller program = new Controller();
		//program.run();
		//WelcomeMenu m = new WelcomeMenu();
		//m.show();
		//MainMenu mm = new MainMenu();
		//mm.show();
		//AccountMenu am = new AccountMenu();
		//am.show();
		SecureRandom sr = new SecureRandom();
		//int salt = sr.nextint()
		String salt = BCrypt.gensalt(12);
		String hash = BCrypt.hashpw("password", salt);
		System.out.println(hash);
	}
}
