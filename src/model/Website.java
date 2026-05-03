package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import manager.Crypto;

public class Website extends Account {
	private String url;
	
	public Website(String service, String username, String password, String url) {
		super(service, username, password);
		this.url = url;
	}
	
	//Why create is static? search for factory method...
	
	//Factory Method is part of a family called Creational Design Patterns 
	//— patterns specifically about how objects get created. 
	//Others in the family include Abstract Factory, Builder, and Singleton. 
	public static Website create(Scanner sc) {
		System.out.println("\nPress Enter on any field to cancel.");
		
		System.out.print("Enter website  : ");
		String service = sc.nextLine();
		if (service.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}

		System.out.print("Enter username : ");
		String username = sc.nextLine();
		if (username.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}
			
		System.out.print("Enter password : ");
		String password = sc.nextLine();
		if (password.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}
		
		System.out.print("Enter URL      : ");
		String url = sc.nextLine();
		if (url.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}
		
		return new Website(service, username, password, url);
	}
	
	@Override
	public void saveAccount(File accountFile) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile, true))) {
			String encryptedPass = Crypto.encrypt(password);
			
			bw.write("WEBSITE"     + "\u001F" + 
					 service       + "\u001F" + 
					 username      + "\u001F" + 
					 encryptedPass + "\u001F" + 
					 url);
			bw.newLine();

			System.out.println("Account saved successfully!");
		} catch (IOException e) {
			System.out.println("Error, unable to write file");
		}		
	}

	@Override
	public void displayFormat() {
		System.out.println(service + " [WEBSITE]");
		System.out.println("    Username : " + username);
		System.out.println("    Password : " + password);
		System.out.println("    URL      : " + url);
		System.out.println();
	}
	
	@Override
	public boolean matches(String query) {
		query = query.toLowerCase();
		return service.toLowerCase().contains(query) || 
			username.toLowerCase().contains(query) ||
			url.toLowerCase().contains(query);
	}
	
	public static Website load(String line) {
		String[] data = line.split("\u001F");
		
		if (data.length != 5) {
		    return null;
		}
		
		String service  = data[1];
		String username = data[2];
		String password = Crypto.decrypt(data[3]);
		String url      = data[4];
		
		return new Website(service, username, password, url);
	}
	
	
	@Override
	public void saveAccountLine(BufferedWriter bw) throws IOException {
		String encryptedPass = Crypto.encrypt(password);
		
		bw.write("WEBSITE"     + "\u001F" + 
				 service       + "\u001F" + 
				 username      + "\u001F" + 
				 encryptedPass + "\u001F" + 
				 url);
		bw.newLine();
	}
}
