package asm.homecataloguer.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;

import asm.homecataloguer.core.CatalogFile;

public class Data
{
    @FXML
    private HBox hBox;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelSize;
    @FXML
    private Label labelDate;
    @FXML
    private Label labelType;

    public Data()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ListCellItem.fxml"));
        fxmlLoader.setController(this);
        
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private String formatSize(int size)
    {
    	if (size < 1024)
    		return size + " B";
    	else if (size < 1024 * 1024)
    		return String.format("%(.2f", ((double) size) / 1024) + " KB";
    	else if (size < 1024 * 1024 * 1024)
    		return String.format("%(.2f", ((double) size) / (1024 * 1024)) + " MB";
    	return String.format("%(.2f", ((double) size) / (1024 * 1024 * 1024)) + " GB";
    }
    
    private String formatTitle(String title, int maxLength)
    {
    	if (title.length() > maxLength)
    		return title.substring(0, 15) + "...";
    	return title;
    }

    public void setInfo(CatalogFile catalogFile)
    {
    	String title = formatTitle(catalogFile.getTitle(), 18);
    	String size = "Size: " + formatSize(catalogFile.getSize());
    	String date = "Upload date: " + catalogFile.getUploadDate();
    	String type = "Type: " + catalogFile.getContentType();
    	
        labelTitle.setText(title);
        labelSize.setText(size);
        labelDate.setText(date);
        labelType.setText(type);
    }

    public HBox getBox()
    {
        return hBox;
    }
}
