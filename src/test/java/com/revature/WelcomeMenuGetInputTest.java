package com.revature;

import org.junit.BeforeClass;
import org.junit.Test;

public class WelcomeMenuGetInputTest {
	static private WelcomeMenu wm;
	
	@BeforeClass
	public static void setup() {
		wm = new WelcomeMenu();
	}
	
	@Test
	public void intInput() {
		wm.show();
	}
}
