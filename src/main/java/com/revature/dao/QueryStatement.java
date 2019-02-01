package com.revature.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryStatement {

	public static boolean insertUser(String user, String hash, String salt, String number, String first, String last, String address, Date birthday) {
		System.out.println("url = "+System.getenv("psql_url"));
		System.out.println("role = "+System.getenv("psql_role"));
		System.out.println("pass = "+System.getenv("psql_pass"));
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))) {
			String query = "INSERT INTO users (email, first_name, last_name, passhash, salt, birthday, phone_number, address)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			statement.setString(2, first);
			statement.setString(3, last);
			statement.setString(4, hash);
			statement.setString(5,  salt);
			statement.setDate(6, birthday);
			statement.setString(7, number);
			statement.setString(8, address);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean insertAccount(BigDecimal balance, String account_type, int account_members, String user) {
		System.out.println("url = "+System.getenv("psql_url"));
		System.out.println("role = "+System.getenv("psql_role"));
		System.out.println("pass = "+System.getenv("psql_pass"));
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))) {
			String query = "INSERT INTO accounts(balance, account_type, account_members) VALUES (?, ?, ?) RETURNING id;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setBigDecimal(1, balance);
			statement.setString(2, account_type);
			statement.setInt(3, account_members);
			
			ResultSet rs = statement.executeQuery();
			int account_id = 0;
			if(rs.next()) {
				account_id = rs.getInt("id");
			}
			
			query = "INSERT INTO user_accounts(user_id, account) VALUES(?, ?);";
			
			statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			statement.setInt(2, account_id);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
