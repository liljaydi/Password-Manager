README — Password Manager V2.1.1

——————————————————————

A terminal-based password manager written in Java.
Stores, searches, and manages your saved accounts with AES encryption.

——————————————————————

FEATURES

- Add accounts (site/app, username, password)
- Search accounts by site or username (partial, case-insensitive)
- View all saved accounts
- Delete single or multiple accounts at once
- Optional master password to lock the app on startup
- Passwords are encrypted using AES before being saved to file

——————————————————————

HOW TO USE

On startup, if a master password is set, you will be prompted to enter
it before accessing the app. You have 3 attempts before access is denied.

Main Menu options:
  [1] Add Account     — saves a new account (duplicates are rejected)
  [2] Search Account  — find accounts by site or username
  [3] View All        — displays all saved accounts with decrypted passwords
  [4] Delete Account  — delete one or multiple accounts at once
  [5] Settings        — manage master password (set, change, or disable)
  [6] Exit            — close the application

——————————————————————

SETTINGS

If no master password is set:
  [1] Set master password

If a master password is already set:
  [1] Change master password
  [2] Disable master password

——————————————————————

DATA FILES

The app uses two files saved in the same folder as the project:

  account.txt        — stores all saved accounts
  masterPassword.txt — stores the encrypted master password (if set)

These files are created automatically when needed.
Do not manually edit them unless you know what you are doing.

——————————————————————

HOW ACCOUNTS ARE SAVED

Each account is saved as a single line in account.txt in this format:

  site-username-encryptedPassword

Examples:
outLOOK@davidPASSjZzrLE7KWm3f7GjSsxW95g==
goodreadsdavidharewoodImQDqQWZ0PIPAYb3Uyohfw==

The third value is the AES-encrypted, Base64-encoded password.
It is automatically decrypted when displayed inside the app.



——————————————————————

FILE STRUCTURE

PasswordManager/
│
├── main/
│   └── MenuPasswordManager.java
│       └── main()
│           ├── Initializes Scanner, ArrayLists, Files
│           ├── Displays splash screen & menu (loop)
│           └── Delegates to managers based on user option
│
├── manager/
│   ├── AccountManager.java
│   │   ├── addAccount()
│   │   ├── searchAccount()          → returns boolean, populates results[]
│   │   ├── viewAllAccount()
│   │   ├── deleteAccount()          → handles single & multi-delete from main list
│   │   └── deleteFromResults()      → multi-delete from search results
│   │
│   ├── Utility.java                 (shared helper, injected into other managers)
│   │   ├── validifyInput()          → single int input, loops until valid range
│   │   ├── validifyVariousInput()   → validates one token from a comma-split string
│   │   ├── checkDuplicate()         → checks site+username pair in array
│   │   ├── saveAccount()            → appends new account to file + array
│   │   ├── saveAccountOverwriteData() → rewrites file from array (post-delete)
│   │   ├── loadAccount()            → reads file into array on startup
│   │   ├── encrypt()                → AES + Base64
│   │   └── decrypt()                → Base64 + AES
│   │
│   └── MasterPasswordManager.java
│       ├── checkPassword()          → reads file, validates input (max 3 attempts)
│       ├── setPassword()            → writes new encrypted password to file
│       └── changePassword()         → verifies old, then overwrites with new
│
└── model/
    └── Account.java
        ├── site     : String
        ├── username : String
        ├── password : String  (stored encrypted)
        └── getters  : getSite(), getUsername(), getPassword()

Data flow summary:
accountFile (account.txt)
    ↕ load/save via Utility
ArrayList<Account> arraysOfAccountObject
    ↕ passed by reference to AccountManager + Utility
ArrayList<Account> results
    ↕ populated by searchAccount(), consumed by deleteFromResults()

passwordFile (masterPassword.txt)
    ↕ read/write via MasterPasswordManager
    └── uses Utility.encrypt() / decrypt()

——————————————————————

AUTHOR

Gyde
