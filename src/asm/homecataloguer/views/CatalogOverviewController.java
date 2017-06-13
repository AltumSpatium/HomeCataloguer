package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.core.CatalogFile;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
				return new ListViewCell();
			}
		});
	}
	
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
		listView.setItems(this.mainApp.getCatalogFiles());
	}
}
