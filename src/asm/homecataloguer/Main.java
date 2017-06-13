package asm.homecataloguer;

import java.io.IOException;
import java.util.ArrayList;

import asm.homecataloguer.core.CatalogFile;
import asm.homecataloguer.helpers.CatalogDBHelper;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.views.CatalogOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private ObservableList<CatalogFile> catalogFiles = FXCollections.observableArrayList();
	
	public Main()
	{
		CatalogDBHelper dbHelper = new CatalogDBHelper();
		ArrayList<CatalogItem> catalogItems = dbHelper.loadAll();
		
		for (CatalogItem item : catalogItems)
		{
			CatalogFile catalogFile = CatalogFile.createCatalogFile(item);
			catalogFiles.add(catalogFile);
		}
	}
	
	public ObservableList<CatalogFile> getCatalogFiles()
	{
		return catalogFiles;
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Home Cataloguer");
		
		initRootLayout();
		showCatalogOverview();
	}
	
	public void initRootLayout()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("views/Main.fxml"));
			
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);			
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showCatalogOverview()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("views/CatalogOverview.fxml"));
			
			AnchorPane catalogOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(catalogOverview);
			
			CatalogOverviewController controller = loader.getController();
			controller.setMainApp(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
