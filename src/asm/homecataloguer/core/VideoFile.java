package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

@CatalogFileType(contentType = ContentType.VIDEO)
public class VideoFile extends CatalogFile
{
	public VideoFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	@Override
	@CreateView(className = "VideoFile")
	public void show(Object layout)
	{		
	}
	
	public void play()
	{
	}
	
	public void pause()
	{		
	}
	
	public void stop()
	{		
	}
}
