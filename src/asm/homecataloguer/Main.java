package asm.homecataloguer;

import java.io.IOException;
import java.util.prefs.Preferences;

import asm.homecataloguer.core.CatalogFile;
import asm.homecataloguer.helpers.CatalogDBHelper;
import asm.homecataloguer.helpers.UserDBHelper;
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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	private Stage primaryStage;
	private BorderPane rootLayout;
	private AnchorPane catalogOverview;
	
	private CatalogOverviewController coController;
	
	private User currentUser;
	
	private ObservableList<CatalogItem> catalogItems = FXCollections.observableArrayList();
	
	public Main()
	{
		loadLastUser();
		
		CatalogDBHelper dbHelper = new CatalogDBHelper();
		for (CatalogItem item : dbHelper.loadInfo())
		{
			if (item != null)
				catalogItems.add(item);
		}
	}
	
	public ObservableList<CatalogItem> getCatalogItems()
	{
		return catalogItems;
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("Home Cataloguer");
		primaryStage.setMinWidth(1015);
		primaryStage.setMaxWidth(1015);
		primaryStage.setMinHeight(640);
		primaryStage.setMaxHeight(640);
		primaryStage.setOnCloseRequest(event -> {
			saveLastUser();
		});
		primaryStage.getIcons().add(new Image("file:resources/favicon.ico"));
		this.primaryStage = primaryStage;
		
		
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
			coController.updateBtns(currentUser.getUserRole() == UserRole.GUEST);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void openCatalogItem(CatalogItem catalogItem)
	{
		CatalogFile catalogFile = CatalogFile.createCatalogFile(catalogItem);
		CatalogFileController controller = new CatalogFileController(catalogFile);
		controller.setMainApp(this);
		controller.initialize();
	}
	
	public void updateView()
	{
		coController.updateBtns(currentUser.getUserRole() == UserRole.GUEST);
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
	
	public void saveLastUser()
	{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		prefs.put("lastUser", currentUser.getUsername());
	}
	
	public void loadLastUser()
	{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String username = prefs.get("lastUser", "guest");
		
		if (username.equals("guest"))
			currentUser = new User(-1, UserRole.GUEST, "guest", "");
		else
		{
			UserDBHelper helper = new UserDBHelper();
			currentUser = helper.findUserByUsername(username);
		}
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
