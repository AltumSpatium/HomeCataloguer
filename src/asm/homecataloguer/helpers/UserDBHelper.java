package asm.homecataloguer.helpers;

import asm.homecataloguer.models.User;
import asm.homecataloguer.models.UserRole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDBHelper
{
	private final String url = "jdbc:mysql://localhost:3306/homecataloguerdb?useSSL=false";
	private final String user = "root";
	private final String password = "topaz123";
	
	private Connection dbConn;
	
	public User findUserById(int id)
	{
		String query = "SELECT * FROM Users WHERE id=" + id;
		return loadUser(query);
	}
	
	public User findUserByUsername(String username)
	{
		String query = "SELECT * FROM Users WHERE username='" + username + "'";
		return loadUser(query);
	}
	
	public User loadUser(String query)
	{
		User foundUser = null;
		
		try
		{
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while (result.next())
			{
				int userId = result.getInt("id");
				UserRole role = UserRole.values()[result.getInt("role")];
				String username = result.getString("username");
				String password = result.getString("password");
				
				foundUser = new User(userId, role, username, password);
			}
			
			return foundUser;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try { dbConn.close(); } catch (SQLException e) {}
		}
	}

	public boolean saveUser(User newUser)
	{
		try
		{
			String query = "INSERT INTO "
					+ "Users(role, username, password) "
					+ "VALUES (?, ?, ?)";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			PreparedStatement preparedStatement = dbConn.prepareStatement(query);
			preparedStatement.setInt(1, newUser.getUserRole().ordinal());
			preparedStatement.setString(2, newUser.getUsername());
			preparedStatement.setString(3, newUser.getPassword());
			
			preparedStatement.executeUpdate();
			int userId = findUserByUsername(newUser.getUsername()).getId();
			newUser.setId(userId);
			
			return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			return false;
		}
		finally
		{
			try { dbConn.close(); } catch (SQLException e) {}
		}	
	}
}
