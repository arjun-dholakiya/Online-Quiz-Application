package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * AttemptQuiz class allows users to attempt quizzes by answering questions
 * with a timer and view their score at the end.
 */
public class AttemptQuiz extends JFrame implements ActionListener {

    // UI Components
    JLabel pageTitle, questionLabel, questionNumberLabel, timerLabel;
    JRadioButton optionA, optionB, optionC, optionD;
    ButtonGroup optionsGroup;
    JButton nextBtn, submitBtn, backBtn;

    // Timer for each question
    Timer questionTimer;
    int timeLeft = 30; // 30 seconds per question

    // Quiz data
    int quizId;
    ArrayList<Integer> questionIds = new ArrayList<>();
    HashMap<Integer, String> userAnswers = new HashMap<>();
    int currentIndex = 0;

    int userId; // current user attempting quiz

    // Constructor initializes the quiz attempt page.
    AttemptQuiz(int quizId, int userId) {
        this.quizId = quizId;
        this.userId = userId;

        // Load questions first before UI setup
        if (!loadQuestions()) return;
        setupUI();
        loadQuestionData();
    }

    // Loads all questions for the selected quiz.
    private boolean loadQuestions() {
        try {
            Database_Connection db = new Database_Connection();
            String query = "SELECT question_id FROM questions WHERE quiz_id = ?";
            PreparedStatement ps = db.connection.prepareStatement(query);
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                questionIds.add(rs.getInt("question_id"));
            }

            if (!questionIds.isEmpty()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No questions found for this quiz.");
                new SelectQuiz(userId);
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Sets up the UI components and layout.
    private void setupUI() {
        setLayout(null);
        getContentPane().setBackground(new Color(245, 248, 255));

        // Page title
        pageTitle = new JLabel("Attempt Quiz");
        pageTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        pageTitle.setBounds(250, 10, 250, 30);
        add(pageTitle);

        // Timer label
        timerLabel = new JLabel("Time Left: 30s");
        timerLabel.setBounds(500, 10, 150, 30);
        timerLabel.setForeground(Color.RED);
        add(timerLabel);

        // Question number and text
        questionNumberLabel = new JLabel("Question 1");
        questionNumberLabel.setBounds(100, 50, 200, 25);
        add(questionNumberLabel);

        questionLabel = new JLabel();
        questionLabel.setBounds(100, 80, 600, 25);
        add(questionLabel);

        // Options setup
        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();

        optionA.setBounds(100, 120, 400, 25);
        optionB.setBounds(100, 160, 400, 25);
        optionC.setBounds(100, 200, 400, 25);
        optionD.setBounds(100, 240, 400, 25);

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        add(optionA);
        add(optionB);
        add(optionC);
        add(optionD);

        // Buttons
        nextBtn = new JButton("Next");
        nextBtn.setBounds(420, 300, 80, 30);
        nextBtn.addActionListener(this);
        add(nextBtn);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(420, 300, 80, 30);
        submitBtn.addActionListener(this);
        submitBtn.setVisible(false);
        add(submitBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(100, 300, 80, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        setTitle("Attempt Quiz");
        setSize(700, 420);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Loads current question data onto the UI.
    private void loadQuestionData() {
        try {
            timeLeft = 30; // reset timer
            startTimer();

            int qid = questionIds.get(currentIndex);
            Database_Connection db = new Database_Connection();
            String query = "SELECT * FROM questions WHERE question_id = ?";
            PreparedStatement ps = db.connection.prepareStatement(query);
            ps.setInt(1, qid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                questionNumberLabel.setText("Question " + (currentIndex + 1) + " of " + questionIds.size());
                questionLabel.setText(rs.getString("question_text"));
                optionA.setText("A. " + rs.getString("option_a"));
                optionB.setText("B. " + rs.getString("option_b"));
                optionC.setText("C. " + rs.getString("option_c"));
                optionD.setText("D. " + rs.getString("option_d"));
                optionsGroup.clearSelection();
            }

            // Toggle submit button for last question
            if (currentIndex == questionIds.size() - 1) {
                nextBtn.setVisible(false);
                submitBtn.setVisible(true);
            } else {
                nextBtn.setVisible(true);
                submitBtn.setVisible(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // Starts timer for current question.
    private void startTimer() {
        if (questionTimer != null) questionTimer.stop();

        questionTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                questionTimer.stop();
                JOptionPane.showMessageDialog(this, "Time's up for this question!");
                saveAnswer(); // save blank if not answered
                moveToNext();
            }
        });
        questionTimer.start();
    }
    // Saves selected answer of current question.
    private void saveAnswer() {
        String selected = null;
        if (optionA.isSelected()) selected = "A";
        else if (optionB.isSelected()) selected = "B";
        else if (optionC.isSelected()) selected = "C";
        else if (optionD.isSelected()) selected = "D";

        if (selected != null) {
            userAnswers.put(questionIds.get(currentIndex), selected);
        }
    }
    // Shows feedback whether user's answer is correct or not.
    private void provideFeedback() {
        try {
            int qid = questionIds.get(currentIndex);
            Database_Connection db = new Database_Connection();
            String query = "SELECT correct_option FROM questions WHERE question_id = ?";
            PreparedStatement ps = db.connection.prepareStatement(query);
            ps.setInt(1, qid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String correct = rs.getString("correct_option");
                String userAns = userAnswers.get(qid);
                if (correct.equals(userAns)) {
                    JOptionPane.showMessageDialog(this, "Correct Answer!");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect. Correct answer was: " + correct);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Loads next question or calculates score if quiz ends.
    private void moveToNext() {
        if (currentIndex < questionIds.size() - 1) {
            currentIndex++;
            loadQuestionData();
        } else {
            calculateScoreAndShow();
        }
    }

    /**
     * Calculates user's final score and displays it.
     * Also records attempt in database.
     */
    private void calculateScoreAndShow() {
        int score = 0;
        try {
            Database_Connection db = new Database_Connection();
            for (int qid : questionIds) {
                String query = "SELECT correct_option FROM questions WHERE question_id = ?";
                PreparedStatement ps = db.connection.prepareStatement(query);
                ps.setInt(1, qid);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String correct = rs.getString("correct_option");
                    String userAns = userAnswers.get(qid);
                    if (correct.equals(userAns)) {
                        score++;
                    }
                }
            }

            // Insert attempt result into attempts table
            String insertQuery = "INSERT INTO attempts (user_id, quiz_id, score) VALUES (?, ?, ?)";
            PreparedStatement insertPs = db.connection.prepareStatement(insertQuery);
            insertPs.setInt(1, userId);
            insertPs.setInt(2, quizId);
            insertPs.setInt(3, score);
            insertPs.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Quiz Submitted!\nYour Score: " + score + " out of " + questionIds.size());
        setVisible(false);
        new UserHomepage(userId);
    }

    // Handles button click events.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextBtn) {
            if (optionsGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Please select any option before proceeding.");
            } else {
                saveAnswer();
                questionTimer.stop();
                provideFeedback();
                moveToNext();
            }
        } else if (e.getSource() == submitBtn) {
            if (optionsGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Please select any option before submitting.");
            } else {
                saveAnswer();
                questionTimer.stop();
                provideFeedback();
                calculateScoreAndShow();
            }
        } else if (e.getSource() == backBtn) {
            questionTimer.stop();
            setVisible(false);
            new SelectQuiz(userId);
        }
    }

    public static void main(String[] args) {
        new AttemptQuiz(1, 11); // Example quizId and userId for testing
    }
}
