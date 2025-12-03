package GUI;

import network.Message;
import network.MessageListener;
import network.MyStompClient;
import util.Utilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame implements MessageListener 
{
    private JPanel connectedUsersPanel, messagePanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane messagePanelScrollPane;
    private Image backgroundImage;

    private static final Color CSUDH_RED = new Color(0xBA, 0x0C, 0x2F);
    private static final Color CSUDH_GOLD = new Color(0xFF, 0xC7, 0x2C);

    public ClientGUI(String username) throws ExecutionException, InterruptedException 
    {
        super("User: " + username);
        this.username = username;

        java.net.URL location = getClass().getResource("/lobby_background.jpg");
        if(location != null)
        {
            backgroundImage = new ImageIcon(location).getImage();
        }

        myStompClient = new MyStompClient(this, username);

        setSize(1200, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this,
                        "Do you really want to leave?", "Exit", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION)
                {
                    myStompClient.disconnectUser(username);
                    dispose();
                }
            }
        });

        addGuiComponents();
    }

    private void addGuiComponents()
    {
        addConnectedUsersComponents();
        addChatComponents();
    }

    private void addConnectedUsersComponents()
    {
        connectedUsersPanel = new JPanel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                g.setColor(new Color(0,0,0,200));
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setOpaque(false);
        connectedUsersPanel.setPreferredSize(new Dimension(250, getHeight()));
        connectedUsersPanel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel connectedUsersLabel = new JLabel("Players");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 20));
        connectedUsersLabel.setForeground(Color.WHITE);
        connectedUsersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectedUsersPanel.add(connectedUsersLabel);
        connectedUsersPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        getContentPane().add(connectedUsersPanel, BorderLayout.WEST);
    }


    private void addChatComponents()
    {
        JPanel chatPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(backgroundImage != null){
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(CSUDH_RED);
                    g.fillRect(0,0,getWidth(),getHeight());
                }
            }
        };
        chatPanel.setOpaque(true);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);

        messagePanelScrollPane = new JScrollPane(messagePanel);
        messagePanelScrollPane.setOpaque(false);
        messagePanelScrollPane.getViewport().setOpaque(false);
        messagePanelScrollPane.setBorder(null);
        messagePanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanelScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chatPanel.add(messagePanelScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10,10,10,10));
        inputPanel.setOpaque(false);

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setBackground(CSUDH_RED.darker());
        inputField.setForeground(CSUDH_GOLD);
        inputField.setCaretColor(CSUDH_GOLD);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CSUDH_GOLD,2),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));
        inputField.addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyTyped(KeyEvent e) 
            {
                if(e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    String input = inputField.getText().trim();
                    if(input.isEmpty())
                    {
                    	return;
                    }
                    
                    inputField.setText("");
                    myStompClient.sendMessage(new Message(username, input));
                }
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        getContentPane().add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message)
    {
        JPanel chatMessage = new JPanel();
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
        chatMessage.setOpaque(false);
        chatMessage.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JLabel usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 16));
        usernameLabel.setForeground(CSUDH_RED);
        chatMessage.add(usernameLabel);

        JLabel messageLabel = new JLabel("<html><body style='width:" + (0.6 * getWidth()) + "px'>" +
                message.getMessage() + "</body></html>");
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        messageLabel.setForeground(CSUDH_GOLD);
        chatMessage.add(messageLabel);

        chatMessage.add(Box.createRigidArea(new Dimension(0,5)));
        
        return chatMessage;
    }


    @Override
    public void onMessageReceive(Message message)
    {
        if(messagePanel == null)
        {
        	return;
        }
        
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();
        messagePanelScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsersUpdated(List<String> users) 
    {
        if(connectedUsersPanel == null)
        {
        	return;
        }

        if(connectedUsersPanel.getComponents().length >= 2)
        {
            connectedUsersPanel.remove(1);
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setOpaque(false);

        for(String user : users)
        {
            JLabel userLabel = new JLabel(user);
            userLabel.setFont(new Font("Inter", Font.BOLD, 16));
            userLabel.setForeground(CSUDH_RED);
            userListPanel.add(userLabel);
            userListPanel.add(Box.createRigidArea(new Dimension(0,5)));
        }

        connectedUsersPanel.add(userListPanel);
        revalidate();
        repaint();
    }


    private void updateMessageSize()
    {
        if(messagePanel == null)
        {
        	return;
        }
        
        for(Component component : messagePanel.getComponents())
        {
            if(component instanceof JPanel)
            {
                JPanel chatMessage = (JPanel) component;
                if(chatMessage.getComponentCount() > 1 && chatMessage.getComponent(1) instanceof JLabel)
                {
                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
                    String text = messageLabel.getText().replaceAll("<html><body style='width:\\d+\\.0*px'>","")
                            .replaceAll("</body></html>","");
                    messageLabel.setText("<html><body style='width:" + (0.6 * getWidth()) + "px'>" + text + "</body></html>");
                }
            }
        }
    }
}
