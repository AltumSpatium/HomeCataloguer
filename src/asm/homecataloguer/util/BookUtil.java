package asm.homecataloguer.util;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BookUtil {
	public static class TextRow
	{
		private final int defaultFontSize = 12;
		private Text text;
		private Font font;
		
		public Text getText()
		{
			return text;
		}
		
		public void setText(Text text)
		{
			this.text = text;
		}
		
		public void setText(String text)
		{
			this.text = new Text(text);
		}
		
		public Font getFont()
		{
			return font;
		}
		
		public void setFont(Font font)
		{
			this.font = font;
		}
		
		public TextRow() {}
		
		public TextRow(String text)
		{
			setText(text);
			setFont(new Font(defaultFontSize));
		}
		
		public TextRow(String text, Font font)
		{
			setText(text);
			setFont(font);
		}
	}
	
	private static TextRow getRow(Node node)
	{
		switch (node.getNodeName())
		{
		case "p":
			return new TextRow("\t" + node.getTextContent() + "\n", new Font(13));
		case "h1":
			return new TextRow("\n" + node.getTextContent() + "\n", new Font(20));
		case "h2":
			return new TextRow("\n" + node.getTextContent() + "\n", new Font(18));
		case "h5":
			return new TextRow("\n" + node.getTextContent() + "\n", new Font(11));
		default:
			return new TextRow("\t" + node.getTextContent() + " ");
		}
	}
	
	private static ArrayList<TextRow> loadContent(Node content)
	{
		ArrayList<TextRow> rows = new ArrayList<>();
		NodeList paragraphs = content.getChildNodes();
		
		for (int i = 0; i < paragraphs.getLength(); i++)
			rows.add(getRow(paragraphs.item(i)));
		
		return rows;
	}
	
	public static ArrayList<TextRow> parseEpubBookDocument(Document bookDoc)
	{
		ArrayList<TextRow> rows = new ArrayList<>();
		Element rootElement = bookDoc.getDocumentElement();
		Node body = rootElement.getLastChild();
		
		Node content = body.getLastChild();
		rows.addAll(loadContent(content));
		
		return rows;
	}
}
