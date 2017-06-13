package asm.homecataloguer.helpers;

import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;
import asm.homecataloguer.models.User;

import java.util.ArrayList;
import java.util.Date;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class CatalogDBHelper
{
	private final String url = "jdbc:mysql://localhost:3306/homecataloguerdb";
	private final String user = "root";
	private final String password = "topaz123";
	
	private Connection dbConn;

	public ArrayList<CatalogItem> loadAll()
	{
		ArrayList<CatalogItem> catalog = new ArrayList<CatalogItem>();
		
		try
		{
			String query = "SELECT * FROM Catalog ORDER BY viewsCount DESC;";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			Statement statement = dbConn.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while (result.next())
			{
				int id = result.getInt("id");
				int userId = result.getInt("userId");
				int size = result.getInt("size");
				int viewsCount = result.getInt("viewsCount");
				ContentType contentType = ContentType.values()[result.getInt("contentType")];
				String title = result.getString("title");
				Date uploadDate = result.getDate("uploadDate");
				
				Blob dataBlob = result.getBlob("data"); 
				byte[] data = dataBlob.getBytes(1, (int)dataBlob.length());
				dataBlob.free();
				
				catalog.add(new CatalogItem(id, userId, size, viewsCount,
						contentType, title, uploadDate, data));
			}
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
	
	private boolean saveItem(CatalogItem catalogItem)
	{
		try
		{
			String query = "INSERT INTO "
					+ "Catalog(userId, size, viewsCount, contentType, title, uploadDate, data) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
			PreparedStatement preparedStatement = dbConn.prepareStatement(query);
			preparedStatement.setInt(1, catalogItem.getUserId());
			preparedStatement.setInt(2, catalogItem.getSize());
			preparedStatement.setInt(3, catalogItem.getViewsCount());
			preparedStatement.setInt(4, catalogItem.getContentType().ordinal());
			preparedStatement.setString(5, catalogItem.getTitle());
			preparedStatement.setDate(6, new java.sql.Date(catalogItem.getUploadDate().getTime()));
			preparedStatement.setBlob(7, new javax.sql.rowset.serial.SerialBlob(catalogItem.getData()));
			preparedStatement.executeUpdate();
			
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
	
	public boolean saveItem(CatalogItem catalogItem, User user)
	{
		// Add role checking
		return saveItem(catalogItem);
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
}
