package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MasterPassword {

	File passwordFile;
	Scanner sc;
	String masterPassword; 
	
	public MasterPassword(File passwordFile, Scanner sc) {
		this.passwordFile = passwordFile;
		this.sc = sc;
	}
	
	public void inputPassword() {
		
		if (passwordFile.exists() && (passwordFile.length() != 0)) {
			if(checkPassword("Enter master password: ")) {
				System.out.println("Access granted!");
				return;
			} else {
				System.out.println("Access denied");
				System.out.println("Exiting...");
				System.exit(0);
			}
		}
		
	}
	
	private boolean checkPassword(String prompt) {
		try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
			String storedPassword = Crypto.decrypt(br.readLine());
			
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
	
	public void masterPasswordSettings() {
		/**This code below are for the master password feature
		 * allows user to modify security 
		 * 
		 * set password to open the application
		 * or disable
		 */
		if (!passwordFile.exists() || (passwordFile.length() == 0)) {
			
			while (true) {
				System.out.println("Settings");
				System.out.println("[1] Set master password");
				System.out.println("[0] Back");
				System.out.print("\nSelect (0 or 1) ");
				int settingsOption = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc); 
				
				if (settingsOption == 1) {
					System.out.println("\nNote: Enabling a master password will require you");
					System.out.println("to enter it every time the app starts.");
					System.out.print("\n[1] Continue [0] Exit ");
					int optionVerify = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc); 
					//System.out.println();
					
					if (optionVerify == 1) {
						if (setPassword()) return;
						else continue;
					}
					else if (optionVerify == 0) return;
					else System.out.println("Something went wrong");
				} 
				else if (settingsOption == 0) return;
				else System.out.println("Something went wrong");
			}
		
		} else {
			
			while (true) {
				
				System.out.println("Settings");
				System.out.println("[1] Change master password");
				System.out.println("[2] Disable master password");
				System.out.println("[0] Back");
				System.out.print("\nSelect (0-2) ");
				int settingsOption = Util.validifyInput(0, 2, "> _", "Invalid input! [input must be 0-2]", sc); 

				if (settingsOption == 1) {
					System.out.println();
					if (!checkPassword("Enter current master password : ")) {
						System.out.println("\n[No changes were made]");
						return;
					} else {
						if (changePassword()) return;
						else continue;
					}
				}
				else if (settingsOption == 2) {
					System.out.println();
					if (checkPassword("Enter current master password : ")) {
						try (FileWriter fw = new FileWriter(passwordFile)) {
							System.out.println("\n[Master password disabled]");
							fw.close();
							return;
						} catch (IOException e) {
							System.out.println("\nFailed to disable master password");
						}
					} else {
						System.out.println("\n[Master password not disabled]");
						return;
					} 
				}
				else if (settingsOption == 0) return;
				else System.out.println("Something went wrong");
			}
		}
		
	}
	
	public boolean setPassword() {
		
		try (FileWriter fw = new FileWriter(passwordFile)) {
			
			boolean masterPassword_not_set = true;
			
			while (masterPassword_not_set) {
				System.out.print("\nSet master password     : ");
				String masterPassword = sc.nextLine();
			
				if (masterPassword.isEmpty()) { // ← stops blank enter
			        System.out.println("\n[Master password cannot be empty]\n");
			        return false;
			    }
				
				System.out.print("Confirm master password : ");
				String confirmMasterPassword = sc.nextLine();
			
				if (masterPassword.equals(confirmMasterPassword)) {
					fw.write(Crypto.encrypt(masterPassword));
					System.out.println("\nMaster password set successfully");
					masterPassword_not_set = false;
				} else {
					System.out.println("\n[Password do not match]");
					System.out.print("\n[1] Try again [0] Back ");
					int option = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
					
					if (option == 1) continue;
					else if (option == 0) {
						System.out.println("\n[Master password has not been set]");
						return true;
					}
					else {
						System.out.println("\nSomething went wrong");
						return false;
					}	
				}
			}
			
			return true;
			
		} catch (IOException e) {
			System.out.println("Error, unable to write file");
			return false;
		}
		
	}
	
	public boolean changePassword() {
		boolean masterPassword_not_set = true;
		while (masterPassword_not_set) {
			System.out.print("Set new master password       : ");
			String masterPassword = sc.nextLine();
			
			if (masterPassword.isEmpty()) { // ← stops blank enter
		        System.out.println("\n[Master password cannot be empty]\n");
		        
		        System.out.print("[1] Try again [0] Exit ");
		        
		        int option = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
		        
		        if (option == 1) {
		        	System.out.println();
		        	continue;
		        } else return true;
		        
		    }
			
			String line = "";
			try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
				line = Crypto.decrypt(br.readLine());
			} catch (IOException e) {
				System.out.println("Error, unable to read file\n");
			}
			
			if (line.equals(masterPassword)) {
				System.out.println("New password cannot be the same as your current password\n");
				System.out.print("[1] Try again [0] Exit ");
				
				int option = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
				
				if (option == 1) {
					System.out.println();
					continue;
				}
				else if (option == 0) return true;
				else {
					System.out.println("\nSomething went wrong");
					return false;
				}
			
				
				
			} else {
				System.out.print("Confirm new master password   : ");
				String confirmMasterPassword = sc.nextLine();
			
				if (masterPassword.equals(confirmMasterPassword)) {
					try (FileWriter fw = new FileWriter(passwordFile)) {
						fw.write(Crypto.encrypt(masterPassword));
						System.out.println("\n[Master password changed]");
						masterPassword_not_set = false;
					} catch (IOException e) {
						System.out.println("Error, unable to write file");
					}
				} else {
					System.out.println("\n[Password do not match]");
					System.out.print("\n[1] Try again [0] Back ");
					int option = Util.validifyInput(0, 1, "> _", "Invalid input! [input must be 0 or 1]", sc);
				
					if (option == 1) {
						System.out.println();
						continue;
					}
					else if (option == 0) {
						System.out.println("\n[No changes where made]");
						return true;
					}
					else {
						System.out.println("\n[Something went wrong]");	
						return false;
					}
				}
			}	
		}
		return true;
	}
	
	
}
