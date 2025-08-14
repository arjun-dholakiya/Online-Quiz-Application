package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * EditQuiz class allows admin to edit existing quizzes.
 * Admin can select a quiz, navigate through its questions,
 * and update them as needed.
 */
public class EditQuiz extends JFrame implements ActionListener {

    // UI components
    JComboBox<String> quizDropdown;
    JButton loadBtn, updateQuestionBtn, nextQuestionBtn, backBtn;
    JTextField questionField, optionAField, optionBField, optionCField, optionDField;
    JComboBox<String> correctAnswerBox;

    // ArrayList to store questions and current quiz state
    ArrayList<Integer> questionIds = new ArrayList<>();
    int currentIndex = 0;
    int currentQuizId = 0;

    EditQuiz() {
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        JLabel pageTitle = new JLabel("Edit Quiz Page");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(235, 10, 250, 30);
        add(pageTitle);

        // Dropdown to select quiz
        JLabel selectQuizLabel = new JLabel("Select Quiz:");
        selectQuizLabel.setBounds(50, 60, 100, 25);
        add(selectQuizLabel);

        quizDropdown = new JComboBox<>();
        quizDropdown.setBounds(150, 60, 250, 25);
        quizDropdown.addItem("Select a quiz");
        add(quizDropdown);

        loadBtn = new JButton("Load");
        loadBtn.setBounds(420, 60, 80, 25);
        loadBtn.addActionListener(this);
        add(loadBtn);

        // Question input fields
        questionField = createField("Question:", 100);
        optionAField = createField("Option A:", 140);
        optionBField = createField("Option B:", 180);
        optionCField = createField("Option C:", 220);
        optionDField = createField("Option D:", 260);

        // Correct answer dropdown
        JLabel correctAnsLabel = new JLabel("Correct Answer:");
        correctAnsLabel.setBounds(50, 300, 100, 25);
        add(correctAnsLabel);

        String[] options = {"A", "B", "C", "D"};
        correctAnswerBox = new JComboBox<>(options);
        correctAnswerBox.setBounds(150, 300, 100, 25);
        add(correctAnswerBox);

        // Action buttons
        updateQuestionBtn = new JButton("Update Question");
        updateQuestionBtn.setBounds(50, 350, 150, 30);
        updateQuestionBtn.addActionListener(this);
        add(updateQuestionBtn);

        nextQuestionBtn = new JButton("Next Question");
        nextQuestionBtn.setBounds(235, 350, 150, 30);
        nextQuestionBtn.addActionListener(this);
        add(nextQuestionBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(420, 350, 80, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // Populate quiz dropdown on load
        populateQuizDropdown();

        // JFrame settings
        setTitle("Edit Quiz");
        setSize(600, 450);
        setLocation(400, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Helper method to create input fields with labels.
    private JTextField createField(String labelText, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(50, y, 100, 25);
        add(label);

        JTextField field = new JTextField();
        field.setBounds(150, y, 350, 25);
        add(field);
        return field;
    }

    // Populates quiz dropdown with available quizzes from database.
    private void populateQuizDropdown() {
        try {
            Database_Connection db = new Database_Connection();
            String query = "SELECT * FROM quizzes";
            ResultSet rs = db.statement.executeQuery(query);

            while (rs.next()) {
                quizDropdown.addItem(rs.getInt("quiz_id") + " - " + rs.getString("title"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Loads questions for the selected quiz.
    private void loadQuestions() {
        try {
            questionIds.clear();
            currentIndex = 0;

            String selected = (String) quizDropdown.getSelectedItem();
            if (selected.equals("Select a quiz")) {
                JOptionPane.showMessageDialog(this, "Please select a quiz");
                return;
            }

            currentQuizId = Integer.parseInt(selected.split(" - ")[0]);

            Database_Connection db = new Database_Connection();
            String query = "SELECT * FROM questions WHERE quiz_id = " + currentQuizId;
            ResultSet rs = db.statement.executeQuery(query);

            while (rs.next()) {
                questionIds.add(rs.getInt("question_id"));
            }

            if (!questionIds.isEmpty()) {
                loadQuestionData();
            } else {
                JOptionPane.showMessageDialog(this, "No questions found for this quiz!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Loads current question data into UI fields.
    private void loadQuestionData() {
        try {
            int qid = questionIds.get(currentIndex);
            Database_Connection db = new Database_Connection();
            String query = "SELECT * FROM questions WHERE question_id = " + qid;
            ResultSet rs = db.statement.executeQuery(query);

            if (rs.next()) {
                questionField.setText(rs.getString("question_text"));
                optionAField.setText(rs.getString("option_a"));
                optionBField.setText(rs.getString("option_b"));
                optionCField.setText(rs.getString("option_c"));
                optionDField.setText(rs.getString("option_d"));
                correctAnswerBox.setSelectedItem(rs.getString("correct_option"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Updates current question with new data entered by admin.
    private void updateCurrentQuestion() {
        if (!areFieldsFilled()) return;

        try {
            int qid = questionIds.get(currentIndex);
            Database_Connection db = new Database_Connection();
            String query = "UPDATE questions SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_option=? WHERE question_id=?";
            PreparedStatement ps = db.connection.prepareStatement(query);

            ps.setString(1, questionField.getText());
            ps.setString(2, optionAField.getText());
            ps.setString(3, optionBField.getText());
            ps.setString(4, optionCField.getText());
            ps.setString(5, optionDField.getText());
            ps.setString(6, correctAnswerBox.getSelectedItem().toString());
            ps.setInt(7, qid);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Question updated successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Navigates to next question in the quiz.
    private void nextQuestion() {
        if (!areFieldsFilled()) return;

        if (currentIndex < questionIds.size() - 1) {
            currentIndex++;
            loadQuestionData();
        } else {
            JOptionPane.showMessageDialog(this, "No more questions!");
        }
    }

    // Validates if all input fields are filled.
    public boolean areFieldsFilled() {
        if (questionField.getText().trim().isEmpty() ||
                optionAField.getText().trim().isEmpty() ||
                optionBField.getText().trim().isEmpty() ||
                optionCField.getText().trim().isEmpty() ||
                optionDField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill all the fields!");
            return false;
        }
        return true;
    }

    // Handles button click events.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadBtn) {
            loadQuestions();
        } else if (e.getSource() == updateQuestionBtn) {
            updateCurrentQuestion();
        } else if (e.getSource() == nextQuestionBtn) {
            nextQuestion();
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new AdminHomepage();
        }
    }

    public static void main(String[] args) {
        new EditQuiz();
    }
}
