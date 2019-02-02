package com.revature;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.revature.dao.QueryStatement;

public class ExecuteProgram {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		Controller program = new Controller();
		program.run();
		//WelcomeMenu m = new WelcomeMenu();
		//m.show();
		//MainMenu mm = new MainMenu();
		//mm.show();
		//AccountMenu am = new AccountMenu();
		//am.show();
		//int salt = sr.nextint()
		//String salt = BCrypt.gensalt(12);
		//String hash = BCrypt.hashpw("password", salt);
		//QueryStatement.insertUser("user@gmail.com", hash, salt, "8583817405", "Kyne", "Liu", "address", new Date(2, 5, 1992));
		
		//System.out.println(QueryStatement.getAccounts("user@gmail.com"));
	}
}
