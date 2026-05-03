package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import manager.Crypto;

public class Bank extends Account {
	private String accNumber;
	private String pin;
	
	public Bank(String service, String username, String password, String accNumber, String pin) {
		super(service, username, password);
		this.accNumber = accNumber;
		this.pin = pin;
	}
	
	public static Bank create(Scanner sc) {
		System.out.println("\nPress Enter on any field to cancel.");
		
		System.out.print("Enter Bank name      : ");
		String service = sc.nextLine();
		if (service.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}

		System.out.print("Enter username       : ");
		String username = sc.nextLine();
		if (username.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}
			
		System.out.print("Enter password       : ");
		String password = sc.nextLine();
		if (password.isEmpty()) {
			System.out.println("\n[Cancelled]"); return null;
		}
			
		System.out.print("Enter Account number : ");
		String accNumber = sc.nextLine();
		// check if it's all digits
		if (!accNumber.matches("\\d+")) {
		    System.out.println("Invalid account number");
		    return null;
		}
			
		System.out.print("Enter pin            : ");
		String pin = sc.nextLine();
		// check if it's all digits
		if (!accNumber.matches("\\d+")) {
			System.out.println("Invalid PIN");
			return null;
		}
				
		return new Bank(service, username, password, accNumber, pin);
	}
	
	@Override
	public void saveAccount(File accountFile) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile, true))) {
			String encryptedPass = Crypto.encrypt(password);
			String encryptedAccNum = Crypto.encrypt(String.valueOf(accNumber));
			String encryptedPin = Crypto.encrypt(String.valueOf(pin));
			
			bw.write("BANK"          + "\u001F" + 
					 service         + "\u001F" + 
					 username        + "\u001F" + 
					 encryptedPass   + "\u001F" + 
					 encryptedAccNum + "\u001F" + 
					 encryptedPin);
			bw.newLine();

			System.out.println("Account saved successfully!");
		} catch (IOException e) {
			System.out.println("Error, unable to write file");
		}		
	}
	
	@Override
	public void displayFormat() {
		System.out.println(service + " [BANK]");
		System.out.println("    Username : " + username);
		System.out.println("    Password : " + password);
		System.out.println("    Account# : " + accNumber);
		System.out.println("    pin      : " + pin);
		System.out.println();
	}
	
	@Override
	public boolean matches(String query) {
		query = query.toLowerCase();
		String stringNumber = String.valueOf(accNumber);
		return service.toLowerCase().contains(query) || 
			username.toLowerCase().contains(query) ||
			stringNumber.toLowerCase().contains(query);
	}
	
	public static Bank load(String line) {
		String[] data = line.split("\u001F");
		
		if (data.length != 6) {
			return null;
		}
		
		String service   = data[1];
		String username  = data[2];
		String password  = Crypto.decrypt(data[3]);
		String accNumber = Crypto.decrypt(data[4]);
		String pin       = Crypto.decrypt(data[5]);
		
		return new Bank(service, username, password, accNumber, pin);
	}
	
	@Override
	public void saveAccountLine(BufferedWriter bw) throws IOException {
		String encryptedPass = Crypto.encrypt(password);
		String encryptedAccNum = Crypto.encrypt(String.valueOf(accNumber));
		String encryptedPin = Crypto.encrypt(String.valueOf(pin));
		
		bw.write("BANK"          + "\u001F" + 
				 service         + "\u001F" + 
				 username        + "\u001F" + 
				 encryptedPass   + "\u001F" + 
				 encryptedAccNum + "\u001F" + 
				 encryptedPin);
		bw.newLine();
	}
}
