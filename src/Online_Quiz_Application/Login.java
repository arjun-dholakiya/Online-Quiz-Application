package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.*;

/**
 * Login class handles user authentication for Admin and User roles.
 * Users can Login, navigate to registration, or exit the application.
 */
public class Login extends JFrame implements ActionListener {

    // UI Components
    JTextField utext;
    JPasswordField ptext;
    Choice login_choice;
    JButton login_btn, cancel_btn, signup_btn;

       Login() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Application logo
        ImageIcon loginIcon = new ImageIcon(ClassLoader.getSystemResource("Images/login_logo.png"));
        Image scaledImage = loginIcon.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        JLabel loginLabel = new JLabel(new ImageIcon(scaledImage));
        loginLabel.setBounds(20, 5, 250, 250);
        add(loginLabel);

        // Username label and Text-field
        JLabel uname = new JLabel("Enter Username");
        uname.setBounds(330, 40, 100, 20);
        add(uname);

        utext = new JTextField();
        utext.setBounds(450, 40, 150, 20);
        add(utext);

        // Password label and password field
        JLabel password = new JLabel("Enter Password");
        password.setBounds(330, 80, 100, 20);
        add(password);

        ptext = new JPasswordField();
        ptext.setBounds(450, 80, 150, 20);
        add(ptext);

        // Login role selection
        JLabel login = new JLabel("Login As");
        login.setBounds(330, 120, 100, 20);
        add(login);

        login_choice = new Choice();
        login_choice.add("Admin");
        login_choice.add("User");
        login_choice.setBounds(450, 120, 150, 20);
        add(login_choice);

        // Login button
        login_btn = new JButton("Login");
        login_btn.setBounds(330, 182, 100, 20);
        login_btn.setBackground(new Color(100, 149, 237));
        login_btn.setForeground(Color.WHITE);
        login_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login_btn.addActionListener(this);
        add(login_btn);

        // Register button
        signup_btn = new JButton("Register");
        signup_btn.setBounds(495, 182, 100, 20);
        signup_btn.setBackground(new Color(60, 179, 113));
        signup_btn.setForeground(Color.WHITE);
        signup_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signup_btn.addActionListener(this);
        add(signup_btn);

        // Cancel button
        cancel_btn = new JButton("Cancel");
        cancel_btn.setBounds(410, 220, 100, 20);
        cancel_btn.setBackground(Color.RED);
        cancel_btn.setForeground(Color.WHITE);
        cancel_btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancel_btn.addActionListener(this);
        add(cancel_btn);

        // JFrame settings
        setSize(640, 300);
        setLocation(400, 200);
        setTitle("Login");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Login();
    }

    // Handles button click events for login, register, and cancel.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login_btn) {
            handleLogin();
        } else if (e.getSource() == signup_btn) {
            setVisible(false);
            new Register();
        } else if (e.getSource() == cancel_btn) {
            setVisible(false);
        }
    }

    /* Handles user authentication by verifying username, password, and role. */
    private void handleLogin() {
        String username = utext.getText();
        String password = new String(ptext.getPassword());
        String role = login_choice.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
        } else {
            try {
                Database_Connection connection = new Database_Connection();
                String hashedPassword = hashPassword(password);

                String query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + hashedPassword + "' AND role='" + role + "'";
                ResultSet rs = connection.statement.executeQuery(query);

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    JOptionPane.showMessageDialog(null, "Login Successful as " + role);
                    setVisible(false);

                    // Redirect to respective homepage
                    if (role.equals("Admin")) {
                        new AdminHomepage();
                    } else if (role.equals("User")) {
                        new UserHomepage(userId);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // For Hash Password
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));

            // Convert bytes to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
