
package org.example.view;

import org.example.model.User;
import org.example.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField employeeIdField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private final UserService userService = new UserService();

    public LoginView() {
        setTitle("PTO Tracker - Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Employee ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        employeeIdField = new JTextField(20);
        panel.add(employeeIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField();
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        setContentPane(panel);

        loginButton.addActionListener(e -> onLogin());
    }

    private void onLogin() {
        String identifier = employeeIdField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (identifier.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee ID and Password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User loggedInUser = userService.login(identifier, password);

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + loggedInUser.getFullName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

             new MainMenuView(loggedInUser).setVisible(true);
            this.dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}