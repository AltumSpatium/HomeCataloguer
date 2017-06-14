package asm.homecataloguer.views;

import asm.homecataloguer.CreateView;
import asm.homecataloguer.Main;
import asm.homecataloguer.core.CatalogFile;
import asm.homecataloguer.helpers.CatalogDBHelper;

import java.lang.reflect.Method;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CatalogFileController
{
	private CatalogFile catalogFile;
	private Main mainApp;
	
	public CatalogFileController(CatalogFile catalogFile)
	{
		CatalogDBHelper helper = new CatalogDBHelper();
		byte[] data = helper.loadData(catalogFile.getId());
		catalogFile.setData(data);
		this.catalogFile = catalogFile;
	}
	
	public void initialize()
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
				mainApp.getRootLayout().setCenter(mainApp.getCatalogOverview());
			}
		});
		cfLayout.getChildren().add(btnBack);
		
		mainApp.getRootLayout().setCenter(cfLayout);
	}
	
	public void createCatalogFileView(AnchorPane layout, CatalogFile catalogFile)
	{
		createTitle(layout, catalogFile);
		
		Method[] methods = catalogFile.getClass().getDeclaredMethods();
		
		for (Method m : methods)
		{
			if (m.isAnnotationPresent(CreateView.class))
			{
				try
				{
					m.invoke(catalogFile, layout);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
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
		
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
	}
}
