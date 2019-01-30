package com.revature;

import java.sql.Date;

public class PersonalMenu implements Showable{
	private String name;
	private String email;
	private String address;
	private String phone;
	private Date birthday;
	
	public PersonalMenu() {
		//TODO: go to db and get all info and set them here
	}
	
	@Override
	public String show() {
		// TODO Auto-generated method stub
		System.out.println("Your personal info:");
		System.out.println("Name: "+name+"\tEmail: "+email+"\nMailing Address: "+address+"\nPhone Number: "+phone+"\tBirthday: "+birthday);
		System.out.println("0)\tEdit mailing address.");
		System.out.println("1)\tEdit phone number.");
		System.out.println("2)\tBack to main menu.");
		System.out.println("3)\tLogout");
		System.out.println("9)\tExit");
		
		return null;
	}

}
