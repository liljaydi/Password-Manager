package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import manager.AccountManager;
import manager.MasterPasswordManager;
import manager.Utility;
import model.Account;

public class MenuPasswordManager {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ArrayList<Account> arraysOfAccountObject = new ArrayList<>();
		
		/**Account class that contains data for each account is stored into an array...
		 * Account object contains (site, user name, password)
		 * this object is stored into ArrayList
		 */
		
		ArrayList<Account> results = new ArrayList<>();//<<--This object is for the search feature
		
		File passwordFile = new File(System.getProperty("user.dir") + "/data/masterPassword.txt");
		File accountFile  = new File(System.getProperty("user.dir") + "/data/account.txt");
		passwordFile.getParentFile().mkdirs();
        accountFile.getParentFile().mkdirs();

        /**getParentFile()` extracts the parent → `C:/Projects/PasswordManagerVersion2/data
         * `mkdirs()` looks at your actual file system — `data/` folder doesn't exist yet → **creates it**
         * - Now your folder structure looks like:
         * PasswordManagerVersion2/
    	 *     data/         ← just created, empty
    	 *     
    	 * new FileWriter(accountFile, true)
    	 * - Sees `data/` folder exists ✅
    	 * - `account.txt` doesn't exist yet → **creates it**
    	 * - Now:
    	 * 
    	 * PasswordManagerVersion2/
    	 *     data/
    	 *         account.txt  ← just created, empty
         */

		Utility util              = new Utility(sc, accountFile, arraysOfAccountObject);
		MasterPasswordManager mpm = new MasterPasswordManager(passwordFile, sc, util);
		AccountManager am         = new AccountManager(sc, util, arraysOfAccountObject, results);
		
		System.out.println("    ====================");
		System.out.println("    |                  |");
		System.out.println("    | Password Manager |");
		System.out.println("    |                  |");
		System.out.println("    ====================\n");
		
		System.out.println("Welcome to Password Manager!");
		
		if (passwordFile.exists() && (passwordFile.length() != 0)) {
			if(mpm.checkPassword("Enter master password: ")) {
				System.out.println("Access granted!");
			} else {
				System.out.println("Access denied");
				System.out.println("Exiting...");
				System.exit(0);
			}
		}
		
		util.loadAccount();
		
		int option;
		do {
			
			System.out.println("=========== Menu ===========");
			System.out.println("| [1] Add Account          |");
			System.out.println("| [2] Search Account       |");
			System.out.println("| [3] View all Account     |");
			System.out.println("| [4] Delete Account       |");
			System.out.println("| [5] Settings             |");
			System.out.println("| [6] Exit                 |");
		    System.out.println("============================");
		    System.out.println("Enter the number of your choice");
		    option = util.validifyInput(1, 6, "Option: ", "Invalid input! [input must be 1-6]"); 
		    
		    System.out.println();
		    
			switch (option) {
				case 1:
					am.addAccount();
					System.out.println();
					break;
				case 2:
					am.searchAccount();
					System.out.print("Press enter to go back to menu...");
				    sc.nextLine();
				    System.out.println();
					break;
				case 3:
					am.viewAllAccount();
					System.out.print("Press enter to go back to menu...");
					sc.nextLine();
					System.out.println();
					break;
				case 4:
					am.deleteAcountMenu();
					break;
				case 5:
					mpm.masterPasswordSettings();
					break;
				case 6:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Something went wrong");
			}
		}  while (option != 6);

	}

}
