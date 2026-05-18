package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import model.Account;

public class AccountManager {

	private Scanner sc;
	private Utility util;
	private ArrayList<Account> arraysOfAccountObject;
	private ArrayList<Account> results;
	
	public AccountManager(Scanner sc, Utility util, ArrayList<Account> arraysOfAccountObject, ArrayList<Account> results) {
		this.sc   = sc;
		this.util = util;
		this.arraysOfAccountObject = arraysOfAccountObject;
		this.results = results;
	}
	
	/**This methods below are used to manage the accounts...
	 * add account
	 * search account
	 * view all account
	 * delete account
	 */
	
	public void addAccount() {
		String site;
		String username;
		
		String encryptedPass;
		//loops while duplicate is found within the saved accounts
		while (true) {
			System.out.println("Press Enter on any field to cancel.");
			System.out.print("Enter site/app: ");
			site = sc.nextLine();
			if (site.isEmpty()) {
				System.out.println("\nGoing back to menu...");
				return;
			}
			System.out.print("Enter username: ");
			username = sc.nextLine();
			if (username.isEmpty()) {
				System.out.println("\nGoing back to menu...");
				return;
			}

			if (util.checkDuplicate(site, username)) {
				System.out.println("\nDuplicate account found");
				System.out.println("[1] Add different account");
				System.out.println("[0] Back to menu");
				int option = util.validifyInput(0, 1, "Option: ", "Invalid input! [input must be 0 or 1]");
				System.out.println();
				
				if (option == 1) continue;
				else if (option == 0) {
					System.out.println("Going back to menu...");
					return;
				}
				else {
					System.out.println("Something went wrong\n");
					return;
				}
			} 
			System.out.print("Enter password: ");
			String password = sc.nextLine();
			if (password.isEmpty()) {
				System.out.println("\nGoing back to menu...");
				return;
			}
			encryptedPass = util.encrypt(password);
			break;
			
		}
		//password is encrypted before saving to file
		//data is send to utility save method
		util.saveAccount(site, username, encryptedPass);
		System.out.print("\nPress enter to go back to menu...");
		sc.nextLine();
	}
	
	public boolean searchAccount() {
		results.clear();
		
	    if (arraysOfAccountObject.isEmpty()) {
	        System.out.println("No account saved\n");
	        return false;
	    }

	    System.out.print("Search (site/username): ");
	    String query = sc.nextLine().toLowerCase();

	    if (query.isEmpty()) { // ← stops blank enter from matching everything
	        System.out.println("Search cannot be empty\n");
	        return false;
	    }
	    
	    for (Account acc : arraysOfAccountObject) {
	        if (acc.getSite().toLowerCase().contains(query) ||
	            acc.getUsername().toLowerCase().contains(query)) {
	            results.add(acc);
	        }
	    }

	    if (results.isEmpty()) {
	        System.out.println("No account found matching \"" + query + "\"\n");
	        return false;
	    } else {
	        System.out.println("Found " + results.size() + " result(s):\n");
	        for (int i = 0; i < results.size(); i++) {
	        	System.out.println("Account [" + (i+1) + "]");
	        	Account acc = results.get(i);
	            System.out.println("| Site/App: " + acc.getSite());
	            System.out.println("| Username: " + acc.getUsername());
	            System.out.println("| Password: " + util.decrypt(acc.getPassword()));
	            System.out.println();
	        }
	        return true;
	    }
	}
	
	public void viewAllAccount() {
		//checks first if there are saved accounts
		if (arraysOfAccountObject.size() == 0) {
			System.out.println("No account saved\n");
			return;
		}
		
		for (int i = 0; i < arraysOfAccountObject.size(); i++) {
			System.out.println("Account [" + (i+1) + "]");
			Account acc = arraysOfAccountObject.get(i);
			System.out.println("| Site/App: " + acc.getSite());
			System.out.println("| Username: " + acc.getUsername());
			System.out.println("| Password: " + util.decrypt(acc.getPassword()));
			System.out.println();
		}
	}
	
	public void deleteAcountMenu() {
		//for account deletion
		//checks if account is available
		if (arraysOfAccountObject.isEmpty()) {
			System.out.println("No account to delete\n");
			System.out.print("Press enter to go back to menu...");
			sc.nextLine();
			System.out.println();
			return;
		}
		//if only 1 account (skip below display) go to delete method directly
		if (arraysOfAccountObject.size() == 1) {
			deleteAccount();
			return;
		}
		//displays if multiple accounts are found
		System.out.println("Delete Account");
		System.out.println("[1] Search by site/username");
		System.out.println("[2] Browse all accounts");
		System.out.println("[0] Back");
		int deleteOption = util.validifyInput(0, 2, "Option: ", "Invalid input! [input must be 0-2]");
		System.out.println();
		
		if (deleteOption == 1) {				
			if (searchAccount()) {
				if (results.size() == 1) {
					System.out.println("[1] Delete the account");
					System.out.println("[0] Cancel");

				} else {
					if (results.size() == 2) System.out.println("Enter number(s) to delete (e.g. 1 or 1,2)");
					else System.out.println("Enter number(s) to delete (e.g. 1 or 1,2," + results.size() + ")");
					System.out.println("Enter 0 to cancel");
				}
			} else {
				return;
			}
			deleteFromResults();
		} else if (deleteOption == 2) {
			viewAllAccount();
			//if account is only 1... output is already handled...
			if (arraysOfAccountObject.size() == 2) System.out.println("Enter number(s) to delete (e.g. 1 or 1,2)");
			else System.out.println("Enter number(s) to delete (e.g. 1 or 1,2," + arraysOfAccountObject.size() + ")");
			System.out.println("Enter 0 to cancel");
			deleteAccount();
		}
		else if (deleteOption == 0) return;
		else System.out.println("Something went wrong1");
	}
	
	public void deleteAccount() {
		//runs when only one account is found
		if (arraysOfAccountObject.size() == 1) {
			viewAllAccount();
			System.out.println("[1] Delete the account");
			System.out.println("[0] Cancel");
			System.out.println("Enter the number of your choice");
			int deleteOption = util.validifyInput(0, 1, "Option: ", "Invalid input! [input must be 0 or 1]");
			if (deleteOption == 1) {
				arraysOfAccountObject.remove(0);
				util.saveAccountOverwriteData();
				System.out.println("Account deleted successfully!\n");
				System.out.print("Press enter to go back to menu...");
				sc.nextLine();
				System.out.println();
				return;
			} 
			else if (deleteOption == 0) {
				System.out.println("No account deleted...\n");
				return;
			}
			else System.out.println("Something went wrong\n");
		}
		//else if multiple accounts...
		
		
		/**this code below allows for multiple deletion in one input
		 * faster deletion process than individually
		 * 
		 * receive user input in string
		 * the string is divided by separator ","
		 * each separated elements are verified whether the input is valid 
		 * (e.g no letter, no numbers out of range, no characters etc.)
		 */
		
		String[] option;
		boolean invalid = false;
		int count = 1;
		
		do {
			invalid = false;
			System.out.print("Option: ");
			String options = sc.nextLine();
			
			option = options.split(",\\s*");
			for (String opt : option) {
				if (util.validifyVariousInput(opt, 0, arraysOfAccountObject.size())) continue;
				if (count >= 4 ) {
					if (count >= 5) {
						System.out.println("Too many invalid attempts. Exiting program...");
						System.exit(0);
					}
					else System.out.println("Too many invalid attempts. Please restart or check your input.");
				}
				else System.out.println("Invalid input! [input must be 0-" + arraysOfAccountObject.size() + "]");
				invalid = true;
				count++;
				break;
			}
		} while (invalid);
		/**this part reverses the deletion process..
		 * 
		 * (reason)
		 * Because when you delete by index while looping forward
		 * the indices shift after each deletion, causing you to skip elements or hit wrong ones.
		 */
		Arrays.sort(option, (a, b) -> Integer.parseInt(b) - Integer.parseInt(a));
		//to be studied
		ArrayList<String> seen = new ArrayList<>();
		for (String o : option) {
		    if (!seen.contains(o)) {
		        seen.add(o);
		    }
		}
		
		for (String o : seen) {
		    try {
		        int num = Integer.parseInt(o);
		        if (num == 0) {
		            System.out.println("No account deleted\n");
		            return;
		        }
		        arraysOfAccountObject.remove(num-1);
		    } catch (NumberFormatException e) {
		        System.out.println("Error, unable to format string to integer");
		    }
		}
		
		util.saveAccountOverwriteData();
		System.out.println("Account deleted successfully!\n");
		System.out.print("Press enter to go back to menu...");
		sc.nextLine();
		System.out.println();
	}
	
	public void deleteFromResults() {
	    String[] option;
	    boolean invalid = false;
	    int count = 1;
	    
	    do {
	        invalid = false;
	        System.out.print("Option: ");
	        String options = sc.nextLine();

	        option = options.split(",\\s*");
	        for (String opt : option) {
	            if (util.validifyVariousInput(opt, 0, results.size())) continue;
	            if (count >= 4 ) {
					if (count >= 5) {
						System.out.println("Too many invalid attempts. Exiting program...");
						System.exit(0);
					}
					else System.out.println("Too many invalid attempts. Please restart or check your input.");
				}
	            else System.out.println("Invalid input! [input must be 0-" + results.size() + "]");
	            invalid = true;
	            count++;
	            break;
	        }
	    } while (invalid);

	    Arrays.sort(option, (a, b) -> Integer.parseInt(b) - Integer.parseInt(a));
	    ArrayList<String> seen = new ArrayList<>();
	    for (String o : option) {
	        if (!seen.contains(o)) seen.add(o);
	    }

	    for (String o : seen) {
	        int num = Integer.parseInt(o);
	        if (num == 0) {
	            System.out.println("No account deleted\n");
	            return;
	        }
	        arraysOfAccountObject.remove(results.get(num - 1)); // removes by object reference
	    }

	    util.saveAccountOverwriteData();
	    System.out.println("Account deleted successfully!\n");
	    System.out.print("Press enter to go back to menu...");
	    sc.nextLine();
	    System.out.println();
	}

}
