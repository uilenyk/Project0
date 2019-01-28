package com.revature;

/**
 * accept an integer
 * if value is divisible by 3, return fizz
 * if value is divisible by 5, return buzz
 * if value is divisible by both, return fizzbuzz
 * else return the value
 * @author Kyne Liu
 *
 */
public class FizzBuzzTest {

	public String fizzbuzz(int n) {
		if(n%3 == 0 && n%5 == 0)
			return "FizzBuzz";
		else if(n%3 == 0)
			return "Fizz";
		else if(n%5 == 0)
			return "Buzz";
		else
			return Integer.toString(n);
	}
}
