package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import manager.AccountManager;
import manager.Util;
import model.Account;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		//below is for creating the "data" folder that contains "account.txt" and "masterPassword.txt"
		File passwordFile = new File(System.getProperty("user.dir") + "/data/masterPassword.txt");
		File accountFile  = new File(System.getProperty("user.dir") + "/data/account.txt");
		passwordFile.getParentFile().mkdirs();
        accountFile.getParentFile().mkdirs();
		
        //arrays to store all accounts objects
		ArrayList<Account> accounts = new ArrayList<>();
		//this class is for managing accounts such as adding new accounts, delete, etc.
		AccountManager am = new AccountManager(sc, accountFile, accounts);
		
		System.out.println("    ====================");
		System.out.println("    |                  |");
		System.out.println("    | Password Manager |");
		System.out.println("    |                  |");
		System.out.println("    ====================\n");
		
		System.out.println("Welcome to Password Manager!");
		
		System.out.println();
		
		am.loadAccount();
		
		int option;
		do {
			
			System.out.println("=========== Menu ===========");
			System.out.println("| [1] Add Account          |");
			System.out.println("| [2] Search Account       |");
			System.out.println("| [3] View all Account     |");
			System.out.println("| [4] Delete Account       |");
			System.out.println("| [5] Settings             |");
			System.out.println("| [0] Exit                 |");
			System.out.println("============================");
		    System.out.print("Select (0-5) ");
		    option = Util.validifyInput(0, 5, "> _", "Invalid input! [input must be 1-6]", sc);
		    System.out.println();
		    
			switch (option) {
				case 1:
					am.addAccountOption();
					System.out.println();
					break;
				case 2:
					am.searchAccountMenu();
				    System.out.println();
					break;
				case 3:
					am.displayAccount();
					System.out.println();
					break;
				case 4:
					am.deleteAccountMenu();
					System.out.println();
					break;
				case 5:
					//mpm.masterPasswordSettings();
					System.out.println("This feature is still under development.\n");
					System.out.print("Press enter to go back to menu...");
					sc.nextLine();
					System.out.println();
					break;
				case 0:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Something went wrong\n");
			}
		}  while (option != 0);
		
	}

}
