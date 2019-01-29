package com.revature;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class WelcomeAuthTest {
	static private WelcomeMenu  m;
	
	@Before
	public void setup() {
		m = new WelcomeMenu();
	}
	
	@Test
	public void repeatFailLogin() {
		String result = m.show();
		assertEquals("exit", result);
	}
}
