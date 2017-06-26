package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.annotations.CatalogFileType;
import asm.homecataloguer.core.CatalogFile;
import asm.homecataloguer.helpers.CatalogDBHelper;
import asm.homecataloguer.models.CatalogItem;
import asm.homecataloguer.models.ContentType;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
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
		
		listView.setOnKeyPressed((keyEvent) -> {
			if (keyEvent.getCode().equals(KeyCode.DELETE))
			{
				CatalogDBHelper helper = new CatalogDBHelper();
				CatalogItem catalogItem = listView.getSelectionModel().getSelectedItem();
				if (helper.deleteItem(catalogItem, mainApp.getCurrentUser()))
					catalogItems.remove(catalogItem);
			}
		});
		
		textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.isEmpty()) listView.setItems(catalogItems);
		});
		textFieldSearch.setOnKeyPressed((keyEvent) -> {
			if (keyEvent.getCode().equals(KeyCode.ENTER))
			{
				String searchText = textFieldSearch.getText().toLowerCase();
				search(searchText);
			}
		});
		
		btnAdd.setOnMouseClicked((mouseEvent) -> {
			FileChooser fileChooser = new FileChooser();
			File newFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
			if (newFile != null) addFile(newFile);
		});
		
		btnSearch.setOnMouseClicked((mouseEvent) -> {
			String searchText = textFieldSearch.getText().toLowerCase();
			search(searchText);
		});
		
		btnSignIn.setOnMouseClicked((mouseEvent) -> {
			mainApp.authorizeUser(this.mainApp.getCurrentUser());
		});
		
		btnExit.setOnMouseClicked((mouseEvent) -> {
			mainApp.exitUser();
		});
	}
	
	private void addFile(File newFile)
	{
		CatalogDBHelper helper = new CatalogDBHelper();
		CatalogItem catalogItem = new CatalogItem();
		catalogItem.setTitle(getFileName(newFile));
		catalogItem.setUserId(mainApp.getCurrentUser().getId());
		catalogItem.setSize((int)newFile.length());
		catalogItem.setContentType(getFileType(newFile));
		try
		{
			byte[] fileData = IOUtils.toByteArray(new FileInputStream(newFile));
			catalogItem.setData(fileData);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (helper.saveItem(catalogItem, mainApp.getCurrentUser()))
			catalogItems.add(catalogItem);
		else new Alert(Alert.AlertType.ERROR, "You exceeded the daily quota!").show();
	}
	
	private String getFileName(File file)
	{
		return FilenameUtils.removeExtension(file.getName());
	}
	
	private ContentType getFileType(File file)
	{
		String ext = FilenameUtils.getExtension(file.getName());
		
		Reflections reflections = new Reflections("asm.homecataloguer.core", new SubTypesScanner(false));
		Set<Class<? extends CatalogFile>> catalogFileClasses = reflections.getSubTypesOf(CatalogFile.class);
		
		for (Class<? extends CatalogFile> c : catalogFileClasses)
		{
			if (c.isAnnotationPresent(CatalogFileType.class) &&
					Arrays.asList(c.getAnnotation(CatalogFileType.class).supportedExt()).contains(ext))
			{
				try
				{
					return c.getAnnotation(CatalogFileType.class).contentType();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return ContentType.UNKNOWN;
	}
	
	private void search(String searchText)
	{
		ObservableList<CatalogItem> foundFiles = catalogItems.filtered(new Predicate<CatalogItem>() {
			@Override
			public boolean test(CatalogItem t) {
				return t.getTitle().toLowerCase().contains(searchText) ||
					t.getUploadDate().toString().toLowerCase().contains(searchText) ||
					t.getContentType().toString().toLowerCase().contains(searchText);
			}
		});
		
		listView.setItems(foundFiles);
	}
	
	public void updateSignLabel()
	{
		signLabel.setText("You're signed in as " +
				mainApp.getCurrentUser().getUsername());
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
