package com.revature;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.FizzBuzzTest;

/**
 * Unit testing is the process of writing a test that tests the behavior of the code on the most
 * granular level. Generally, this means testing individual methods, by providing input values and 
 * asserting an expected output value.
 * @author Kyne Liu
 *
 */
public class FizzBuzzUnitTest {
	static FizzBuzzTest f;
	
	/*
	 * THis method will be run before any unit tests in the class. THis can be used to set up the
	 * class for testing. In addition there are @Before - before each test, @After - after each test
	 * @AfterClass - after all tests have run
	 */
	@BeforeClass
	public static void setup() {
		f = new FizzBuzzTest();
	}
	
	@Test
	public void test15() {
		String result = f.fizzbuzz(15);
		assertEquals("FizzBuzz", result);
	}
	
	@Test
	public void test3() {
		String result = f.fizzbuzz(3);
		assertEquals("Fizz", result);
	}
	
	@Test
	public void test22() {
		String result = f.fizzbuzz(22);
		assertEquals("22", result);
	}
}
