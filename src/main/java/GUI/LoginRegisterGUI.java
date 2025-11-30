package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class LoginRegisterGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;

    public LoginRegisterGUI() {
        setTitle("Multiplayer Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(139, 0, 0));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 240, 200));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginUsernameField = new JTextField(15);
        loginUsernameField.setBorder(new LineBorder(new Color(139, 0, 0), 2));
        formPanel.add(loginUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPasswordField = new JPasswordField(15);
        loginPasswordField.setBorder(new LineBorder(new Color(139, 0, 0), 2));
        formPanel.add(loginPasswordField, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 215, 0));

        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        JButton switchButton = createStyledButton("Register");
        switchButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        buttonPanel.add(loginButton);
        buttonPanel.add(switchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0));

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(139, 0, 0));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 240, 200));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        registerUsernameField = new JTextField(15);
        registerUsernameField.setBorder(new LineBorder(new Color(139, 0, 0), 2));
        formPanel.add(registerUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        registerPasswordField = new JPasswordField(15);
        registerPasswordField.setBorder(new LineBorder(new Color(139, 0, 0), 2));
        formPanel.add(registerPasswordField, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 215, 0));

        JButton registerButton = createStyledButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        JButton switchButton = createStyledButton("Login");
        switchButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        buttonPanel.add(registerButton);
        buttonPanel.add(switchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(new Color(255, 215, 0));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 40));

        button.setBackground(new Color(139, 0, 0));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(220, 20, 60)); }
            public void mouseExited(MouseEvent e) { button.setBackground(new Color(139, 0, 0)); }
        });

        return button;
    }

    private void handleLogin() {
        String username = loginUsernameField.getText();
        char[] password = loginPasswordField.getPassword();

        if(username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: connect to backend for login
        JOptionPane.showMessageDialog(this, "Login successful for user: " + username);
    }

    private void handleRegister() {
        String username = registerUsernameField.getText();
        char[] password = registerPasswordField.getPassword();

        if(username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: connect to backend for registration
        JOptionPane.showMessageDialog(this, "Registration successful for user: " + username);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginRegisterGUI::new);
    }
}
