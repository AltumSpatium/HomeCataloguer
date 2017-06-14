package asm.homecataloguer.core;

import java.util.Date;

import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

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
		switch(item.getContentType())
		{
		case VIDEO:
			return new VideoFile(item);
		case AUDIO:
			return new AudioFile(item);
		case BOOK:
			return new BookFile(item);
		case DOCUMENT:
			return new DocumentFile(item);
		default:
			return null;
		}
	}
	
	public abstract void show(Object layout);
}
