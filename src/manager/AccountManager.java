package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Account;
import model.Bank;
import model.Website;

public class AccountManager {
	private Scanner sc;
	private File accountFile;
	private ArrayList<Account> accounts;
	
	//arrays to store all results from search
	private ArrayList<Account> results = new ArrayList<>();
	
	//this is dependency injection - receives shared instances instead of creating new ones
	public AccountManager(Scanner sc, File accountFile, ArrayList<Account> accounts) {
		this.sc = sc;
		this.accountFile = accountFile;
		this.accounts = accounts;
	}
	
	//to add account depending on the type (website, bank, or general)
	public void addAccountOption() {
		System.out.println("Select account type:");
		System.out.println("[1] Website Account");
		System.out.println("[2] Bank Account");
		System.out.println("[3] General");
		System.out.println("[0] Cancel");
		int option = Util.validifyInput(0, 3, "Option: ", "Invalid input! [input must be 0-3]", sc);
		
		switch(option) {
			case 1:
				//for adding new website account
				Account newWebsite = Website.create(sc);
				if(newWebsite == null) return; //user cancel
				
				if (Util.canAdd(newWebsite, accounts, sc)) {
					accounts.add(newWebsite);
					newWebsite.saveAccount(accountFile);
				}

				break;
			case 2:
				//for adding new bank account
				Account newBankAccount = Bank.create(sc);
				if(newBankAccount == null) return; //user cancel
				
				if (Util.canAdd(newBankAccount, accounts, sc)) {
					accounts.add(newBankAccount);
					newBankAccount.saveAccount(accountFile);
				}

				break;
			case 3:
				//for adding new general account
				Account newAccount = Account.create(sc);
				if(newAccount == null) return; //user cancel
				
				if (Util.canAdd(newAccount, accounts, sc)) {
					accounts.add(newAccount);
					newAccount.saveAccount(accountFile);
				}
				
				break;
			case 0:
				return;
			default:
				System.out.println("\nInvalid option");
				return;
		}
		
		System.out.print("\nPress enter to go back to menu...");
		sc.nextLine();
		
	}
	
	public void loadAccount() {
		if (!accountFile.exists()) {
			System.out.println("No file saved\n");
			return;
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(accountFile))) {
			String line;
			
			ArrayList<String> corrupted = new ArrayList<>();
			
			while ((line = br.readLine()) != null) {
				
				if (line.startsWith("WEBSITE")) {
					Website savedWebsite = Website.load(line);
					if (savedWebsite == null) {
						corrupted.add(line);
						continue;
					}
					accounts.add(savedWebsite);
				} 
				
				else if (line.startsWith("BANK")) {
					Bank savedBankAccount = Bank.load(line);
					if (savedBankAccount == null) {
						corrupted.add(line);
					    continue;
					}
					accounts.add(savedBankAccount);
				}
				
				else if (line.startsWith("GENERAL")) {
					Account savedAccount = Account.load(line);
					if (savedAccount == null) {
					    corrupted.add(line);
					    continue;
					}
					accounts.add(savedAccount);
				}
				
				else {
					corrupted.add(line);
				}
			}
			
			if (corrupted.size() != 0) {
				System.out.println("Corrupted account found");
				for (String corrupt : corrupted) {
					System.out.println(corrupt);
				}
				System.out.println();
			}
			
			//Add the corrupted handler method later
			
			System.out.println("File loaded\n");
		} catch (IOException e) {
			System.out.println("Error, unable to load file");
		}
		
	}
	
	public void displayAccount() {
		if (accounts.isEmpty()) {
	        System.out.println("No account saved");
	        return;
	    }
		
		Util.displayList(accounts);
		
		System.out.print("Press enter to go back to menu...");
	    sc.nextLine();
	}
	
	public void searchAccountMenu() {
		searchAccount("Search Account: ");
		if (!results.isEmpty()) {
	        System.out.print("Press enter to go back to menu...");
	        sc.nextLine();
	    }
	}
	
	
	private void searchAccount(String prompt) {
		results.clear();
		
	    if (accounts.isEmpty()) {
	        System.out.println("No account saved");
	        return;
	    }

	    System.out.print(prompt);
	    String query = sc.nextLine().toLowerCase();

	    if (query.isEmpty()) { // ← stops blank enter from matching everything
	        System.out.println("Search cannot be empty");
	        return;
	    }
	    
	    for (Account acc : accounts) {
	    	if (acc.matches(query)) {
	    		results.add(acc);
	    	}
	    }

	    if (results.isEmpty()) {
	        System.out.println("No account found matching \"" + query + "\"");
	    } else {
	        System.out.println("Found " + results.size() + " result(s):\n");
	        Util.displayList(results);
	    }
	}
	
	public void deleteAccountMenu() {
	    if (accounts.isEmpty()) {
	        System.out.println("No account to delete\n");
	        System.out.print("Press enter to go back to menu...");
	        sc.nextLine();
	        System.out.println();
	        return;
	    }

	    if (accounts.size() == 1) {
	        deleteAccount();
	        return;
	    }

	    System.out.println("Delete Account");
	    System.out.println("[1] Search by site/username");
	    System.out.println("[2] Browse all accounts");
	    System.out.println("[0] Back");
	    int deleteOption = Util.validifyInput(0, 2, "Option: ", "Invalid input! [input must be 0-2]", sc);
	    System.out.println();

	    if (deleteOption == 1) {
	        searchAccount("Search account to delete: ");
	        if (results.isEmpty()) return;
	        deletePrompt(results.size());
	        deleteFromResults();

	    } else if (deleteOption == 2) {
	        Util.displayList(accounts);
	        deletePrompt(accounts.size());
	        deleteAccount();

	    } else if (deleteOption == 0) return;
	    else System.out.println("Something went wrong");
	}

	private void deletePrompt(int size) {
	    if (size == 1) {
	        System.out.println("[1] Delete the account");
	        System.out.println("[0] Cancel");
	    } else if (size == 2) {
	        System.out.println("Enter number(s) to delete (e.g. 1 or 1,2)");
	        System.out.println("Enter 0 to cancel");
	    } else {
	        System.out.println("Enter number(s) to delete (e.g. 1 or 1,2," + size + ")");
	        System.out.println("Enter 0 to cancel");
	    }
	}
	
	private void deleteAccount() {
		
		
		
	}
	
	private void deleteFromResults() {
		
		
		
	}
	
}
