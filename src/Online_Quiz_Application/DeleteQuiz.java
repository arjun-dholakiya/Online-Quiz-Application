package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * DeleteQuiz class allows the admin to delete entire quiz topics
 * or individual questions from the database.
 */
public class DeleteQuiz extends JFrame implements ActionListener {

    JComboBox<String> topicDropdown, questionDropdown;
    JButton deleteTopicBtn, deleteQuestionBtn, backBtn;

    DeleteQuiz() {
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        JLabel pageTitle = new JLabel("Delete Quiz Page");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(250, 10, 250, 30);
        add(pageTitle);

        // Topic selection components
        JLabel topicLabel = new JLabel("Select Topic:");
        topicLabel.setBounds(50, 60, 100, 25);
        add(topicLabel);

        topicDropdown = new JComboBox<>();
        topicDropdown.setBounds(160, 60, 250, 25);
        topicDropdown.addItem("Select a topic");
        add(topicDropdown);

        deleteTopicBtn = new JButton("Delete Entire Topic");
        deleteTopicBtn.setBounds(450, 60, 180, 25);
        deleteTopicBtn.addActionListener(this);
        add(deleteTopicBtn);

        // Question selection components
        JLabel questionLabel = new JLabel("Select Question:");
        questionLabel.setBounds(30, 110, 120, 25);
        add(questionLabel);

        questionDropdown = new JComboBox<>();
        questionDropdown.setBounds(160, 110, 250, 25);
        questionDropdown.addItem("Select a question");
        add(questionDropdown);

        deleteQuestionBtn = new JButton("Delete Selected Question");
        deleteQuestionBtn.setBounds(450, 110, 180, 25);
        deleteQuestionBtn.addActionListener(this);
        add(deleteQuestionBtn);

        // Back button
        backBtn = new JButton("Back");
        backBtn.setBounds(300, 160, 100, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // Populate topics on load
        populateTopics();
        topicDropdown.addActionListener(this);

        setTitle("Delete Quiz");
        setSize(700, 250);
        setLocation(400, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    // Populates the topic dropdown with quiz titles from the database.
    private void populateTopics() {
        try {
            Database_Connection db = new Database_Connection();
            String query = "SELECT DISTINCT title FROM quizzes";
            ResultSet rs = db.statement.executeQuery(query);

            while (rs.next()) {
                topicDropdown.addItem(rs.getString("title").trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Populates the question dropdown based on the selected topic.
    private void populateQuestions(String topic) {
        try {
            questionDropdown.removeAllItems();
            questionDropdown.addItem("Select a question");

            Database_Connection db = new Database_Connection();
            String query = "SELECT q.question_id, q.question_text " +
                    "FROM questions q " +
                    "JOIN quizzes z ON q.quiz_id = z.quiz_id " +
                    "WHERE z.title = ?";
            PreparedStatement ps = db.connection.prepareStatement(query);
            ps.setString(1, topic.trim());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                questionDropdown.addItem(rs.getInt("question_id") + " - " + rs.getString("question_text"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Deletes an entire quiz topic along with its questions.
    private void deleteTopic(String topic) {
        try {
            Database_Connection db = new Database_Connection();

            // Get quiz ID
            String getQuizIdQuery = "SELECT quiz_id FROM quizzes WHERE title = ?";
            PreparedStatement psGetQuizId = db.connection.prepareStatement(getQuizIdQuery);
            psGetQuizId.setString(1, topic.trim());
            ResultSet rs = psGetQuizId.executeQuery();

            if (rs.next()) {
                int quizId = rs.getInt("quiz_id");

                // Delete questions of the quiz
                String deleteQuestionsQuery = "DELETE FROM questions WHERE quiz_id = ?";
                PreparedStatement psDeleteQuestions = db.connection.prepareStatement(deleteQuestionsQuery);
                psDeleteQuestions.setInt(1, quizId);
                psDeleteQuestions.executeUpdate();

                // Delete the quiz itself
                String deleteQuizQuery = "DELETE FROM quizzes WHERE quiz_id = ?";
                PreparedStatement psDeleteQuiz = db.connection.prepareStatement(deleteQuizQuery);
                psDeleteQuiz.setInt(1, quizId);
                psDeleteQuiz.executeUpdate();

                JOptionPane.showMessageDialog(this, "Topic and all its questions deleted successfully!");
                dispose();
                new DeleteQuiz();
            } else {
                JOptionPane.showMessageDialog(this, "Quiz not found!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting topic!");
        }
    }

    /**
     * Deletes a selected question from the quiz.
     * If no questions remain, deletes the quiz.
     */
    private void deleteQuestion(int questionId, String topic) {
        try {
            Database_Connection db = new Database_Connection();

            // Delete selected question
            String deleteQuestionQuery = "DELETE FROM questions WHERE question_id = ?";
            PreparedStatement psDeleteQuestion = db.connection.prepareStatement(deleteQuestionQuery);
            psDeleteQuestion.setInt(1, questionId);
            psDeleteQuestion.executeUpdate();

            // Check if any questions remain in the topic
            String checkQuery = "SELECT q.question_id FROM questions q " +
                    "JOIN quizzes z ON q.quiz_id = z.quiz_id " +
                    "WHERE z.title = ?";
            PreparedStatement psCheck = db.connection.prepareStatement(checkQuery);
            psCheck.setString(1, topic.trim());
            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                deleteTopic(topic);
            } else {
                JOptionPane.showMessageDialog(this, "Question deleted successfully!");
                dispose();
                new DeleteQuiz();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles button click events for
     * topic/question deletion and navigation.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == topicDropdown) {
            String topic = (String) topicDropdown.getSelectedItem();
            if (!topic.equals("Select a topic")) {
                populateQuestions(topic);
            }
        } else if (e.getSource() == deleteTopicBtn) {
            String topic = (String) topicDropdown.getSelectedItem();
            if (!topic.equals("Select a topic")) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete the entire topic and its questions?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteTopic(topic);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a topic first!");
            }
        } else if (e.getSource() == deleteQuestionBtn) {
            String selectedQuestion = (String) questionDropdown.getSelectedItem();
            String topic = (String) topicDropdown.getSelectedItem();

            if (selectedQuestion != null && !selectedQuestion.equals("Select a question")) {
                int questionId = Integer.parseInt(selectedQuestion.split(" - ")[0]);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this question?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteQuestion(questionId, topic);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a question first!");
            }
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new AdminHomepage();
        }
    }

    public static void main(String[] args) {
        new DeleteQuiz();
    }
}
