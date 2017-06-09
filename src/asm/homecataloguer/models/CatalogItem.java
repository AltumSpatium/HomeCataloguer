package asm.homecataloguer.models;

import java.util.Date;

public class CatalogItem
{
	private int id;
	private int userId;
	private int size;
	private int viewsCount;
	private ContentType contentType;
	private String title;
	private Date uploadDate;
	private byte[] data;
	
	public CatalogItem() {
		size = 0;
		viewsCount = 0;
		contentType = ContentType.DOCUMENT;
		title = "";
		uploadDate = new Date();
	}
	
	public CatalogItem(int id, int userId, int size, int viewsCount,
			ContentType contentType, String title, Date uploadDate, byte[] data)
	{
		this.id = id;
		this.userId = userId;
		this.size = size;
		this.viewsCount = viewsCount;
		this.contentType = contentType;
		this.title = title;
		this.uploadDate = uploadDate;
		this.data = data;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	public int getViewsCount()
	{
		return viewsCount;
	}
	
	public void setViewsCount(int viewsCount)
	{
		this.viewsCount = viewsCount;
	}
	
	public ContentType getContentType()
	{
		return contentType;
	}
	
	public void setContentType(ContentType contentType)
	{
		this.contentType = contentType;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public Date getUploadDate()
	{
		return uploadDate;
	}
	
	public void setUploadDate(Date uploadDate)
	{
		this.uploadDate = uploadDate;
	}
	
	public byte[] getData()
	{
		return data;
	}
	
	public void setData(byte[] data)
	{
		this.data = data;
	}
}
