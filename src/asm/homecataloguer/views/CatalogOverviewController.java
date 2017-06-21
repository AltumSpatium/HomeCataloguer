package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.models.CatalogItem;

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
	private ListView<CatalogItem> listView;
	@FXML
	private TextField textFieldSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnSignIn;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnAdd;
	@FXML
	private Label signLabel;
	private Main mainApp;
	
	private ObservableList<CatalogItem> catalogItems;
	
	public CatalogOverviewController() {}
	
	@FXML
	private void initialize()
	{
		listView.setCellFactory(new Callback<ListView<CatalogItem>, ListCell<CatalogItem>>()
		{
			@Override
			public ListCell<CatalogItem> call(ListView<CatalogItem> param)
			{
				ListViewCell cell = new ListViewCell();
				
				cell.setOnMouseClicked((mouseEvent) -> {
					if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
					{
						if (mouseEvent.getClickCount() == 2)
						{
							CatalogItem catalogItem = cell.getItem();
							mainApp.openCatalogItem(catalogItem);
						}
					}
				});
				
				return cell;
			}
		});
		
		textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.isEmpty()) listView.setItems(catalogItems);
		});
		
		btnSearch.setOnMouseClicked((mouseEvent) -> {
			String searchText = textFieldSearch.getText().toLowerCase();
			ObservableList<CatalogItem> foundFiles = catalogItems.filtered(new Predicate<CatalogItem>() {
				@Override
				public boolean test(CatalogItem t) {
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
	
	public void updateBtns(boolean isGuest)
	{
		btnSignIn.setVisible(isGuest);
		btnExit.setVisible(!isGuest);
		btnAdd.setVisible(!isGuest);
	}
	
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
		catalogItems = this.mainApp.getCatalogItems();
		
		updateSignLabel();
		listView.setItems(catalogItems);
	}
}
