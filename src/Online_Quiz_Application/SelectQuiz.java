package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.LinkedHashMap;

/**
 * SelectQuiz class allows the user to view all quizzes
 * and select a quiz to attempt.
 */
public class SelectQuiz extends JFrame implements ActionListener {

    JList<String> quizList;
    JButton backBtn, startQuizBtn;
    DefaultListModel<String> listModel;
    LinkedHashMap<Integer, Integer> indexToQuizId; // Maps list index to quiz_id
    int userId;

    // Constructor initializes the Select Quiz page for the user.
    SelectQuiz(int userId) {
        this.userId = userId;

        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        // Page title
        JLabel pageTitle = new JLabel("Select Quiz Topic");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(270, 10, 250, 30);
        add(pageTitle);

        // Quiz list setup
        listModel = new DefaultListModel<>();
        quizList = new JList<>(listModel);
        quizList.setFont(new Font("Tahoma", Font.PLAIN, 14));
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(quizList);
        scrollPane.setBounds(50, 60, 600, 250);
        add(scrollPane);

        // Start Quiz button
        startQuizBtn = new JButton("Start Quiz");
        startQuizBtn.setBounds(50, 330, 100, 30);
        startQuizBtn.addActionListener(this);
        add(startQuizBtn);

        // Back button
        backBtn = new JButton("Back");
        backBtn.setBounds(550, 330, 100, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        indexToQuizId = new LinkedHashMap<>();

        // Populate the quiz list from database
        populateQuizList();

        setTitle("User Select Quiz");
        setSize(720, 420);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Populates the quiz list with quizzes from the database.
    private void populateQuizList() {
        try {
            Database_Connection db = new Database_Connection();
            String query = "SELECT q.quiz_id, q.title, COUNT(que.question_id) as question_count " +
                    "FROM quizzes q LEFT JOIN questions que ON q.quiz_id = que.quiz_id " +
                    "GROUP BY q.quiz_id, q.title";
            ResultSet rs = db.statement.executeQuery(query);

            int count = 1;
            while (rs.next()) {
                int quizId = rs.getInt("quiz_id");
                String title = rs.getString("title");
                int questionCount = rs.getInt("question_count");

                // Display format: 1. Title (no. of questions)
                String listItem = count + ". " + title + " (" + questionCount + " questions)";
                listModel.addElement(listItem);

                // Map list index to quiz_id for future selection
                indexToQuizId.put(count - 1, quizId);
                count++;
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
            // Navigate back to user homepage
            setVisible(false);
            new UserHomepage(userId);
        } else if (e.getSource() == startQuizBtn) {
            int selectedIndex = quizList.getSelectedIndex();
            if (selectedIndex != -1) {
                // Retrieve quizId from mapping
                int quizId = indexToQuizId.get(selectedIndex);
                setVisible(false);
                new AttemptQuiz(quizId, userId);
                System.out.println("Selected quiz id: " + quizId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a quiz to start!");
            }
        }
    }

    public static void main(String[] args) {
        new SelectQuiz(11); // Example userId for testing
    }
}
