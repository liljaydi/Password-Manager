package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import manager.Crypto;

public class Account {
	protected String service;
	protected String username;
	protected String password;
	
	public Account(String service, String username, String password) {
		this.service = service;
		this.username = username;
		this.password = password;
	}
	
	public static Account create(Scanner sc) {
		System.out.println("\nPress Enter on any field to cancel.");
		
		System.out.print("Enter App      : ");
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

		return new Account(service, username, password);
	}
	
	public boolean isDuplicate(ArrayList<Account> accounts) {
		for (Account acc : accounts) {
			//compare first if the type of account is the same
			if (acc.getClass() != this.getClass()) continue;
			if (acc.service.equalsIgnoreCase(this.service) && 
				acc.username.equals(this.username)) return true;
		}
		return false;
		
	}
	
	public void saveAccount(File accountFile) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile, true))) {
			String encryptedPass = Crypto.encrypt(password);
			
			bw.write("GENERAL" + "\u001F" + 
			         service   + "\u001F" + 
					 username  + "\u001F" + 
			         encryptedPass);
			bw.newLine();

			System.out.println("Account saved successfully!");
		} catch (IOException e) {
			System.out.println("Error, unable to write file");
		}
	}
	
	public void displayFormat() {
		System.out.println(service + " [GENERAL]");
		System.out.println("    Username : " + username);
		System.out.println("    Password : " + password);
		System.out.println();
	}
	
	public boolean matches(String query) {
		query = query.toLowerCase();
		return service.toLowerCase().contains(query) || 
			username.toLowerCase().contains(query);
	}
	
	public static Account load(String line) {
		String[] data = line.split("\u001F");
		
		if (data.length != 4) {
			return null;
		}
		
		String service  = data[1];
		String username = data[2];
		String password = Crypto.decrypt(data[3]);
		
		return new Account(service, username, password);
	}
	
	public void saveAccountLine(BufferedWriter bw) throws IOException {
		String encryptedPass = Crypto.encrypt(password);
		
		bw.write("GENERAL" + "\u001F" + 
		         service   + "\u001F" + 
				 username  + "\u001F" + 
		         encryptedPass);
		bw.newLine();
	}
	
}
