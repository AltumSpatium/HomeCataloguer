package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

import java.util.Date;
import java.util.Set;

import org.reflections.*;
import org.reflections.scanners.SubTypesScanner;

public abstract class CatalogFile
{
	private int id;
	private int userId;
	private String title;
	private ContentType contentType;
	private int size;
	private Date uploadDate;
	private int viewsCount;
	private byte[] data;
	
	public CatalogFile(CatalogItem catalogItem)
	{
		this.id = catalogItem.getId();
		this.userId = catalogItem.getUserId();
		this.title = catalogItem.getTitle();
		this.contentType = catalogItem.getContentType();
		this.size = catalogItem.getSize();
		this.uploadDate = catalogItem.getUploadDate();
	}
	
	public ContentType getContentType()
	{
		return contentType;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public byte[] getData()
	{
		return data;
	}
	
	public void setData(byte[] data)
	{
		this.data = data;
	}
	
	public Date getUploadDate()
	{
		return uploadDate;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public int getViewsCount()
	{
		return viewsCount;
	}
	
	public static CatalogFile createCatalogFile(CatalogItem item)
	{
		Reflections reflections = new Reflections("asm.homecataloguer.core", new SubTypesScanner(false));
		Set<Class<? extends CatalogFile>> catalogFileClasses = reflections.getSubTypesOf(CatalogFile.class);
		
		for (Class<? extends CatalogFile> c : catalogFileClasses)
		{
			if (c.isAnnotationPresent(CatalogFileType.class) &&
					c.getAnnotation(CatalogFileType.class).contentType().equals(item.getContentType()))
			{
				try
				{
					return c.getConstructor(CatalogItem.class).newInstance(new Object[] { item });
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public abstract void show(Object layout);
}
