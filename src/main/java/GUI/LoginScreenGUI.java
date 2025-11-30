package GUI;

import account.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreenGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton actionButton;
    private JButton switchModeButton;
    private JLabel titleLabel;
    private Image backgroundImage;

    private boolean isLoginMode = true;
    private final AccountManager accountManager;

    private static final Color BUTTON_BLUE = new Color(0x57, 0xA6, 0xFF);
    private static final Color TEXT_WHITE = Color.WHITE;

    public LoginScreenGUI() {
        accountManager = new AccountManager();

        setTitle("Game Lobby Login");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load background image
        java.net.URL imageLocation = getClass().getResource("/login_background.jpg");
        if (imageLocation != null) {
            backgroundImage = new ImageIcon(imageLocation).getImage();
        } else {
            backgroundImage = null;
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        titleLabel = new JLabel("GAME LOBBY", SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username field
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        // Action button (Login/Register)
        actionButton = new JButton("LOGIN");
        actionButton.setFont(new Font("Arial", Font.BOLD, 18));
        actionButton.setBackground(BUTTON_BLUE);
        actionButton.setForeground(Color.BLACK);
        actionButton.setFocusPainted(false);
        gbc.gridy = 3;
        mainPanel.add(actionButton, gbc);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAction();
            }
        });

        // Switch mode button
        switchModeButton = new JButton("Switch to Register");
        switchModeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        switchModeButton.setForeground(TEXT_WHITE);
        switchModeButton.setContentAreaFilled(false);
        switchModeButton.setBorderPainted(false);
        gbc.gridy = 4;
        mainPanel.add(switchModeButton, gbc);

        switchModeButton.addActionListener(e -> switchMode());

        // Make window resizable and components stay centered
        setMinimumSize(new Dimension(400, 300));
    }

    private void handleAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || username.length() > 16 || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isLoginMode) {
            if (accountManager.validateLogin(username, password)) {
                openClientGUI(username);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (accountManager.registerAccount(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                switchMode();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void switchMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            actionButton.setText("LOGIN");
            switchModeButton.setText("Switch to Register");
        } else {
            actionButton.setText("REGISTER");
            switchModeButton.setText("Switch to Login");
        }
    }

    private void openClientGUI(String username) {
        try {
            GUI.ClientGUI clientGUI = new GUI.ClientGUI(username);
            clientGUI.setVisible(true);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
