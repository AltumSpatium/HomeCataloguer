package asm.homecataloguer;

import java.io.IOException;
import java.util.ArrayList;

import asm.homecataloguer.core.CatalogFile;
import asm.homecataloguer.helpers.CatalogDBHelper;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.User;
import asm.homecataloguer.models.UserRole;
import asm.homecataloguer.views.AuthorizationController;
import asm.homecataloguer.views.CatalogFileController;
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
	private AnchorPane catalogOverview;
	
	private CatalogOverviewController coController;
	
	private User currentUser;
	
	private ObservableList<CatalogFile> catalogFiles = FXCollections.observableArrayList();
	
	public Main()
	{
		currentUser = new User(-1, UserRole.GUEST, "guest", "");
		
		CatalogDBHelper dbHelper = new CatalogDBHelper();
		ArrayList<CatalogItem> catalogItems = dbHelper.loadInfo();
		
		for (CatalogItem item : catalogItems)
		{
			CatalogFile catalogFile = CatalogFile.createCatalogFile(item);
			if (catalogFile != null)
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
		this.primaryStage.setMinWidth(620);
		this.primaryStage.setMinHeight(440);
		
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
			
			catalogOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(catalogOverview);
			
			coController = loader.getController();
			coController.setMainApp(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void openCatalogFile(CatalogFile catalogFile)
	{
		CatalogFileController controller = new CatalogFileController(catalogFile);
		controller.setMainApp(this);
		controller.initialize();
	}
	
	public void updateView()
	{
		System.out.println(currentUser.getUsername());
		coController.updateSignBtn(currentUser.getUserRole() == UserRole.GUEST);
		coController.updateSignLabel();
	}
	
	public void authorizeUser(User currentUser)
	{
		AuthorizationController controller = new AuthorizationController(currentUser);
		controller.setMainApp(this);
		controller.intitialize();
	}
	
	public void exitUser()
	{
		currentUser = new User(-1, UserRole.GUEST, "guest", "");
		updateView();
	}
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}
	
	public BorderPane getRootLayout()
	{
		return rootLayout;
	}
	
	public AnchorPane getCatalogOverview()
	{
		return catalogOverview;
	}
	
	public User getCurrentUser()
	{
		return currentUser;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
