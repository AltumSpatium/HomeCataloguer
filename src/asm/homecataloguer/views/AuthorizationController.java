package asm.homecataloguer.views;

import asm.homecataloguer.Main;
import asm.homecataloguer.helpers.UserDBHelper;
import asm.homecataloguer.models.User;
import asm.homecataloguer.models.UserRole;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

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
		btnBack.setPrefWidth(75);
		btnBack.setPrefHeight(30);
		btnBack.setLayoutX(10);
		btnBack.setLayoutY(10);
		btnBack.setFont(new Font(18));
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
		labelUsername.setFont(new Font(18));
		AnchorPane.setLeftAnchor(labelUsername, 150.0);
		AnchorPane.setTopAnchor(labelUsername, 250.0);
		layout.getChildren().add(labelUsername);
		
		Label labelPassword = new Label();
		labelPassword.setText("Password: ");
		labelPassword.setFont(new Font(18));
		AnchorPane.setLeftAnchor(labelPassword, 150.0);
		AnchorPane.setTopAnchor(labelPassword, 300.0);
		layout.getChildren().add(labelPassword);
		
		TextField tfUsername = new TextField();
		tfUsername.setFont(new Font(18));
		AnchorPane.setLeftAnchor(tfUsername, 255.0);
		AnchorPane.setRightAnchor(tfUsername, 255.0);
		AnchorPane.setTopAnchor(tfUsername, 247.0);
		layout.getChildren().add(tfUsername);

		PasswordField tfPassword = new PasswordField();
		tfPassword.setFont(new Font(18));
		AnchorPane.setLeftAnchor(tfPassword, 255.0);
		AnchorPane.setRightAnchor(tfPassword, 255.0);
		AnchorPane.setTopAnchor(tfPassword, 297.0);
		layout.getChildren().add(tfPassword);
		
		Button btnRegister = new Button();
		btnRegister.setFont(new Font(18));
		btnRegister.setText("Register");
		btnRegister.setOnMouseClicked((mouseEvent) -> {
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			if (username.isEmpty() || password.isEmpty())
			{
				System.out.println("Username or password fields must not be empty!");
				return;
			} else if (password.length() < 8)
			{
				System.out.println("Password must be at least 8 symbols length!");
				return;				
			}
			
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
		AnchorPane.setLeftAnchor(btnRegister, 325.0);
		AnchorPane.setRightAnchor(btnRegister, 520.0);
		AnchorPane.setTopAnchor(btnRegister, 347.0);
		layout.getChildren().add(btnRegister);

		Button btnSignIn = new Button();
		btnSignIn.setFont(new Font(18));
		btnSignIn.setText("Sign In");
		btnSignIn.setOnMouseClicked((mouseEvent) -> {
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			if (username.isEmpty() || password.isEmpty())
			{
				System.out.println("Username or password fields must not be empty!");
				return;
			} else if (password.length() < 8)
			{
				System.out.println("Password must be at least 8 symbols length!");
				return;				
			}
				
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
		AnchorPane.setLeftAnchor(btnSignIn, 520.0);
		AnchorPane.setRightAnchor(btnSignIn, 325.0);
		AnchorPane.setTopAnchor(btnSignIn, 347.0);
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
