package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

@CatalogFileType(contentType = ContentType.DOCUMENT, supportedExt = {"docx"})
public class DocumentFile extends CatalogFile
{
	public DocumentFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	private String loadDocument()
	{
		String documentText = "";
		try {
			WordprocessingMLPackage wordMLPackage = Docx4J.load(
					new ByteArrayInputStream(getData()));
			MainDocumentPart mainPart = wordMLPackage.getMainDocumentPart();
			for (Object obj : mainPart.getContent())
				documentText += obj.toString() + "\n";
			return documentText;
		} catch (Docx4JException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	@CreateView(className = "DocumentFile")
	public void show(Object layout)
	{
		String docText = loadDocument();
		
		AnchorPane docLayout = new AnchorPane();
		AnchorPane.setLeftAnchor(docLayout, 150.0);
		AnchorPane.setRightAnchor(docLayout, 150.0);
		AnchorPane.setTopAnchor(docLayout, 70.0);
		AnchorPane.setBottomAnchor(docLayout, 0.0);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMinWidth(700);
		scrollPane.setMaxWidth(700);
		scrollPane.setMinHeight(500);
		scrollPane.setMaxHeight(500);
		scrollPane.setStyle("-fx-background : white; -fx-padding: 3px;");
		docLayout.getChildren().add(scrollPane);
		
		Text text = new Text(docText);
		text.setFont(new Font(14));
		scrollPane.setContent(text);
		
		((AnchorPane) layout).getChildren().add(docLayout);
	}
}
