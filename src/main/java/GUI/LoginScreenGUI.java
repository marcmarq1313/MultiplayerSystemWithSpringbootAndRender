package GUI;

import account.AccountManager;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;

public class LoginScreenGUI extends JFrame 
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField winsField;
    private JTextField lossesField;
    private JButton actionButton;
    private JButton switchModeButton;
    private JLabel titleLabel;
    private Image backgroundImage;

    private boolean isLoginMode = true;
    private final AccountManager accountManager;

    private static final Color BUTTON_BLUE = new Color(0x57, 0xA6, 0xFF);
    private static final Color TEXT_WHITE = Color.WHITE;

    public LoginScreenGUI() 
    {
        accountManager = new AccountManager();

        setTitle("Game Lobby Login");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        java.net.URL imageLocation = getClass().getResource("/login_background.jpg");
        backgroundImage = (imageLocation != null) ? new ImageIcon(imageLocation).getImage() : null;

        JPanel mainPanel = new JPanel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                if(backgroundImage != null)
                {
                	g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                else 
                { 
                	g.setColor(Color.BLACK); g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        titleLabel = new JLabel("GAME LOBBY", SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 36));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

       
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setUI(new JTextFieldHintUI("Username", Color.GRAY));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);


        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setText("Password");
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() 
        {
            public void focusGained(java.awt.event.FocusEvent e) 
            {
                if(String.valueOf(passwordField.getPassword()).equals("Password"))
                {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('*');
                }
            }
            public void focusLost(java.awt.event.FocusEvent e)
            {
                if(passwordField.getPassword().length == 0)
                {
                    passwordField.setText("Password");
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        winsField = new JTextField();
        winsField.setFont(new Font("Arial", Font.PLAIN, 18));
        winsField.setUI(new JTextFieldHintUI("Wins", Color.GRAY));
        winsField.setHorizontalAlignment(JTextField.CENTER);
        winsField.setVisible(false);
        gbc.gridy = 3;
        mainPanel.add(winsField, gbc);

        lossesField = new JTextField();
        lossesField.setFont(new Font("Arial", Font.PLAIN, 18));
        lossesField.setUI(new JTextFieldHintUI("Losses", Color.GRAY));
        lossesField.setHorizontalAlignment(JTextField.CENTER);
        lossesField.setVisible(false);
        gbc.gridy = 4;
        mainPanel.add(lossesField, gbc);

        actionButton = new JButton("LOGIN");
        actionButton.setFont(new Font("Arial", Font.BOLD, 18));
        actionButton.setBackground(BUTTON_BLUE);
        actionButton.setForeground(Color.BLACK);
        actionButton.setFocusPainted(false);
        gbc.gridy = 5;
        mainPanel.add(actionButton, gbc);
        actionButton.addActionListener(e -> handleAction());

        switchModeButton = new JButton("Switch to Register");
        switchModeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        switchModeButton.setForeground(TEXT_WHITE);
        switchModeButton.setContentAreaFilled(false);
        switchModeButton.setBorderPainted(false);
        gbc.gridy = 6;
        mainPanel.add(switchModeButton, gbc);
        switchModeButton.addActionListener(e -> switchMode());

        setMinimumSize(new Dimension(400, 300));
    }

    private void handleAction()
    {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        if(username.isEmpty() || username.length() > 16 || password.isEmpty() || password.equals("Password")) 
        {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(isLoginMode) 
        {
            if(accountManager.validateLogin(username, password))
            {
                openClientGUI(username);
            }else 
            {
                JOptionPane.showMessageDialog(this, "Incorrect username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            int wins, losses;
            
            try 
            {
                wins = Integer.parseInt(winsField.getText().trim());
                losses = Integer.parseInt(lossesField.getText().trim());
                if (wins < 0 || losses < 0) 
                {
                	throw new NumberFormatException();
               	}
            } 
            catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Wins and losses must be non-negative integers", "Error", JOptionPane.ERROR_MESSAGE);
                
                return;
            }

            if(accountManager.registerAccount(username, password, wins, losses)) 
            {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                switchMode();
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void switchMode() 
    {
        isLoginMode = !isLoginMode;
        winsField.setVisible(!isLoginMode);
        lossesField.setVisible(!isLoginMode);

        if(isLoginMode) 
        {
            actionButton.setText("LOGIN");
            switchModeButton.setText("Switch to Register");
        }
        else 
        {
            actionButton.setText("REGISTER");
            switchModeButton.setText("Switch to Login");
        }

        revalidate();
        repaint();
    }

    private void openClientGUI(String username)
    {
        try 
        {
            GUI.ClientGUI clientGUI = new GUI.ClientGUI(username);
            clientGUI.setVisible(true);
            dispose();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}

class JTextFieldHintUI extends javax.swing.plaf.basic.BasicTextFieldUI implements java.awt.event.FocusListener 
{
    private String hint;
    private Color hintColor;

    public JTextFieldHintUI(String hint, Color hintColor) 
    {
        this.hint = hint;
        this.hintColor = hintColor;
    }

    @Override
    protected void paintSafely(Graphics g) 
    {
        super.paintSafely(g);
        JTextComponent comp = getComponent();
        if(comp.getText().isEmpty() && !comp.hasFocus()) 
        {
            g.setColor(hintColor);
            FontMetrics fm = g.getFontMetrics();
            int x = (comp.getWidth() - fm.stringWidth(hint)) / 2;
            int y = (comp.getHeight() + fm.getAscent()) / 2 - 2;
            g.drawString(hint, x, y);
        }
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent e) 
    { 
    	getComponent().repaint(); 
    }
    
    @Override
    public void focusLost(java.awt.event.FocusEvent e) 
    { 
    	getComponent().repaint();
    }
    
    @Override
    protected void installListeners()
    {
    	super.installListeners(); getComponent().addFocusListener(this);
    }
    
    @Override
    protected void uninstallListeners()
    {
    	super.uninstallListeners(); getComponent().removeFocusListener(this);
    }
}
