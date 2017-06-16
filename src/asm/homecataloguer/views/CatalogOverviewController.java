package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.core.CatalogFile;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class CatalogOverviewController
{
	@FXML
	private ListView<CatalogFile> listView;
	@FXML
	private TextField textFieldSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnSignIn;
	@FXML
	private Button btnExit;
	@FXML
	private Label signLabel;
	private Main mainApp;
	
	private ObservableList<CatalogFile> catalogFiles;
	
	public CatalogOverviewController() {}
	
	@FXML
	private void initialize()
	{
		btnExit.setVisible(false);
		
		listView.setCellFactory(new Callback<ListView<CatalogFile>, ListCell<CatalogFile>>()
		{
			@Override
			public ListCell<CatalogFile> call(ListView<CatalogFile> param)
			{
				ListViewCell cell = new ListViewCell();
				
				cell.setOnMouseClicked((mouseEvent) -> {
					if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
					{
						if (mouseEvent.getClickCount() == 2)
						{
							CatalogFile catalogFile = cell.getItem();
							mainApp.openCatalogFile(catalogFile);
						}
					}
				});
				
				return cell;
			}
		});
		
		textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.isEmpty()) listView.setItems(catalogFiles);
		});
		
		btnSearch.setOnMouseClicked((mouseEvent) -> {
			String searchText = textFieldSearch.getText().toLowerCase();
			ObservableList<CatalogFile> foundFiles = catalogFiles.filtered(new Predicate<CatalogFile>() {
				@Override
				public boolean test(CatalogFile t) {
					return t.getTitle().toLowerCase().contains(searchText) ||
						t.getUploadDate().toString().toLowerCase().contains(searchText) ||
						t.getContentType().toString().toLowerCase().contains(searchText);
				}
			});
			
			listView.setItems(foundFiles);
		});
		
		btnSignIn.setOnMouseClicked((mouseEvent) -> {
			mainApp.authorizeUser(this.mainApp.getCurrentUser());
		});
		
		btnExit.setOnMouseClicked((mouseEvent) -> {
			mainApp.exitUser();
		});
	}
	
	public void updateSignLabel()
	{
		signLabel.setText("You're signed in as " + this.mainApp.getCurrentUser().getUsername());
	}
	
	public void updateSignBtn(boolean visible)
	{
		btnSignIn.setVisible(visible);
		btnExit.setVisible(!visible);
	}
	
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
		catalogFiles = this.mainApp.getCatalogFiles();
		
		updateSignLabel();
		listView.setItems(catalogFiles);
	}
}
