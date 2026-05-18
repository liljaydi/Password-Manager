package manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import model.Account;

public class Utility {

	private Scanner sc;
	private File accountFile;
	private ArrayList<Account> arraysOfAccountObject;
	
	private static final String KEY = "1234567890123456";
	
	public Utility (Scanner sc, File accountFile, ArrayList<Account> arraysOfAccountObject) {
		this.sc = sc;
		this.accountFile = accountFile;
		this.arraysOfAccountObject = arraysOfAccountObject;
	}

	boolean checkDuplicate(String site, String username) {
		for (int i = 0; i < arraysOfAccountObject.size(); i++) {
			Account acc = arraysOfAccountObject.get(i);
			if (site.equalsIgnoreCase(acc.getSite()) && username.equals(acc.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	public int validifyInput(int range1, int range2, String prompt1, String prompt2) {
		int option;
		int count = 1;
		while (true) {
			if (count > 4 ) {
				if (count > 5) {
					System.out.println("Too many invalid attempts. Exiting program...");
					System.exit(0);
				}
				else System.out.println("Too many invalid attempts. Please restart or check your input.");
			}
			
			try {
				System.out.print(prompt1);
				option = Integer.parseInt(sc.nextLine());
				if (option < range1 || option > range2) {
					if (count >= 4 ) {
						count++;
						continue;
					}
					else System.out.println(prompt2);
					count++;
					continue;
				}
				return option;
			}
			catch (NumberFormatException e) {
				if (count >= 4 ) {
					count++;
					continue;
				}
				else System.out.println(prompt2);
				count++;
			}
		}
	}
	
	boolean validifyVariousInput(String option, int range1, int range2) {
		try {
			int num = Integer.parseInt(option);
			if (num < range1 || num > range2) return false;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}	
	}
	
	void saveAccount(String site, String username, String password) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile, true))) {
			bw.write(site + "\u001F" + username + "\u001F" + password);
			bw.newLine();
			//account is declared as an object then save to file
			Account newAccount = new Account(site, username, password);
			arraysOfAccountObject.add(newAccount);
			System.out.println("Account saved successfully!");
		} catch (IOException e) {
			System.out.println("Error, unable to write file");
		}		
	}
	
	void saveAccountOverwriteData() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile))) {
			String line = "";
			for (Account acc : arraysOfAccountObject) {
				line = acc.getSite() + "\u001F" + acc.getUsername() + "\u001F" + acc.getPassword();
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("Error, unable to over write file");
		}
	}
	
	public void loadAccount() {
		if (!accountFile.exists()) {
			System.out.println("No file saved\n");
			return;
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(accountFile))) {
			String line;
			int count = 0;
			
			ArrayList<String> corrupted = new ArrayList<>();
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split("\u001F");
				if (data.length != 3) {
				    corrupted.add(line);
				    count++;
				    continue;
				}
				
				String site     = data[0];
				String username = data[1];
				String password = data[2];
				
				Account savedAccount = new Account(site, username, password);
				arraysOfAccountObject.add(savedAccount);
			}
			
			if (count != 0) {
			    System.out.println("\nFound " + corrupted.size() + " corrupted account(s)\n");
			    
			    System.out.println("See details?");
			    int seeDetails = validifyInput(0, 1, "Select option. yes(1) / no(0): ", "Invalid input! [input must be 0 or 1]");
			    
			    if (seeDetails == 1) {
			        System.out.println();
			        for (int i = 0; i < corrupted.size(); i++) {
			            System.out.println("Account " + (i+1) +": " + corrupted.get(i));    
			        
			        }
			        
			        System.out.println("\nCannot load account, the data format is unreadable\n");
			        System.out.println("Do you want to delete the corrupted data?");
			        
			        int delete = validifyInput(0,1, "Select option. yes(1) / no(0): ", "Invalid input! [input must be 0 or 1]");
			    
			        if (delete == 1) {
			            System.out.println("\nAre you sure you want to delete? Once deleted, you will not be able to retrieve the data\n[1] proceed deletion\n[0] cancel");
			            int proceedDelete = validifyInput(0, 1, "Select option: ", "Invalid input! input must be [0 or 1]");
			           
			            if (proceedDelete == 1) {
			                saveAccountOverwriteData();
			            System.out.println("\nAccount deleted...\n");
			            } else {
			                System.out.println();  
			            }
			        } else {
			            System.out.println("\nThe corrupted account is saved inside \"account.txt\" file and can only be viewed externally\n");
			        }
			    } else {
			        System.out.println();
			    }
			}
			
			
			
			System.out.println("File loaded\n");
		} catch (IOException e) {
			System.out.println("Error, unable to load file");
		}
		
	}
	
	public String encrypt(String password) {
	    try {
	        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);

	        byte[] encrypted = cipher.doFinal(password.getBytes());
	        return Base64.getEncoder().encodeToString(encrypted);

	    } catch (Exception e) {
	        System.out.println("Encryption error");
	        return password;
	    }
	}
	
	public String decrypt(String encryptedPassword) {
	    try {
	        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);

	        byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
	        return new String(cipher.doFinal(decoded));

	    } catch (Exception e) {
	        System.out.println("Decryption error");
	        return encryptedPassword;
	    }
	}
	
}
