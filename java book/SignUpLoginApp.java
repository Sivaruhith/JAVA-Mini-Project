import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUpLoginApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel, signupPanel, loginPanel;
    private JTextField signupUsername, loginUsername;
    private JPasswordField signupPassword, loginPassword;
    private JButton signupButton, loginButton, switchToLoginButton, switchToSignupButton;

    public SignUpLoginApp() {
        setTitle("Sign Up and Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupSignupPanel();
        setupLoginPanel();

        mainPanel.add(signupPanel, "Signup");
        mainPanel.add(loginPanel, "Login");

        add(mainPanel);
        cardLayout.show(mainPanel, "Signup");

        setVisible(true);
    }

    private void setupSignupPanel() {
        signupPanel = new ImagePanel("360_F_119115529_mEnw3lGpLdlDkfLgRcVSbFRuVl6sMDty.jpg"); // Path to signup background image
        signupPanel.setLayout(new GridBagLayout());

        JLabel signupLabel = new JLabel("Sign Up");
        signupLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signupLabel.setForeground(Color.WHITE);

        signupUsername = new JTextField(15);
        signupPassword = new JPasswordField(15);
        signupButton = new JButton("Sign Up");
        switchToLoginButton = new JButton("Already have an account? Login");

        setupButton(signupButton);
        setupButton(switchToLoginButton);

        signupButton.addActionListener(e -> signUp());
        switchToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        signupPanel.add(signupLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        signupPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        signupPanel.add(signupUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        signupPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        signupPanel.add(signupPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        signupPanel.add(signupButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        signupPanel.add(switchToLoginButton, gbc);
    }

    private void setupLoginPanel() {
        loginPanel = new ImagePanel("images.jpg"); // Path to login background image
        loginPanel.setLayout(new GridBagLayout());

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setForeground(Color.WHITE);

        loginUsername = new JTextField(15);
        loginPassword = new JPasswordField(15);
        loginButton = new JButton("Login");
        switchToSignupButton = new JButton("Don't have an account? Sign Up");

        setupButton(loginButton);
        setupButton(switchToSignupButton);

        loginButton.addActionListener(e -> login());
        switchToSignupButton.addActionListener(e -> cardLayout.show(mainPanel, "Signup"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(loginLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(loginUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(loginPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        loginPanel.add(switchToSignupButton, gbc);
    }

    private void setupButton(JButton button) {
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 140, 0));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 215, 0));
            }
        });
    }

    private void signUp() {
        String username = signupUsername.getText();
        String password = new String(signupPassword.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/logininfo", "root", "Ruhith@12115");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sign Up Successful! Please login.");
            cardLayout.show(mainPanel, "Login");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void login() {
        String username = loginUsername.getText();
        String password = new String(loginPassword.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/logininfo", "root", "Ruhith@12115");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                LibraryManagement LibraryManagement = new LibraryManagement();  
                LibraryManagement.setVisible(true);                 
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials, please try again.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpLoginApp::new);
    }
}

// Custom JPanel for Image Background
class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}


