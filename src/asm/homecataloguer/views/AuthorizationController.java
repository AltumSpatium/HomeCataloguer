package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.helpers.UserDBHelper;
import asm.homecataloguer.models.User;
import asm.homecataloguer.models.UserRole;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AuthorizationController
{
	private Main mainApp;
	private User currentUser;
	
	public AuthorizationController(User user)
	{
		currentUser = user;
	}
	
	public void intitialize()
	{
		AnchorPane authLayout = new AnchorPane();
		
		createAuthView(authLayout);
		
		Button btnBack = new Button();
		btnBack.setText("Back");
		btnBack.setOnMouseClicked((mouseEvent) -> {
			mainApp.getRootLayout().setCenter(mainApp.getCatalogOverview());
		});
		authLayout.getChildren().add(btnBack);
		
		mainApp.getRootLayout().setCenter(authLayout);
	}
	
	public void createAuthView(AnchorPane layout)
	{		
		Label labelUsername = new Label();
		labelUsername.setText("Username: ");
		AnchorPane.setLeftAnchor(labelUsername, 150.0);
		AnchorPane.setTopAnchor(labelUsername, 130.0);
		layout.getChildren().add(labelUsername);
		
		Label labelPassword = new Label();
		labelPassword.setText("Password: ");
		AnchorPane.setLeftAnchor(labelPassword, 150.0);
		AnchorPane.setTopAnchor(labelPassword, 160.0);
		layout.getChildren().add(labelPassword);
		
		TextField tfUsername = new TextField();
		AnchorPane.setLeftAnchor(tfUsername, 215.0);
		AnchorPane.setRightAnchor(tfUsername, 200.0);
		AnchorPane.setTopAnchor(tfUsername, 127.0);
		layout.getChildren().add(tfUsername);

		TextField tfPassword = new TextField();
		AnchorPane.setLeftAnchor(tfPassword, 215.0);
		AnchorPane.setRightAnchor(tfPassword, 200.0);
		AnchorPane.setTopAnchor(tfPassword, 157.0);
		layout.getChildren().add(tfPassword);
		
		Button btnRegister = new Button();
		btnRegister.setText("Register");
		btnRegister.setOnMouseClicked((mouseEvent) -> {
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			if (registerUser(username, password))
			{
				System.out.println("Registered");
				mainApp.updateView();
				mainApp.getRootLayout().setCenter(mainApp.getCatalogOverview());
			}
			else
			{
				// Change to dialog windows
				System.out.println("This username is already taken!");
			}
		});
		AnchorPane.setLeftAnchor(btnRegister, 245.0);
		AnchorPane.setTopAnchor(btnRegister, 187.0);
		layout.getChildren().add(btnRegister);

		Button btnSignIn = new Button();
		btnSignIn.setText("Sign In");
		btnSignIn.setOnMouseClicked((mouseEvent) -> {
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			if (signInUser(username, password))
			{
				System.out.println("Signed In");
				mainApp.updateView();
				mainApp.getRootLayout().setCenter(mainApp.getCatalogOverview());				
			}
			else
			{
				// Change to dialog windows
				System.out.println("Incorrect username or password!");
			}
		});
		AnchorPane.setLeftAnchor(btnSignIn, 315.0);
		AnchorPane.setRightAnchor(btnSignIn, 225.0);
		AnchorPane.setTopAnchor(btnSignIn, 187.0);
		layout.getChildren().add(btnSignIn);
	}
	
	public boolean registerUser(String username, String password)
	{
		UserDBHelper helper = new UserDBHelper();
		if (helper.findUserByUsername(username) != null) return false;
		
		currentUser.setUsername(username);
		currentUser.setPassword(password);
		currentUser.setUserRole(UserRole.USER);
		helper.saveUser(currentUser);	
		return true;
	}
	
	public boolean signInUser(String username, String password)
	{
		UserDBHelper helper = new UserDBHelper();
		User foundUser = helper.findUserByUsername(username);
		if (foundUser == null ||
				!foundUser.getPassword().equals(password)) return false;
		
		currentUser.setId(foundUser.getId());
		currentUser.setUsername(foundUser.getUsername());
		currentUser.setPassword(foundUser.getPassword());
		currentUser.setUserRole(foundUser.getUserRole());
		return true;
	}

	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
	}

}
