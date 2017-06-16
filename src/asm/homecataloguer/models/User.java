package asm.homecataloguer.models;

public class User
{
	private int id;
	private UserRole role;
	private String username;
	private String password;
	
	public User() {}
	
	public User(int id, UserRole role, String username, String password)
	{
		this.id = id;
		this.role = role;
		this.username = username;
		this.password = password;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public UserRole getUserRole()
	{
		return role;
	}
	
	public void setUserRole(UserRole role)
	{
		this.role = role;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
}
