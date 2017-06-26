package asm.homecataloguer.helpers;

import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;
import asm.homecataloguer.models.User;
import asm.homecataloguer.models.UserRole;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;

public class CatalogDBHelper
{
	private final String url = "jdbc:mysql://localhost:3306/homecataloguerdb?useSSL=false";
	private final String user = "root";
	private final String password = "topaz123";
	
	private Connection dbConn;

	public ArrayList<CatalogItem> loadInfo()
	{
		ArrayList<CatalogItem> catalog = new ArrayList<CatalogItem>();
		
		try
		{
			String query = "SELECT * FROM Catalog ORDER BY viewsCount DESC;";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			loadCursorData(catalog, result);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try { dbConn.close(); } catch (SQLException e) {}
		}
		
		return catalog;
	}
	
	public byte[] loadData(int id)
	{
		byte[] data = null;
		
		try
		{
			String query = "SELECT data FROM Catalog WHERE id=" + id;
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while (result.next())
			{
				Blob dataBlob = result.getBlob("data"); 
				data = dataBlob.getBytes(1, (int)dataBlob.length());
				dataBlob.free();
			}
			
			return data;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try { dbConn.close(); } catch (SQLException e) {}
		}
	}
	
	private boolean saveItem(CatalogItem catalogItem)
	{
		try
		{
			String query = "INSERT INTO "
					+ "Catalog(userId, size, viewsCount, contentType, title, uploadDate, data) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			PreparedStatement preparedStatement = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, catalogItem.getUserId());
			preparedStatement.setInt(2, catalogItem.getSize());
			preparedStatement.setInt(3, catalogItem.getViewsCount());
			preparedStatement.setInt(4, catalogItem.getContentType().ordinal());
			preparedStatement.setString(5, catalogItem.getTitle());
			preparedStatement.setDate(6, new java.sql.Date(catalogItem.getUploadDate().getTime()));
			preparedStatement.setBlob(7, new javax.sql.rowset.serial.SerialBlob(catalogItem.getData()));
			preparedStatement.executeUpdate();
			
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next())
				catalogItem.setId(generatedKeys.getInt(1));
			
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
	
	private String getDayBeforeDate(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR,-1);
		Date oneDayBefore = calendar.getTime();
		
		return new SimpleDateFormat("yyyy-MM-dd").format(oneDayBefore);	
	}
	
	private int getUserUploadSize(User currUser)
	{
		int uploadSize = 0;
		
		try
		{
			String currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String dayBeforeDate = getDayBeforeDate(new Date());
			
			String query = "SELECT SUM(size) as sum FROM Catalog WHERE "
					+ "userId=" + currUser.getId() + " AND "
					+ "uploadDate BETWEEN '" + dayBeforeDate + "' AND '" + currDate + "'";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result.next())
				uploadSize = result.getInt("sum");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try { dbConn.close(); } catch (SQLException e) {}
		}
		
		return uploadSize;
	}
	
	public boolean saveItem(CatalogItem catalogItem, User user)
	{
		int dailyQuota = 10485760;
		
		if (user.getUserRole().equals(UserRole.USER))
		{
			int uploadSize = getUserUploadSize(user);
			if (uploadSize > dailyQuota || (uploadSize + catalogItem.getSize()) > dailyQuota)
				return false;
		}
		
		return saveItem(catalogItem);
	}
	
	private boolean deleteItem(CatalogItem catalogItem)
	{
		try
		{
			String query = "DELETE FROM Catalog WHERE id=" + catalogItem.getId();
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			statement.executeUpdate(query);
			
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
	
	public boolean deleteItem(CatalogItem catalogItem, User user)
	{
		if (user.getUserRole().equals(UserRole.GUEST))
			return false;
		
		return deleteItem(catalogItem);
	}
	
	public boolean increaseViewsCount(int id, int oldViewsCount)
	{
		try
		{
			String query = "UPDATE Catalog "
					+ "SET viewsCount=" + (oldViewsCount + 1) + " " 
					+ "WHERE id=" + id;
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			statement.executeQuery(query);
			
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
		
	public void loadCursorData(ArrayList<CatalogItem> catalog, ResultSet result)
	{
		try
		{
			while (result.next())
			{
				int id = result.getInt("id");
				int userId = result.getInt("userId");
				int size = result.getInt("size");
				int viewsCount = result.getInt("viewsCount");
				ContentType contentType = ContentType.values()[result.getInt("contentType")];
				String title = result.getString("title");
				Date uploadDate = result.getDate("uploadDate");
			
				catalog.add(new CatalogItem(id, userId, size, viewsCount,
					contentType, title, uploadDate, null));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
