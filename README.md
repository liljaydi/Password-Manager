# Password Manager

A terminal-based password manager to store and manage your account credentials, written in Java.
Built from scratch as a first project — structured around OOP with inheritance and package separation.
See CHANGELOG.md for the full history.

---

## Features

- **Three account types** — Website, Bank, and General
- **AES encryption** — passwords, account numbers, and PINs encrypted at rest
- **Master password protection** — optional, but recommended
- **Search** — partial, case-insensitive search by site or username
- **Add / View / Delete** — full account management from the terminal
- **Duplicate detection** — flags duplicates on add *(replace behavior not fully working yet)*
- **Cancel anytime** — press Enter on any field to return to the menu

---

## Account Types

| Type | Fields |
|------|--------|
| **Website** | Site, Username, Password, URL |
| **Bank** | Bank Name, Username, Password, Account Number, PIN |
| **General** | App/Service, Username, Password |

---

## How to Run

**Requirements:** [JDK](https://www.oracle.com/java/technologies/downloads/) — Java 8 or higher

Open the project in VSCode with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) installed, then run `Main.java`.

---

## How It Works

Accounts are saved locally in the `data/` folder using a prefixed line format:

```
GENERAL<US>service<US>username<US>encryptedPassword
WEBSITE<US>site<US>username<US>encryptedPassword<US>url
BANK<US>bank<US>username<US>encryptedPassword<US>encryptedAccNum<US>encryptedPin
```

`<US>` is the ASCII Unit Separator `(U+001F)` — used instead of commas to avoid conflicts with special characters in passwords.

---

## Project Structure

```
src/
├── main/
│   └── Main.java
├── manager/
│   ├── AccountManager.java
│   ├── Crypto.java
│   ├── MasterPassword.java
│   └── Util.java
└── model/
    ├── Account.java
    ├── Website.java
    └── Bank.java
```
