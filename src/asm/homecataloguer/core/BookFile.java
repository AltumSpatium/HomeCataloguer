package asm.homecataloguer.core;

import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.annotations.CreateView;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;
import asm.homecataloguer.util.BookUtil;
import asm.homecataloguer.util.BookUtil.TextRow;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.w3c.dom.Document;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.util.ResourceUtil;

@CatalogFileType(contentType = ContentType.BOOK, supportedExt = {"epub"})
public class BookFile extends CatalogFile
{
	private TextFlow tfBook;
	
	private ArrayList<ArrayList<Text>> pages = new ArrayList<>();
	private int currentPage = 0;
	
	public BookFile(CatalogItem catalogItem)
	{
		super(catalogItem);
	}
	
	private Book loadBook()
	{
		try
		{
			EpubReader epubReader = new EpubReader();
			return epubReader.readEpub(new ByteArrayInputStream(getData()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Text processRow(TextRow row)
	{
		Text text = row.getText();
		text.setFont(row.getFont());
		
		return text;
	}
	
	private ArrayList<ArrayList<Text>> createPages(Book book)
	{
		ArrayList<ArrayList<Text>> pages = new ArrayList<>();
		int pageSize = 700;
		
		ArrayList<Text> page = new ArrayList<>();
		int symbolsCount = 0;
		for (Resource res : book.getContents())
		{
			try
			{
				Document bookDoc = ResourceUtil.getAsDocument(res);
				ArrayList<TextRow> rows = BookUtil.parseEpubBookDocument(bookDoc);
				for (int i = 0; i < rows.size(); i++)
				{
					Text text = processRow(rows.get(i));
					String rowText = text.getText();
					
					int lineSymbolsCount = rowText.equals("\n") ? 105 : rowText.length();
					
					symbolsCount += lineSymbolsCount;
					if (symbolsCount > pageSize)
					{
						int excessLength = symbolsCount - pageSize;
						String validText = rowText.substring(0, excessLength) + '-';
						String excessText = rowText.substring(excessLength);
						text.setText(validText);
						page.add(text);
						
						Text nextRow;
						if (i < rows.size() - 1)
							nextRow = processRow(rows.get(++i));
						else
							nextRow = processRow(new TextRow(excessText, text.getFont()));
						
						nextRow.setText(excessText + nextRow.getText());
						pages.add(page);
						page = new ArrayList<>();
						page.add(nextRow);
						symbolsCount = 0;
					}
					else page.add(text);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return pages;
	}
	
	@Override
	@CreateView(className = "BookFile")
	public void show(Object layout)
	{
		Book book = loadBook();
		
		AnchorPane bookLayout = new AnchorPane();
		AnchorPane.setLeftAnchor(bookLayout, 150.0);
		AnchorPane.setRightAnchor(bookLayout, 150.0);
		AnchorPane.setTopAnchor(bookLayout, 70.0);
		AnchorPane.setBottomAnchor(bookLayout, 0.0);
		
		pages = createPages(book);
		ArrayList<Text> page = pages.get(currentPage);
		
		tfBook = new TextFlow(page.toArray(new Node[1]));
		tfBook.setMinWidth(700);
		tfBook.setMaxWidth(700);
		tfBook.setMinHeight(450);
		tfBook.setMaxHeight(450);
		tfBook.setStyle("-fx-background-color : white;"
						+ "-fx-padding: 3px;");
		bookLayout.getChildren().add(tfBook);
		
		Button btnPrev = new Button();
		btnPrev.setText("Prev");
		btnPrev.setPrefSize(80, 40);
		AnchorPane.setLeftAnchor(btnPrev, 100.0);
		AnchorPane.setBottomAnchor(btnPrev, 20.0);
		btnPrev.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (currentPage > 0)
				{
					ArrayList<Text> page = pages.get(--currentPage);
					tfBook.getChildren().clear();
					for (Text line : page) tfBook.getChildren().add(line);
				}
			}
		});
		bookLayout.getChildren().add(btnPrev);
		
		Button btnNext = new Button();
		btnNext.setText("Next");
		btnNext.setPrefSize(80, 40);
		AnchorPane.setRightAnchor(btnNext, 100.0);
		AnchorPane.setBottomAnchor(btnNext, 20.0);
		btnNext.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (currentPage < pages.size() - 1)
				{
					ArrayList<Text> page = pages.get(++currentPage);
					tfBook.getChildren().clear();
					for (Text line : page) tfBook.getChildren().add(line);
				}
			}
		});
		bookLayout.getChildren().add(btnNext);
	
		((AnchorPane) layout).getChildren().add(bookLayout);
	}
}
