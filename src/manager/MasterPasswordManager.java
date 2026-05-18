package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MasterPasswordManager {

	private File passwordFile;
	private Scanner sc;
	private Utility util;
	
	public MasterPasswordManager(File passwordFile, Scanner sc, Utility util) {
		this.passwordFile = passwordFile;
		this.sc = sc;
		this.util = util;
	}
	
	
	public void masterPasswordSettings() {
		/**This code below are for the master password feature
		 * allows user to modify security 
		 * 
		 * set password to open the application
		 * or disable
		 */
		if (!passwordFile.exists() || (passwordFile.length() == 0)) {
			System.out.println("Settings");
			System.out.println("[1] Set master password");
			System.out.println("[0] Back");
			int settingsOption = util.validifyInput(0, 1, "Option: ", "Invalid input! [input must be 0 or 1]"); 
			System.out.println();
			
			if (settingsOption == 1) {
				System.out.println("Note: Enabling a master password will require you");
				System.out.println("to enter it every time the app starts.");
				System.out.println("[1] Continue");
				System.out.println("[0] Back");
				int optionVerify = util.validifyInput(0, 1, "Option: ", "Invalid input! [input must be 0 or 1]"); 
				System.out.println();
				
				if (optionVerify == 1) setPassword();
				else if (optionVerify == 0) return;
				else System.out.println("Something went wrong");
			} 
			else if (settingsOption == 0) return;
			else System.out.println("Something went wrong");
		} else {
			System.out.println("Settings");
			System.out.println("[1] Change master password");
			System.out.println("[2] Disable master password");
			System.out.println("[0] Back");
			int settingsOption = util.validifyInput(0, 2, "Option: ", "Invalid input! [input must be 0-2]"); 

			if (settingsOption == 1) {
				if (!checkPassword("Enter current master password: ")) {
					System.out.println("No changes were made.\n");
				} else changePassword();
			}
			else if (settingsOption == 2) {
				if (checkPassword("Enter current master password: ")) {
					try (FileWriter fw = new FileWriter(passwordFile)) {
						System.out.println("Master password disabled\n");
						fw.close();
					} catch (IOException e) {
						System.out.println("Failed to disable master password\n");
					}
				} else {
					System.out.println("Master password not disabled\n");
				} 
			}
			else if (settingsOption == 0) {
				System.out.println();
				return;
			}
			else System.out.println("Something went wrong");
		}
		
	}
	
	public boolean checkPassword(String prompt) {
		try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
			String storedPassword = util.decrypt(br.readLine());
			
			int count = 1;
			while (true) {
				System.out.print(prompt);
				String masterPassword = sc.nextLine();
				
				if (masterPassword.equals(storedPassword)) {
					return true;
				} else {
					if (count == 3) System.out.println("Incorrect password.\n");
					else System.out.println("Incorrect password. Please try again\n");
				}
				if (count == 3) {
					System.out.println("Too many failed attempts."); // runs when 3 incorrect password attempts...
					return false;
				}
				count++;
			}
		} catch (IOException e) {
			System.out.println("Error, unable to read master password file");
			return false;
		}
	}
	
	public void setPassword() {
		
		try (FileWriter fw = new FileWriter(passwordFile)) {
			
			boolean masterPassword_not_set = true;
			
			while (masterPassword_not_set) {
				System.out.print("Set master password: ");
				String masterPassword = sc.nextLine();
			
				if (masterPassword.isEmpty()) { // ← stops blank enter
			        System.out.println("Master password cannot be empty\n");
			        return;
			    }
				
				System.out.print("Confirm master password: ");
				String confirmMasterPassword = sc.nextLine();
			
				if (masterPassword.equals(confirmMasterPassword)) {
					fw.write(util.encrypt(masterPassword));
					System.out.println("\nMaster password set successfully\n");
					masterPassword_not_set = false;
				} else {
					System.out.println("\nPassword do not match.");
					System.out.println("[1] Try again");
					System.out.println("[0] Back");
					int option = util.validifyInput(0, 1, "Option: ", "Invalid input! [input must be 0 or 1]");
					System.out.println();
					
					if (option == 1) continue;
					else if (option == 0) {
						System.out.println("Master password has not been set.\n");
						return;
					}
					else {
						System.out.println("Something went wrong\n");
						return;
					}	
				}
			}
		} catch (IOException e) {
			System.out.println("Error, unable to write file");
		}
		
	}

	public void changePassword() {
		boolean masterPassword_not_set = true;
		while (masterPassword_not_set) {
			System.out.print("Set new master password: ");
			String masterPassword = sc.nextLine();
			
			String line = "";
			try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
				line = util.decrypt(br.readLine());
			} catch (IOException e) {
				System.out.println("Error, unable to read file\n");
			}
			
			if (line.equals(masterPassword)) {
				System.out.println("New password cannot be the same as your current password\n");
			} else {
				System.out.print("Confirm new master password: ");
				String confirmMasterPassword = sc.nextLine();
			
				if (masterPassword.equals(confirmMasterPassword)) {
					try (FileWriter fw = new FileWriter(passwordFile)) {
						fw.write(util.encrypt(masterPassword));
						System.out.println("\nMaster password changed\n");
						masterPassword_not_set = false;
					} catch (IOException e) {
						System.out.println("Error, unable to write file");
					}
				} else {
					System.out.println("\nPassword do not match.");
					System.out.println("[1] Try again");
					System.out.println("[0] Back");
					int option = util.validifyInput(0, 1, "Option: ", "Invalid input! [input must be 0 or 1]");
					System.out.println();
				
					if (option == 1) continue;
					else if (option == 0) {
						System.out.println("No changes where made.\n");
						return;
					}
					else System.out.println("Something went wrong\n");	
				}
			}	
		}
	}

}
