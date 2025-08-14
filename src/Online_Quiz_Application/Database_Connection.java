package Online_Quiz_Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Database_Connection class handles establishing a connection to the MySQL database.
 * It initializes the connection and statement objects for executing SQL queries.
 */
public class Database_Connection {

    // Database credentials and URL
    String url = "jdbc:mysql://localhost:3307/online_quiz_application";
    String username = "root";
    String password = "";

    // Connection and Statement objects used throughout the application
    Connection connection;
    Statement statement;

    Database_Connection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
    }
}
