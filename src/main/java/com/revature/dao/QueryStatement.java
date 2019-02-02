package com.revature.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public static int insertAccount(BigDecimal balance, String account_type, int account_members, String user) {
		System.out.println("url = "+System.getenv("psql_url"));
		System.out.println("role = "+System.getenv("psql_role"));
		System.out.println("pass = "+System.getenv("psql_pass"));
		int account_id = 0;
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))) {
			String query = "INSERT INTO accounts(balance, account_type, account_members) VALUES (?, ?, ?) RETURNING id;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setBigDecimal(1, balance);
			statement.setString(2, account_type);
			statement.setInt(3, account_members);
			
			ResultSet rs = statement.executeQuery();
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
			return account_id;
		}
		return account_id;
	}
	
	public static boolean userExists(String user) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT email FROM users WHERE email = ?;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			
			ResultSet rs = statement.executeQuery();
			
			if(rs.next()) {
				System.out.println("user = "+rs.getString("email"));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public static List<ResultSet> getAccounts(String user) {
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT account FROM user_accounts WHERE user_id = ?;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			
			ResultSet rs = statement.executeQuery();
			List<ResultSet> account = new ArrayList<>();
			while(rs.next()) {
				query = "SELECT * FROM accounts WHERE id = ?";
				statement = conn.prepareStatement(query);
				statement.setInt(1, rs.getInt("account"));
				ResultSet result = statement.executeQuery();
				account.add(result);
			}
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<ResultSet>();
	}
	
	public static ResultSet getHash(String user) {
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT passhash, salt FROM users WHERE email = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			ResultSet rs = statement.executeQuery();
			return rs;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean deposit(int id, BigDecimal deposit) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT balance FROM accounts WHERE id = ?;";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			rs.next();
			BigDecimal newBalance = rs.getBigDecimal("balance");
			newBalance = newBalance.add(deposit);
			
			query = "UPDATE accounts SET balance = ? WHERE id = ?;";
			statement = conn.prepareStatement(query);
			statement.setBigDecimal(1, newBalance);
			statement.setInt(2, id);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Sorry, something went wrong.");
			return false;
		}
	}
}
