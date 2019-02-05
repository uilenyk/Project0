package com.revature.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryStatement {

	public static boolean insertUser(String user, String hash, String salt, String number, String first, String last, String address) {
		System.out.println("url = "+System.getenv("psql_url"));
		System.out.println("role = "+System.getenv("psql_role"));
		System.out.println("pass = "+System.getenv("psql_pass"));
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))) {
			String query = "INSERT INTO users (email, first_name, last_name, passhash, salt, phone_number, address)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			statement.setString(2, first);
			statement.setString(3, last);
			statement.setString(4, hash);
			statement.setString(5,  salt);
			statement.setString(6, number);
			statement.setString(7, address);
			
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
	
	public static boolean checkAccount(int id) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT id FROM accounts WHERE id = ?;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setInt(1, id);
			
			ResultSet rs = statement.executeQuery();
			
			if(rs.next()) {
				System.out.println("user = "+rs.getString("id"));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public static List<Map<String, String>> getAccounts(String user) {
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT account FROM user_accounts WHERE user_id = ?;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			
			ResultSet rs = statement.executeQuery();
			List<Map<String, String>> account = new ArrayList<>();
			while(rs.next()) {
				query = "SELECT * FROM accounts WHERE id = ?";
				statement = conn.prepareStatement(query);
				statement.setInt(1, rs.getInt("account"));
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					Map<String, String> a = new HashMap<>();
					a.put("id", String.valueOf(result.getInt("id")));
					a.put("balance", String.valueOf(result.getBigDecimal("balance")));
					a.put("type", result.getString("account_type"));
					account.add(a);
				}
			}
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, String>>();
	}
	
	public static Map<String, String> getHash(String user) {
		try(Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT passhash, salt FROM users WHERE email = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, user);
			ResultSet rs = statement.executeQuery();
			Map<String, String> result = new HashMap<>();
			if (rs.next()) {
				result.put("hash", rs.getString("passhash"));
				result.put("salt", rs.getString("salt"));
			}
			return result;
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
	
	public static boolean widthdraw(int id, BigDecimal widthdraw) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT balance FROM accounts WHERE id = ?;";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			
			rs.next();
			BigDecimal newBalance = rs.getBigDecimal("balance");
			newBalance = newBalance.subtract(widthdraw);
			
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
	
	//TODO: transfer
	public static boolean transfer(int from, int to, BigDecimal amount) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String fromQuery = "SELECT balance FROM accounts WHERE id = ?;";
			String toQuery = "SELECT balance FROM accounts WHERE id = ?;";
			PreparedStatement fromStatement = conn.prepareStatement(fromQuery);
			PreparedStatement toStatement = conn.prepareStatement(toQuery);
			fromStatement.setInt(1, from);
			toStatement.setInt(1, to);
			ResultSet fromRS = fromStatement.executeQuery();
			ResultSet toRS = toStatement.executeQuery();
			
			BigDecimal newFromBalance;
			BigDecimal newToBalance;
			fromRS.next();
			toRS.next();
			newFromBalance = fromRS.getBigDecimal("balance");
			newFromBalance = newFromBalance.subtract(amount);
			newToBalance = toRS.getBigDecimal("balance");
			newToBalance = newToBalance.add(amount);
			
			fromQuery = "UPDATE accounts SET balance = ? WHERE id = ?;";
			fromStatement = conn.prepareStatement(fromQuery);
			fromStatement.setBigDecimal(1, newFromBalance);
			fromStatement.setInt(2, from);
			toQuery = "UPDATE accounts SET balance = ? WHERE id = ?;";
			toStatement = conn.prepareStatement(toQuery);
			toStatement.setBigDecimal(1, newToBalance);
			toStatement.setInt(2, to);
			fromStatement.executeUpdate();
			toStatement.executeUpdate();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static Map<String, String> getUser(String user) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "SELECT first_name, last_name, email, address, phone_number FROM users WHERE email = ?;";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, user);
			ResultSet rs = statement.executeQuery();
			Map<String, String> result = new HashMap<>();
			if(rs.next()) {
				result.put("name", rs.getString("first_name")+ " "+rs.getString("last_name"));
				result.put("address", rs.getString("address"));
				result.put("phone", rs.getString("phone_number"));
			}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean updateNumber(String user, String number) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "UPDATE users SET phone_number = ? WHERE email = ?;";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, number);
			statement.setString(2, user);
			statement.executeUpdate();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updateAddress(String user, String address) {
		try (Connection conn = DriverManager.getConnection(System.getenv("psql_url"), System.getenv("psql_role"), System.getenv("psql_pass"))){
			String query = "UPDATE users SET address = ? WHERE email = ?;";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, address);
			statement.setString(2, user);
			statement.executeUpdate();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
