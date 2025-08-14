package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;

/**
 * Register class allows new users to register as Admin or User.
 * Stores registration details securely in the database with hashed passwords.
 */
public class Register extends JFrame implements ActionListener {

    // UI Components
    Choice registerAsChoice;
    JTextField f_name, u_name, e_address;
    JPasswordField pass;
    JButton register, cancel;

        Register() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Role selection (Admin/User)
        JLabel registerAS = new JLabel("Register As");
        registerAS.setBounds(30, 50, 125, 20);
        add(registerAS);

        registerAsChoice = new Choice();
        registerAsChoice.add("Admin");
        registerAsChoice.add("User");
        registerAsChoice.setBounds(170, 50, 120, 20);
        add(registerAsChoice);

        // Full name
        JLabel full_name = new JLabel("Enter Full Name");
        full_name.setBounds(30, 110, 125, 20);
        add(full_name);

        f_name = new JTextField();
        f_name.setBounds(170, 110, 125, 20);
        add(f_name);

        // Username
        JLabel user_name = new JLabel("Enter Username");
        user_name.setBounds(30, 150, 125, 20);
        add(user_name);

        u_name = new JTextField();
        u_name.setBounds(170, 150, 125, 20);
        add(u_name);

        // Password
        JLabel password = new JLabel("Enter Password");
        password.setBounds(30, 190, 125, 20);
        add(password);

        pass = new JPasswordField();
        pass.setBounds(170, 190, 125, 20);
        add(pass);

        // Email address
        JLabel email_address = new JLabel("Enter E-Mail Address");
        email_address.setBounds(30, 230, 125, 20);
        add(email_address);

        e_address = new JTextField();
        e_address.setBounds(170, 230, 125, 20);
        add(e_address);

        // Register button
        register = new JButton("Register");
        register.setBounds(30, 280, 120, 20);
        register.setBackground(new Color(60, 179, 113));
        register.setForeground(Color.WHITE);
        register.setCursor(new Cursor(Cursor.HAND_CURSOR));
        register.addActionListener(this);
        add(register);

        // Cancel button
        cancel = new JButton("Cancel");
        cancel.setBounds(175, 280, 120, 20);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancel.addActionListener(this);
        add(cancel);

        // Registration page logo
        ImageIcon signupLogo = new ImageIcon(ClassLoader.getSystemResource("Images/signup_logo.png"));
        Image signupImg = signupLogo.getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT);
        JLabel signupLabel = new JLabel(new ImageIcon(signupImg));
        signupLabel.setBounds(320, 30, 250, 250);
        add(signupLabel);

        // JFrame settings
        setSize(600, 380);
        setLocation(500, 200);
        setTitle("Register");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Register();
    }

    // Handles button click events for registration and cancellation.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            handleRegistration();
        } else if (e.getSource() == cancel) {
            setVisible(false);
            new Login();
        }
    }

    /**
     * Validates user input and inserts new user details into the database.
     * Password is stored as a hashed value using SHA-256.
     */
    private void handleRegistration() {
        String role = registerAsChoice.getSelectedItem();
        String fullName = f_name.getText();
        String username = u_name.getText();
        String password = new String(pass.getPassword());
        String email = e_address.getText();

        // Input validation
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
        } else {
            try {
                String hashedPassword = hashPassword(password);

                Database_Connection connection = new Database_Connection();
                String query = "INSERT INTO users(full_name, username, password, email, role) VALUES('" + fullName + "','" + username + "','" + hashedPassword + "','" + email + "','" + role + "')";
                connection.statement.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Registration Successful!");
                setVisible(false);
                new Login();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Registration Failed! Please try again.");
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
