package asm.homecataloguer;

import java.io.IOException;
import java.util.ArrayList;

import asm.homecataloguer.core.AudioFile;
import asm.homecataloguer.core.BookFile;
import asm.homecataloguer.core.CatalogFile;
import asm.homecataloguer.core.DocumentFile;
import asm.homecataloguer.core.VideoFile;
import asm.homecataloguer.helpers.CatalogDBHelper;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.views.CatalogOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application
{
	private Stage primaryStage;
	private BorderPane rootLayout;
	private AnchorPane catalogOverview; 
	
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
			
			CatalogOverviewController controller = loader.getController();
			controller.setMainApp(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void openCatalogFile(CatalogFile catalogFile)
	{
		AnchorPane cfLayout = new AnchorPane();
		
		createCatalogFileView(cfLayout, catalogFile);
		
		Button btnBack = new Button();
		btnBack.setText("Back");
		btnBack.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				rootLayout.setCenter(catalogOverview);
			}
		});
		cfLayout.getChildren().add(btnBack);
		
		rootLayout.setCenter(cfLayout);
	}
	
	public void createCatalogFileView(AnchorPane layout, CatalogFile catalogFile)
	{
		createTitle(layout, catalogFile);
		
		switch(catalogFile.getContentType())
		{
		case AUDIO:
			AudioFile audioFile = (AudioFile) catalogFile;
			createAudioView(layout, audioFile);
			break;
		case VIDEO:
			VideoFile videoFile = (VideoFile) catalogFile;
			createVideoView(layout, videoFile);
			break;
		case BOOK:
			BookFile bookFile = (BookFile) catalogFile;
			createBookView(layout, bookFile);
			break;
		case DOCUMENT:
			DocumentFile documentFile = (DocumentFile) catalogFile;
			createDocumentView(layout, documentFile);
			break;			
		}
	}
	
	public void createTitle(AnchorPane layout, CatalogFile catalogFile)
	{
		Label label = new Label();
		String title = catalogFile.getTitle();
		
		label.setText(title);
		label.setTranslateY(5);
		label.setFont(new Font(20));
		label.setAlignment(Pos.CENTER);
		label.setTextFill(Color.GREEN);
		
		AnchorPane.setLeftAnchor(label, 0.0);
		AnchorPane.setRightAnchor(label, 0.0);
		layout.getChildren().add(label);
	}
	
	public void createAudioView(AnchorPane layout, AudioFile audioFile)
	{
		AnchorPane audioLayout = new AnchorPane();
		audioLayout.setMinWidth(200);
		audioLayout.setMaxWidth(200);
		AnchorPane.setLeftAnchor(audioLayout, 0.0);
		AnchorPane.setRightAnchor(audioLayout, 0.0);
		AnchorPane.setBottomAnchor(audioLayout, 50.0);
		
		Button btnPlay = new Button();
		btnPlay.setText("Play");
		btnPlay.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				audioFile.play();
			}
		});
		btnPlay.setAlignment(Pos.BOTTOM_LEFT);
		AnchorPane.setLeftAnchor(btnPlay, 250.0);

		Button btnStop = new Button();
		btnStop.setText("Stop");
		btnStop.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				audioFile.stop();
			}
		});
		btnStop.setAlignment(Pos.BOTTOM_RIGHT);
		AnchorPane.setRightAnchor(btnStop, 250.0);
		
		audioLayout.getChildren().add(btnPlay);
		audioLayout.getChildren().add(btnStop);
		layout.getChildren().add(audioLayout);
	}
	
	public void createVideoView(AnchorPane layout, VideoFile videoFile)
	{
		
	}
	
	public void createBookView(AnchorPane layout, BookFile bookFile)
	{
		
	}
	
	public void createDocumentView(AnchorPane layout, DocumentFile documentFile)
	{
		
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
