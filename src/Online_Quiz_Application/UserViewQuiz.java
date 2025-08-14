package Online_Quiz_Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * UserViewQuiz class displays all available quizzes to the user
 * with quiz ID, title, and number of questions for each quiz.
 */
public class UserViewQuiz extends JFrame implements ActionListener {

    JTable quizTable;
    JButton backBtn;

    UserViewQuiz() {
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        // Page title
        JLabel pageTitle = new JLabel("Available Quizzes");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(250, 10, 250, 30);
        add(pageTitle);

        // Table columns with non-editable model
        String[] columnNames = {"Quiz ID", "Topic Title", "No. of Questions"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        // Table setup
        quizTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(quizTable);
        scrollPane.setBounds(50, 60, 600, 250);
        add(scrollPane);

        // Back button
        backBtn = new JButton("Back");
        backBtn.setBounds(300, 330, 100, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // Populate table with data
        populateQuizTable(model);

        setTitle("View Quiz");
        setSize(720, 420);
        setLocation(400, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Populates the quiz table with quiz data from the database.
    private void populateQuizTable(DefaultTableModel model) {
        try {
            Database_Connection db = new Database_Connection();
            String query = "SELECT q.quiz_id, q.title, COUNT(que.question_id) as question_count " +
                    "FROM quizzes q LEFT JOIN questions que ON q.quiz_id = que.quiz_id " +
                    "GROUP BY q.quiz_id, q.title";
            ResultSet rs = db.statement.executeQuery(query);

            // Add each quiz as a row in the table
            while (rs.next()) {
                int quizId = rs.getInt("quiz_id");
                String title = rs.getString("title");
                int questionCount = rs.getInt("question_count");

                Object[] row = {quizId, title, questionCount};
                model.addRow(row);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading quizzes!");
        }
    }

    // Handles button click events.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            setVisible(false);
            new UserHomepage(11); // Return to user homepage
        }
    }

    public static void main(String[] args) {
        new UserViewQuiz();
    }
}
