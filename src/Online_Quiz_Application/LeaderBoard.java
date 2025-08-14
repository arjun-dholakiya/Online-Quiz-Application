package Online_Quiz_Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * LeaderBoard class displays the top scorers across all quizzes.
 * Shows user ranks, usernames, quiz titles, scores, and attempt dates.
 */
public class LeaderBoard extends JFrame implements ActionListener {

    JTable leaderboardTable;
    JButton backBtn;
    int userId;

    LeaderBoard(int userId) {
        this.userId = userId;

        // UI layout and theme
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        // Page title
        JLabel pageTitle = new JLabel("Leaderboard - Top Scorers");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(220, 10, 400, 30);
        add(pageTitle);

        // Table columns
        String[] columnNames = {"Rank", "Username", "Quiz Title", "Score", "Attempt Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };

        // Table setup
        leaderboardTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBounds(30, 60, 640, 250);
        add(scrollPane);

        // Back button
        backBtn = new JButton("Back");
        backBtn.setBounds(300, 330, 100, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // Populate leaderboard table with data
        populateLeaderboard(model);

        // Frame properties
        setTitle("Leaderboard");
        setSize(720, 420);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Populates the leaderboard table with top scorers from the database.
    private void populateLeaderboard(DefaultTableModel model) {
        try {
            Database_Connection db = new Database_Connection();

            // SQL query to get top 10 scores with username and quiz title
            String query = "SELECT u.username, q.title, a.score, a.attempt_date " +
                    "FROM attempts a " +
                    "JOIN users u ON a.user_id = u.id " +
                    "JOIN quizzes q ON a.quiz_id = q.quiz_id " +
                    "ORDER BY a.score DESC, a.attempt_date DESC " +
                    "LIMIT 10";

            PreparedStatement ps = db.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                String quizTitle = rs.getString("title");
                int score = rs.getInt("score");
                Timestamp attemptDate = rs.getTimestamp("attempt_date");

                // Convert timestamp to readable format (e.g. yyyy-MM-dd HH:mm:ss)
                String formattedDate = attemptDate.toLocalDateTime().toLocalDate() + " " + attemptDate.toLocalDateTime().toLocalTime();

                // Add row to table
                model.addRow(new Object[]{rank, username, quizTitle, score, formattedDate});
                rank++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading leaderboard!");
        }
    }

    // Handles back button click event to return to user homepage.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            setVisible(false);
            new UserHomepage(userId);
        }
    }

    public static void main(String[] args) {
        new LeaderBoard(11); // for testing with userId=11
    }
}
