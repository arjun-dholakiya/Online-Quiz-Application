package Online_Quiz_Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * ViewResult class displays the user's past quiz attempts with scores and attempt dates.
 * Provides a simple table view with a back button to return to the user homepage.
 */
public class ViewResult extends JFrame implements ActionListener {

    JTable resultTable;
    JButton backBtn;
    int userId;

    ViewResult(int userId) {
        this.userId = userId;

        // UI Layout and theme
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        // Page title
        JLabel pageTitle = new JLabel("Quiz Results");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(300, 10, 300, 30);
        add(pageTitle);

        // Table columns
        String[] columnNames = {"Quiz Title", "Score", "Attempt Date"};

        // Table model with cells set as non-editable
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing
            }
        };

        // Result table setup
        resultTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(50, 60, 600, 250);
        add(scrollPane);

        // Back button setup
        backBtn = new JButton("Back");
        backBtn.setBounds(300, 330, 100, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // Populate table with user's quiz results
        populateResults(model);

        // Frame properties
        setTitle("View Results");
        setSize(720, 420);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Fetches and displays user's quiz attempts in the table.
    private void populateResults(DefaultTableModel model) {
        try {
            Database_Connection db = new Database_Connection();

            // SQL query to fetch quiz title, score, and attempt date for the user
            String query = "SELECT q.title, a.score, a.attempt_date " +
                    "FROM attempts a " +
                    "JOIN quizzes q ON a.quiz_id = q.quiz_id " +
                    "WHERE a.user_id = ? " +
                    "ORDER BY a.attempt_date DESC";

            PreparedStatement ps = db.connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            // Format date to "dd-MM-yyyy hh:mm:ss AM/PM"
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

            // Add each row to the table
            while (rs.next()) {
                String quizTitle = rs.getString("title");
                int score = rs.getInt("score");
                Timestamp attemptDate = rs.getTimestamp("attempt_date");

                String formattedDate = sdf.format(attemptDate);

                model.addRow(new Object[]{quizTitle, score, formattedDate});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading results!");
        }
    }

    // Handles back button click event.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            setVisible(false);
            new UserHomepage(userId);
        }
    }

    public static void main(String[] args) {
        new ViewResult(11); // Test with any userId
    }
}
