package asm.homecataloguer.views;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import asm.homecataloguer.models.CatalogItem;

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
    		return title.substring(0, maxLength - 3) + "...";
    	return title;
    }
    
    private String formatDate(Date date)
    {
    	return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public void setInfo(CatalogItem catalogItem)
    {
    	String title = formatTitle(catalogItem.getTitle(), 18);
    	String size = "Size: " + formatSize(catalogItem.getSize());
    	String date = "Upload date: " + formatDate(catalogItem.getUploadDate());
    	String type = "Type: " + catalogItem.getContentType();
    	
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
