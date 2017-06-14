package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.core.CatalogFile;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class CatalogOverviewController
{
	@FXML
	private ListView<CatalogFile> listView;
	private Main mainApp;
	
	public CatalogOverviewController() {}
	
	@FXML
	private void initialize()
	{
		listView.setCellFactory(new Callback<ListView<CatalogFile>, ListCell<CatalogFile>>()
		{
			@Override
			public ListCell<CatalogFile> call(ListView<CatalogFile> param)
			{
				ListViewCell cell = new ListViewCell();
				
				cell.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent mouseEvent)
					{
						if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
						{
							if (mouseEvent.getClickCount() == 2)
							{
								CatalogFile catalogFile = cell.getItem();
								mainApp.openCatalogFile(catalogFile);
							}
						}
					}
				});
				
				return cell;
			}
		});
	}
	
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
		listView.setItems(this.mainApp.getCatalogFiles());
	}
}
