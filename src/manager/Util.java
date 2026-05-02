package manager;

import java.util.ArrayList;
import java.util.Scanner;

import model.Account;

public class Util {
	
	//I added canAdd because of replace account or not... 
	//This 
	static boolean canAdd(Account newAccount, ArrayList<Account> accounts, Scanner sc) {
		if (newAccount.isDuplicate(accounts)) {
			System.out.print("\nAccount already exists. Replace it? (y/n): ");
			String choice = sc.nextLine().trim().toLowerCase();
			
			if (choice.equals("y")) {
				//to be added
				System.out.println("\nNOTE: This feature is still under development.");
				System.out.println("Duplicate account will still appear in the list.\n");
				return true;
			} else {
				return false;
			}
		} else return true;
	}
	
	static void displayList(ArrayList<Account> list) {
		int i = 1;
		for (Account acc : list) {
			System.out.print("[" + i + "] ");
			acc.displayFormat();
			i++;
		}
	}
	
	public static int validifyInput(int range1, 
							 int range2, 
							 String prompt1, 
							 String prompt2, 
							 Scanner sc) {
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
	
	public static boolean validifyVariousInput(String option, int range1, int range2) {
		try {
			int num = Integer.parseInt(option);
			if (num < range1 || num > range2) return false;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}	
	}
	
}
