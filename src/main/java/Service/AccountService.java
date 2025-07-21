package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO; // Initialize

    // Each accountservice will have it's own DAO object
    public AccountService(AccountDAO dao) {
        this.accountDAO = dao; // Add DAO object to the Service class
    }

    // Method to handle user registeration
    public Account register(Account account) {
        // Validate inputs
        int returnFlag = 0;
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            returnFlag = 1;
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            returnFlag = 1;
        }

        // Check if username already exists
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            returnFlag = 1;
        }
        // 
        if (returnFlag == 1){
            return null; 
            // It is important to avoid timing attacks when creating and evaluating Users
            // We don't want to give potential attackers any idea on how our backend is working.
            // So It's a good idea to avoid early returns. 
        }

        // Proceed to save the new account
        return accountDAO.createAccount(account);
    }


    // Method to handle login
    public Account login(Account credentials) {
        int returnFlag = 0;
        Account existing = accountDAO.getAccountByUsername(credentials.getUsername());
        if (existing != null && existing.getPassword().equals(credentials.getPassword())) {
            // Check the existing account exists and then check if the password matches
            returnFlag = 1;
    }
    if (returnFlag == 1){
        // It is important to avoid timing attacks when evaluating Users
        // We don't want to give potential attackers any idea on how our backend is working.
        // So It's a good idea to avoid early returns. 
        
        return existing;
    }

    return null;
}
}
