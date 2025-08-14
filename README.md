# Online Quiz Application

An intuitive Java Swing-based Online Quiz Application with MySQL integration that enables Admins to create, edit, delete, and view quizzes, while Users can attempt quizzes, view their results, and check the leaderboard.

## ✨ Features

- **Admin Module**
  - Login & register securely with hashed passwords
  - Create quizzes with multiple questions and options
  - Edit existing quizzes and questions
  - Delete quizzes or individual questions
  - View all quizzes and their question counts

- **User Module**
  - Register and login
  - View available quizzes
  - Attempt quizzes with a countdown timer
  - Get immediate feedback after each question
  - View past quiz results with timestamps
  - Leaderboard to see top scorers across quizzes

## 🛠️ Technologies Used

- **Language:** Java (Swing GUI)
- **Database:** MySQL
- **IDE:** IntelliJ IDEA
- **Build Tool:** javac & java commands

## ⚙️ Installation & Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/arjun-dholakiya/Online-Quiz-Application.git

2. Set up the MySQL database
   - Create the database
      ```bash
        - CREATE DATABASE online_quiz_application;
        - USE online_quiz_application;
3. Create required tables
    1) users table
          ```bash
         CREATE TABLE users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            full_name VARCHAR(100),
            username VARCHAR(50) UNIQUE,
            password VARCHAR(255),
            email VARCHAR(100),
            role ENUM('Admin', 'User')
         );
    2) quizzes table
          ```bash
        CREATE TABLE quizzes (
            quiz_id INT AUTO_INCREMENT PRIMARY KEY,
            title VARCHAR(255)
          );
    3) questions table
          ```bash
        CREATE TABLE questions (
            question_id INT AUTO_INCREMENT PRIMARY KEY,
            quiz_id INT,
            question_text VARCHAR(500),
            option_a VARCHAR(255),
            option_b VARCHAR(255),
            option_c VARCHAR(255),
            option_d VARCHAR(255),
            correct_option ENUM('A', 'B', 'C', 'D'),
            FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
        );
    4) attempts table
          ```bash
        CREATE TABLE attempts (
            attempt_id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT,
            quiz_id INT,
            score INT,
            attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id),
            FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id)
        );
4. Configure Database Credentials
     - Open Database_Connection.java
     - Update your MySQL username, password, and port as per your local setup:
       ```bash
           String url = "jdbc:mysql://localhost:3307/online_quiz_application";
           String username = "root";
           String password = "";
5. Run the application
     - Open the project in your IDE (IntelliJ, Eclipse, VS Code with Java extension).
     - Run Splash_Screen.java or Login.java as the entry point.
     - Congrats!! Application Is Succesfully Run.

## 📂 Project Structure
    - Online_Quiz_Application/
    
       - src/
          - Images/
              - admin_logo.png
              - login_logo.png
              - signup_logo.png
              - splash.png
              - user_logo.png
              
          - Online_Quiz_Application/
              - AdminHomepage.java
              - AttemptQuiz.java
              - CreateQuiz.java
              - Database_Connection.java
              - DeleteQuiz.java
              - EditQuiz.java
              - LeaderBoard.java
              - Login.java
              - Register.java
              - SelectQuiz.java
              - Splash_Screen.java
              - UserHomepage.java
              - UserViewQuiz.java
              - ViewResult.java

## ✏️ Author

  Arjun Dholakiya

    - Linkedin : https://www.linkedin.com/in/arjun-dholakiya-6251592b9
    - Github : https://github.com/arjun-dholakiya

## 🚫 Limitations

  - Uses hardcoded database connection (no environment configs).
  - Password reset feature not implemented.
  - No email verification or multi-factor authentication.
  - Limited to single-user sessions per execution.
  - Uses Java Swing, not deployable as a web application without migration.

## 🎯 Future Enhancements

  - Add email notifications after quiz attempts
  - Enhance UI with Material Design themes
  - Export results as PDF reports
  - Add profile editing and password reset features
  - Implement pagination for leaderboards
  
## 💡 “Consistency and clarity build great projects. Keep learning and building. Thank You."




    

    

    
              
              
              
              
              
              
              
              
            

