package manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	// arrays to store all results from search
	private ArrayList<Account> results = new ArrayList<>();
	
	// this is dependency injection - receives shared instances instead of creating new ones
	public AccountManager(Scanner sc, File accountFile, ArrayList<Account> accounts) {
		this.sc = sc;
		this.accountFile = accountFile;
		this.accounts = accounts;
	}
	
	// to add account depending on the type (website, bank, or general)
	public void addAccountOption() {
		
		while (true) {
			System.out.println("Account type");
			System.out.println("[1] Website Account");
			System.out.println("[2] Bank Account");
			System.out.println("[3] General");
			System.out.println("[0] Back");
			System.out.print("\nSelect (0-3) ");
			int option = Util.validifyInput(0, 3, "> _", "Invalid input! [input must be 0-3]", sc);
			
			switch(option) {
			case 1:
				// for adding new website account
				Account newWebsite = Website.create(sc);
				if(newWebsite == null) continue; //user cancel
				
				if (Util.canAdd(newWebsite, accounts, sc)) {
					accounts.add(newWebsite);
					newWebsite.saveAccount(accountFile);
				}

				break;
			case 2:
				// for adding new bank account
				Account newBankAccount = Bank.create(sc);
				if(newBankAccount == null) continue; //user cancel
				
				if (Util.canAdd(newBankAccount, accounts, sc)) {
					accounts.add(newBankAccount);
					newBankAccount.saveAccount(accountFile);
				}

				break;
			case 3:
				// for adding new general account
				Account newAccount = Account.create(sc);
				if(newAccount == null) continue; //user cancel
				
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

			break;
		}
		
		System.out.print("\nPress enter to continue...");
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
			
			// Add the corrupted handler method later
			
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
		
		while (true) {
			System.out.println("======= ACCOUNT(S) =========");
			Util.displayList(accounts);
			
			System.out.println("============================");
			
			int num = 0;
			
			if (accounts.size() == 1) {
				System.out.print("[1] View  [0] Exit ");
				num = Util.validifyInput(0, accounts.size(), "> _", 
						("Invalid input! [input must be 0 or 1]"), 
						sc);
			} else {
				System.out.print("Select account (1-" + accounts.size() + ") or 0 to exit ");
				num = Util.validifyInput(0, accounts.size(), "> _", 
						("Invalid input! [input must be 1-" + accounts.size() + "]"), 
						sc);
			}
			
			if (num != 0) {
				Account acc = accounts.get(num-1);
				System.out.println();
				acc.displayFull();
				
				System.out.print("[1] Back   [0] Exit  ");
				int option = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
				
				if (option == 1) {
					System.out.println();
					continue;
				} else if (option == 0) {
					return;
				} else {
					System.out.println("Something went wrong");
					return;
				}
			} else if (num == 0) {
				return;
			} else {
				System.out.println("Something went wrong");
			}
		}
		
	}
	
	// This is called from main option 2 - search account
	public void searchAccountMenu() {
		while (true) {
			searchAccount("Search Account: ");
			
			if (!results.isEmpty()) { // if result is not empty
		        if (results.size() == 1) { // if result is only one
		        	System.out.println("============================");
		        	System.out.print("[1] View  [0] Exit ");
		        	int input = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
		        	
		        	if (input == 1) {
		        		Account acc = results.get(input-1);
		        		System.out.println();
		        		acc.displayFull();
		        		System.out.print("[1] Search Again  [0] Exit ");
		        		int anotherInput = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
		        		
		        		if (anotherInput == 1) {
		        			System.out.println();
		        			continue;
		        		} else {
		        			return;
		        		}
		        		
		        	} else if (input == 0) {
		        		return;
		        	}
		        	
		        } else { // multiple results
		        	System.out.println("============================");
		        	System.out.print("Select to view (1-" + results.size() + ") or 0 to Exit ");
		        	int input = Util.validifyInput(0, results.size(), "> _", ("Invalid input! [input must be 0-" + results.size() + "]"), sc);
		        	
		        	if (input == 0) {
		        		return;
		        	}
		        	
		        	Account acc = results.get(input-1);
		        	System.out.println();
		        	acc.displayFull();
		        	
		        	System.out.print("[1] Search Again  [0] Exit ");
	        		int anotherInput = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
	        		
	        		if (anotherInput == 1) {
	        			System.out.println();
	        			continue;
	        		} else {
	        			return;
	        		}
		        }
		        
		    } else { // if result is empty
		    	System.out.print("\n[1] Search Again  [0] Exit ");
		    	int input = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
		    	
		    	if (input == 1) {
		    		System.out.println();
		    		continue;
		    	} else {
		    		return;
		    	}
		    }
		}
		
	}
	
	// this is called from searchAccountMenu... (above)
	// The reason why there are 2 search function is because...
	// this method is also used in the delete process.
	private void searchAccount(String prompt) {
		results.clear();
		
	    if (accounts.isEmpty()) {
	        System.out.println("No account saved");
	        return;
	    }

	    System.out.print(prompt);
	    String query = sc.nextLine().toLowerCase();

	    if (query.isEmpty()) { // ← stops blank enter from matching everything
	        System.out.println("\n[Search cannot be empty]\n");
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
	
	// This function does not handle actual deletion process...
	// it only shows the options of what kind of deletion 
	// eg. Search by site/username or Browse all accounts
	public void deleteAccountMenu() { 
	    if (accounts.isEmpty()) {
	        System.out.println("No account to delete\n");
	        System.out.print("Press enter to continue...");
	        sc.nextLine();
	        System.out.println();
	        return;
	    }

	    if (accounts.size() == 1) {
	        deleteSingle(); // This is where the delete single (method) gets used
	        return;
	    }

	   while (true) {
		   boolean successful;
		   
		   System.out.println("Delete Account");
		   System.out.println("[1] Search by site/username");
		   System.out.println("[2] Browse all accounts");
		   System.out.println("[0] Back\n");
		   int deleteOption = Util.validifyInput(0, 2, "Select (0-2) > _", "Invalid input! [input must be 0-2]", sc);

		   if (deleteOption == 1) {
			   System.out.println();
			   searchAccount("Search account to delete: ");
		       if (results.isEmpty()) {
		    	   System.out.println();
		    	   continue;
		       }
		       deletePrompt(results.size());
		       successful = deleteFromResults(); // This is where the delete from results (method) gets used
		       
		       if (!successful) {
		    	   System.out.println();
		    	   continue;
		       }
		       
		       return;
		       
		   } else if (deleteOption == 2) {
			   System.out.println();
		       Util.displayList(accounts);
		       deletePrompt(accounts.size());
		       successful = deleteByIndex(); // This is where the delete by index (method) gets used
		       
		       if (!successful) {
		    	   System.out.println();
		    	   continue;
		       }
		       
		       return;
		       
		   } else if (deleteOption == 0) return;
		   else System.out.println("Something went wrong");
	   }
	   
	}
	
	// helper prompt for deleteAccountMenu
	private void deletePrompt(int size) {
	    if (size == 1) {
	        System.out.print("[1] Delete the account  [0] Cancel ");
	    } else if (size == 2) {
	        System.out.println("Enter number(s) to delete (e.g. 1 or 1,2) or 0 to cancel");
	    } else {
	        System.out.println("Enter number(s) to delete (e.g. 1 or 1,2," + size + ") or 0 to cancel");
	    }
	}
	
	private void deleteSingle() {
	    Util.displayList(accounts);
	    deletePrompt(1);
	    int opt = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
	    if (opt == 1) {
	        accounts.remove(0);
	        overwriteSave();
	        System.out.println("\nAccount deleted successfully!\n");
	        System.out.print("Press enter to continue...");
	        sc.nextLine();
	    } else {
	        System.out.println("\n[No account deleted]");
	    }
	}
	
	// Deletes from the full accounts list (Browse all path)
	// Index entered by user maps directly to accounts ArrayList
	
	/* this code below allows for multiple deletion in one input
	 * faster deletion process than individually
	 * 
	 * receive user input in string
	 * the string is divided by separator ","
	 * each separated elements are verified whether the input is valid 
	 * (e.g no letter, no numbers out of range, no characters etc.)
	 */
	
	// NOTE: This process below still accepts single input 
	
	private boolean deleteByIndex() {
		
		String[] option = Util.promptDeleteInput(sc, accounts.size());
		
		ArrayList<String> seen = new ArrayList<>();
		
		// this part removes duplicate
		
		for (String opt : option) {
			//example: 3,1,1
			// if seen does not contain 1 then add it to seen, if otherwise skip that 1 (in the 3rd loop)
		    if (!seen.contains(opt)) {
		        seen.add(opt);
		    }
		}
		
		// this part is to delete the chosen options
		
		for (String opt : seen) {
		    try {
		        int num = Integer.parseInt(opt);
		        if (num == 0 && seen.size() == 1) {
		            System.out.println("\n[No account deleted]");
		            return false; // false - delete not successful
		        }
		        
		        if (num == 0) continue; // skip 0 if mixed with other numbers
		        
		        // why num-1? Display:    1    2    3
		        //            ArrayList:  0    1    2
		        
		        accounts.remove(num-1);
		        
		        // user types 1 → removes index 0 ✓
		        // user types 2 → removes index 1 ✓
		        // user types 3 → removes index 2 ✓
		        
		    } catch (NumberFormatException e) {
		        System.out.println("Error, unable to format string to integer");
		    }
		}
		
		overwriteSave();
		System.out.println("\nAccount deleted successfully!\n");
		System.out.print("Press enter to continue...");
		sc.nextLine();
		
		return true; // true - delete successful
	}
	
	// Deletes from search results (Search path)
	// Index entered by user maps to results ArrayList, not accounts
	// Removes by object reference since positions differ between results and accounts
	
	// NOTE: This process below still accepts single input 
	
	public boolean deleteFromResults() {
	    
		String[] option = Util.promptDeleteInput(sc, results.size());
		
	    ArrayList<String> seen = new ArrayList<>();
	    
	    for (String opt : option) {
	        if (!seen.contains(opt)) seen.add(opt);
	    }
	    
	    // this part is to delete the chosen options
		
	 	for (String opt : seen) {
	 		try {
	 			int num = Integer.parseInt(opt);
	 		    if (num == 0 && seen.size() == 1) {
	 		    	System.out.println("\n[No account deleted]");
	 		        return false; // false - delete not successful
	 		    }
	 		        
	 		    if (num == 0) continue; // skip 0 if mixed with other numbers
	 		        
	 		    // why num-1? Display:    1    2    3
	 		    //            ArrayList:  0    1    2
	 		        
	 		    accounts.remove(results.get(num-1));
	 		        
	 		    // user types 1 → removes index 0 ✓
	 		    // user types 2 → removes index 1 ✓
	 		    // user types 3 → removes index 2 ✓
	 		        
	 		} catch (NumberFormatException e) {
	 		    System.out.println("Error, unable to format string to integer");
	 		}
	 	}
	    
	    overwriteSave();
	    System.out.println("\nAccount deleted successfully!\n");
	    System.out.print("Press enter to continue...");
	    sc.nextLine();
	    
	    return true; // true - delete successful
	    
	}
	
	// This is the delete inside File happens
	void overwriteSave() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(accountFile))) {
			for (Account acc : accounts) {
				acc.saveAccountLine(bw);
			}
		} catch (IOException e) {
			System.out.println("Error, unable to over write file");
		}
	}
	
}
