package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

@CatalogFileType(contentType = ContentType.DOCUMENT)
public class DocumentFile extends CatalogFile
{
	public DocumentFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	@Override
	@CreateView(className = "DocumentFile")
	public void show(Object layout)
	{
		
	}
}
