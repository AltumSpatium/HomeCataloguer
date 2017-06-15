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
	private CatalogItem catalogItem;
	
	public CatalogFile(CatalogItem catalogItem)
	{
		this.catalogItem = catalogItem;
	}
	
	public ContentType getContentType()
	{
		return catalogItem.getContentType();
	}
	
	public String getTitle()
	{
		return catalogItem.getTitle();
	}
	
	public int getSize()
	{
		return catalogItem.getSize();
	}
	
	public byte[] getData()
	{
		return catalogItem.getData();
	}
	
	public void setData(byte[] data)
	{
		this.catalogItem.setData(data);
	}
	
	public Date getUploadDate()
	{
		return catalogItem.getUploadDate();
	}
	
	public int getId()
	{
		return catalogItem.getId();
	}
	
	public int getUserId()
	{
		return catalogItem.getUserId();
	}
	
	public int getViewsCount()
	{
		return catalogItem.getViewsCount();
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
