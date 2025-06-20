package org.example.view;

import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainMenuView extends JFrame {

    private final User currentUser;

    public MainMenuView(User user) {
        this.currentUser = user;


        setTitle("PTO Tracker - Welcome, " + currentUser.getFullName());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 15, 15));

        JButton btnAddPto = createMenuButton("Add PTO Request", "icons/add.png");
        JButton btnUpdatePto = createMenuButton("Update/Cancel PTO", "icons/update.png");
        JButton btnViewSchedule = createMenuButton("View Schedule", "icons/calendar.png");
        JButton btnManageRequests = createMenuButton("Manage Requests", "icons/manage.png");
        JButton btnManageUsers = createMenuButton("Manage Users", "icons/users.png");
        JButton btnExit = createMenuButton("Exit Application", "icons/exit.png");

        if (currentUser.getRole() != User.Role.ADMIN) {
            btnManageRequests.setEnabled(false);
            btnManageRequests.setToolTipText("This option is for Admins only.");
        }
        if (currentUser.getRole() != User.Role.ADMIN) {
            btnManageRequests.setEnabled(false);
            btnManageRequests.setToolTipText("This option is for Admins only.");

            btnManageUsers.setEnabled(false);
            btnManageUsers.setToolTipText("This option is for Admins only.");
        }

        buttonsPanel.add(btnAddPto);
        buttonsPanel.add(btnUpdatePto);
        buttonsPanel.add(btnViewSchedule);
        buttonsPanel.add(btnManageRequests);
        buttonsPanel.add(btnManageUsers);
        buttonsPanel.add(btnExit);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        btnAddPto.addActionListener(e -> new AddPtoView().setVisible(true));
        btnUpdatePto.addActionListener(e -> new UpdatePtoView().setVisible(true));
        btnViewSchedule.addActionListener(e -> new ScheduleView().setVisible(true));
        btnManageRequests.addActionListener(e -> new ManageRequestsView().setVisible(true));
        btnManageUsers.addActionListener(e -> new UserManagementView().setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));

        setContentPane(mainPanel);
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            URL iconUrl = getClass().getResource("/" + iconPath);
            if (iconUrl != null) {
                button.setIcon(new ImageIcon(iconUrl));
            }
        } catch (Exception e) {
            System.err.println("Icono no encontrado: " + iconPath);
        }

        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setFocusPainted(false);
        return button;
    }
}