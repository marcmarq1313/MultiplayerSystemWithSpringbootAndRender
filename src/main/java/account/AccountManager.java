package account;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private static final String ACCOUNT_FILE = "accounts.json";
    private final Map<String, UserAccount> accounts;
    private final ObjectMapper objectMapper;

    public AccountManager() {
        objectMapper = new ObjectMapper();
        accounts = loadAccounts();
    }

    private Map<String, UserAccount> loadAccounts() {
        File file = new File(ACCOUNT_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<String, UserAccount>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveAccounts() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ACCOUNT_FILE), accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerAccount(String username, String password) {
        if(accounts.containsKey(username)) return false;
        String hashedPassword = hashPassword(password);
        accounts.put(username, new UserAccount(username, hashedPassword));
        saveAccounts();
        return true;
    }

    public boolean validateLogin(String username, String password) {
        if(!accounts.containsKey(username)) return false;
        UserAccount account = accounts.get(username);
        String hashedPassword = hashPassword(password);
        return account.getHashedPassword().equals(hashedPassword);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hashBytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
