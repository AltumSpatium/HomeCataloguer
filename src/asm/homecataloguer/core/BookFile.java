package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

@CatalogFileType(contentType = ContentType.BOOK)
public class BookFile extends CatalogFile
{
	public BookFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	@Override
	@CreateView(className = "BookFile")
	public void show(Object layout)
	{
		System.out.println("Creating view for Book file");
	}
}
