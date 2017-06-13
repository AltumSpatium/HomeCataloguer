package asm.homecataloguer.core;

import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

public abstract class CatalogFile
{
	public CatalogItem catalogItem;
	
	public ContentType getContentType()
	{
		return catalogItem.getContentType();
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
	
	public abstract void show();
}
