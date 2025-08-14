package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UserHomepage class represents the main dashboard for normal users.
 * Provides navigation to view quizzes, attempt quizzes, view results, see leaderboard, and logout.
 */
public class UserHomepage extends JFrame implements ActionListener {

    // UI Buttons
    JButton view_quiz, attempt_quiz, view_result, leaderboard, logout;
    int userId; // stores currently logged-in user ID


    UserHomepage(int userId) {
        this.userId = userId;

        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));
        Font buttonFont = new Font("Tahoma", Font.PLAIN, 15);

        // View Quiz button
        view_quiz = new JButton("View Quiz");
        view_quiz.setBounds(50, 40, 165, 30);
        view_quiz.setFont(buttonFont);
        view_quiz.setBackground(new Color(0, 123, 255));
        view_quiz.setForeground(Color.WHITE);
        view_quiz.setFocusPainted(false);
        view_quiz.addActionListener(this);
        add(view_quiz);

        // Attempt Quiz button
        attempt_quiz = new JButton("Attempt Quiz");
        attempt_quiz.setBounds(50, 90, 165, 30);
        attempt_quiz.setFont(buttonFont);
        attempt_quiz.setBackground(new Color(40, 167, 69));
        attempt_quiz.setForeground(Color.WHITE);
        attempt_quiz.setFocusPainted(false);
        attempt_quiz.addActionListener(this);
        add(attempt_quiz);

        // View Result button
        view_result = new JButton("View Result");
        view_result.setBounds(50, 140, 165, 30);
        view_result.setFont(buttonFont);
        view_result.setBackground(new Color(159, 57, 69));
        view_result.setForeground(Color.WHITE);
        view_result.setFocusPainted(false);
        view_result.addActionListener(this);
        add(view_result);

        // Leaderboard button
        leaderboard = new JButton("Leaderboard");
        leaderboard.setBounds(50, 190, 165, 30);
        leaderboard.setFont(buttonFont);
        leaderboard.setBackground(new Color(255, 193, 7));
        leaderboard.setForeground(Color.BLACK);
        leaderboard.setFocusPainted(false);
        leaderboard.addActionListener(this);
        add(leaderboard);

        // Logout button
        logout = new JButton("Logout");
        logout.setBounds(50, 240, 165, 30);
        logout.setFont(buttonFont);
        logout.setBackground(Color.GRAY);
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.addActionListener(this);
        add(logout);

        // User profile image
        ImageIcon userLogo = new ImageIcon(ClassLoader.getSystemResource("Images/user_logo.png"));
        Image userImg = userLogo.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        JLabel userLabel = new JLabel(new ImageIcon(userImg));
        userLabel.setBounds(290, 38, 253, 253);
        add(userLabel);

        // JFrame settings
        setSize(600, 350);
        setLocation(400, 200);
        setTitle("User HomePage");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new UserHomepage(11); // example userId for testing
    }

    /**
     * Handles button click events and redirects to respective pages.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logout) {
            setVisible(false);
            new Login();
        } else if (e.getSource() == view_quiz) {
            setVisible(false);
            new UserViewQuiz();
        } else if (e.getSource() == attempt_quiz) {
            setVisible(false);
            new SelectQuiz(userId);
        } else if (e.getSource() == view_result) {
            setVisible(false);
            new ViewResult(userId);
        } else if (e.getSource() == leaderboard) {
            setVisible(false);
            new LeaderBoard(userId);
        }
    }
}
