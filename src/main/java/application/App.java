package application;

import javax.swing.*;
import GUI.ClientGUI;
import GUI.LoginScreenGUI;
import java.util.concurrent.ExecutionException;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // runOriginalCode();
            runNewLoginScreen(); // Uncomment to test new login
        });
    }

    private static void runOriginalCode() {
        String username = JOptionPane.showInputDialog(null,
                "Enter Username (Max: 16 Characters): ",
                "Chat Application",
                JOptionPane.QUESTION_MESSAGE);

        if (username == null || username.isEmpty() || username.length() > 16) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Username",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClientGUI clientGUI = null;
        try {
            clientGUI = new ClientGUI(username);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        clientGUI.setVisible(true);
    }

    private static void runNewLoginScreen() {
        LoginScreenGUI loginScreen = new LoginScreenGUI();
        loginScreen.setVisible(true);
    }
}
