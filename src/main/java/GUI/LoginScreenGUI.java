package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreenGUI extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;
    private JLabel titleLabel;
    private Image backgroundImage;

    private static final Color BUTTON_BLUE = new Color(0x57, 0xA6, 0xFF);
    private static final Color TEXT_WHITE = Color.WHITE;

    public LoginScreenGUI() {
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
        gbc.insets = new Insets(20, 20, 20, 20);
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
        gbc.gridwidth = 2;
        mainPanel.add(usernameField, gbc);

        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(BUTTON_BLUE);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                if(username.isEmpty() || username.length() > 16){
                    JOptionPane.showMessageDialog(LoginScreenGUI.this,
                            "Invalid Username (Max 16 chars)",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                openClientGUI(username);
            }
        });

        // Make window resizable and components stay centered
        setMinimumSize(new Dimension(400, 300));
    }

    private void openClientGUI(String username){
        try {
            GUI.ClientGUI clientGUI = new GUI.ClientGUI(username);
            clientGUI.setVisible(true);
            dispose();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
