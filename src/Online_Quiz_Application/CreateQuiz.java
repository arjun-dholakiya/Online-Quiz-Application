package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * CreateQuiz class allows admin to create new quizzes with multiple questions.
 * Admin can add quiz title, questions, options, and specify correct answer.
 */
public class CreateQuiz extends JFrame implements ActionListener {

    // UI components
    JTextField topicField, questionField, optionAField, optionBField, optionCField, optionDField;
    JComboBox<String> correctAnswerBox;
    JButton addQuestionBtn, finishBtn, backBtn;

    int quizId = 0; // stores newly created quiz_id after first insertion

    CreateQuiz() {
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        JLabel pageTitle = new JLabel("Create New Quiz");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(200, 10, 250, 30);
        add(pageTitle);

        // Topic input
        JLabel topicLabel = new JLabel("Topic:");
        topicLabel.setBounds(50, 50, 100, 25);
        add(topicLabel);

        topicField = new JTextField();
        topicField.setBounds(150, 50, 312, 25);
        add(topicField);

        // Question input
        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setBounds(50, 90, 100, 25);
        add(questionLabel);

        questionField = new JTextField();
        questionField.setBounds(150, 90, 312, 25);
        add(questionField);

        // Option A input
        JLabel optionALabel = new JLabel("Option A:");
        optionALabel.setBounds(50, 130, 100, 25);
        add(optionALabel);

        optionAField = new JTextField();
        optionAField.setBounds(150, 130, 312, 25);
        add(optionAField);

        // Option B input
        JLabel optionBLabel = new JLabel("Option B:");
        optionBLabel.setBounds(50, 170, 100, 25);
        add(optionBLabel);

        optionBField = new JTextField();
        optionBField.setBounds(150, 170, 312, 25);
        add(optionBField);

        // Option C input
        JLabel optionCLabel = new JLabel("Option C:");
        optionCLabel.setBounds(50, 210, 100, 25);
        add(optionCLabel);

        optionCField = new JTextField();
        optionCField.setBounds(150, 210, 312, 25);
        add(optionCField);

        // Option D input
        JLabel optionDLabel = new JLabel("Option D:");
        optionDLabel.setBounds(50, 250, 100, 25);
        add(optionDLabel);

        optionDField = new JTextField();
        optionDField.setBounds(150, 250, 312, 25);
        add(optionDField);

        // Correct Answer dropdown
        JLabel correctAnsLabel = new JLabel("Correct Answer:");
        correctAnsLabel.setBounds(50, 290, 100, 25);
        add(correctAnsLabel);

        String[] options = {"A", "B", "C", "D"};
        correctAnswerBox = new JComboBox<>(options);
        correctAnswerBox.setBounds(150, 290, 100, 25);
        add(correctAnswerBox);

        // Buttons
        addQuestionBtn = new JButton("Add Question");
        addQuestionBtn.setBounds(50, 340, 120, 30);
        addQuestionBtn.addActionListener(this);
        add(addQuestionBtn);

        finishBtn = new JButton("Finish Quiz Creation");
        finishBtn.setBounds(200, 340, 150, 30);
        finishBtn.addActionListener(this);
        add(finishBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(380, 340, 80, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // JFrame settings
        setTitle("Create Quiz");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new CreateQuiz();
    }

    /**
     * Handles button click events for adding questions,
     * finishing quiz creation, and going back.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addQuestionBtn) {

            // Validate input fields
            if (topicField.getText().trim().isEmpty()
                    || questionField.getText().trim().isEmpty()
                    || optionAField.getText().trim().isEmpty()
                    || optionBField.getText().trim().isEmpty()
                    || optionCField.getText().trim().isEmpty()
                    || optionDField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please fill all details");
            } else {
                try {
                    Database_Connection dbConnection = new Database_Connection();

                    // Insert quiz title only first time
                    if (quizId == 0) {
                        String topic = topicField.getText().trim();

                        String insertQuizQuery = "INSERT INTO quizzes(title) VALUES(?)";
                        PreparedStatement psQuiz = dbConnection.connection.prepareStatement(insertQuizQuery, Statement.RETURN_GENERATED_KEYS);
                        psQuiz.setString(1, topic);
                        psQuiz.executeUpdate();

                        ResultSet rs = psQuiz.getGeneratedKeys();
                        if (rs.next()) {
                            quizId = rs.getInt(1);
                        }
                    }

                    // Insert question into questions table
                    String insertQuestionQuery = "INSERT INTO questions(quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES(?,?,?,?,?,?,?)";
                    PreparedStatement psQuestion = dbConnection.connection.prepareStatement(insertQuestionQuery);
                    psQuestion.setInt(1, quizId);
                    psQuestion.setString(2, questionField.getText());
                    psQuestion.setString(3, optionAField.getText());
                    psQuestion.setString(4, optionBField.getText());
                    psQuestion.setString(5, optionCField.getText());
                    psQuestion.setString(6, optionDField.getText());
                    psQuestion.setString(7, correctAnswerBox.getSelectedItem().toString());

                    psQuestion.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Question added successfully!");

                    // Clear question fields for next entry
                    questionField.setText("");
                    optionAField.setText("");
                    optionBField.setText("");
                    optionCField.setText("");
                    optionDField.setText("");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding question!");
                }
            }

        } else if (e.getSource() == finishBtn) {
            JOptionPane.showMessageDialog(null, "Quiz creation completed!");
            setVisible(false);
            new AdminHomepage();
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new AdminHomepage();
        }
    }
}
