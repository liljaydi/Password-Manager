package model;

public class Account {

	private String site;
	private String username;
	private String password;
	
	public Account(String site, String username, String password) {
		this.site 	  = site;
		this.username = username;
		this.password = password;
	}

	public String getSite() {
		return site;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	
	

}
