package Online_Quiz_Application;

import javax.swing.*;
import java.awt.*;


 /* Splash_Screen class shows a splash screen with application logo and name
 for 3 seconds before launching the Login page. */
public class Splash_Screen extends JFrame {
       Splash_Screen() {

           setLayout(new BorderLayout());

        // Load and resize splash image from resources
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Images/splash.png"));
        Image scaledImage = imageIcon.getImage().getScaledInstance(600, 400, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Create JLabel to hold the splash image
        JLabel imageLabel = new JLabel(scaledIcon);
        add(imageLabel, BorderLayout.CENTER);

        // Application name label at the bottom
        JLabel nameLabel = new JLabel("Online Quiz Application", JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(new Color(0, 102, 204));
        add(nameLabel, BorderLayout.SOUTH);

        // Configure JFrame properties
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the window on screen
        setUndecorated(true); // Remove window borders
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Timer to close splash screen after 3 seconds and open Login screen
        Timer timer = new Timer(3000, e -> {
            setVisible(false);
            new Login(); // Navigate to Login page after splash
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        new Splash_Screen();
    }
}
