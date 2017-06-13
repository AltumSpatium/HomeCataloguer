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

    public void setInfo(CatalogFile catalogFile)
    {
    	String title = catalogFile.catalogItem.getTitle();
    	int size = catalogFile.catalogItem.getSize();
    	String sizeText = "Size: " + (size > 1024 ? (size / 1024 + " KB") : (size + " B"));
    	String date = "Upload date: " + catalogFile.catalogItem.getUploadDate();
    	String type = "Type: " + catalogFile.catalogItem.getContentType();
    	
        labelTitle.setText(title);
        labelSize.setText(sizeText);
        labelDate.setText(date);
        labelType.setText(type);
    }

    public HBox getBox()
    {
        return hBox;
    }
}
