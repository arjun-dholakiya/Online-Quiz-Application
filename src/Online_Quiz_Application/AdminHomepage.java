package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AdminHomepage class represents the main dashboard for Admin users.
 * Provides navigation buttons to create, edit, delete, and view quizzes, as well as logout functionality.
 */
public class AdminHomepage extends JFrame implements ActionListener {

    // UI Buttons
    JButton create_quiz, edit_quiz, delete_quiz, view_all_quiz, logout;

    AdminHomepage() {
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        Font buttonFont = new Font("Tahoma", Font.PLAIN, 15);

        // Create Quiz button
        create_quiz = new JButton("Create Quiz");
        create_quiz.setBounds(360, 40, 160, 30);
        create_quiz.setFont(buttonFont);
        create_quiz.setBackground(new Color(0, 123, 255));
        create_quiz.setForeground(Color.WHITE);
        create_quiz.setFocusPainted(false);
        create_quiz.addActionListener(this);
        add(create_quiz);

        // Edit Quiz button
        edit_quiz = new JButton("Edit Quiz");
        edit_quiz.setBounds(360, 90, 160, 30);
        edit_quiz.setFont(buttonFont);
        edit_quiz.setBackground(new Color(40, 167, 69));
        edit_quiz.setForeground(Color.WHITE);
        edit_quiz.setFocusPainted(false);
        edit_quiz.addActionListener(this);
        add(edit_quiz);

        // Delete Quiz button
        delete_quiz = new JButton("Delete Quiz");
        delete_quiz.setBounds(360, 140, 160, 30);
        delete_quiz.setFont(buttonFont);
        delete_quiz.setBackground(new Color(220, 53, 69));
        delete_quiz.setForeground(Color.WHITE);
        delete_quiz.setFocusPainted(false);
        delete_quiz.addActionListener(this);
        add(delete_quiz);

        // View All Quizzes button
        view_all_quiz = new JButton("View All Quiz");
        view_all_quiz.setBounds(360, 190, 160, 30);
        view_all_quiz.setFont(buttonFont);
        view_all_quiz.setBackground(new Color(255, 193, 7));
        view_all_quiz.setForeground(Color.BLACK);
        view_all_quiz.setFocusPainted(false);
        view_all_quiz.addActionListener(this);
        add(view_all_quiz);

        // Logout button
        logout = new JButton("Logout");
        logout.setBounds(360, 240, 160, 30);
        logout.setFont(buttonFont);
        logout.setBackground(Color.GRAY);
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.addActionListener(this);
        add(logout);

        // Admin profile image
        ImageIcon adminLogo = new ImageIcon(ClassLoader.getSystemResource("Images/admin_logo.png"));
        Image adminImg = adminLogo.getImage().getScaledInstance(243, 243, Image.SCALE_DEFAULT);
        JLabel adminLabel = new JLabel(new ImageIcon(adminImg));
        adminLabel.setBounds(20, 27, 250, 250);
        add(adminLabel);

        // JFrame settings
        setSize(600, 350);
        setLocation(400, 200);
        setTitle("Admin Homepage");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new AdminHomepage();
    }


    // Handles action events for button clicks and redirects to respective pages.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logout) {
            setVisible(false);
            new Login();
        } else if (e.getSource() == create_quiz) {
            setVisible(false);
            new CreateQuiz();
        } else if (e.getSource() == edit_quiz) {
            setVisible(false);
            new EditQuiz();
        } else if (e.getSource() == delete_quiz) {
            setVisible(false);
            new DeleteQuiz();
        } else if (e.getSource() == view_all_quiz) {
            setVisible(false);
            new ViewAllQuiz();
        }
    }
}
