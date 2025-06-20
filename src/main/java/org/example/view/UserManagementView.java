package org.example.view;

import org.example.model.User;
import org.example.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementView extends JFrame {

    private final UserService userService = new UserService();
    private JTable userTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<User.Role> roleComboBox;

    private JButton btnNew;
    private JButton btnSave;
    private JButton btnDelete;

    public UserManagementView() {
        setTitle("User Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane();

        tableModel = new DefaultTableModel(new String[]{"ID", "Full Name", "Email", "Role"}, 0);
        userTable = new JTable(tableModel);
        splitPane.setLeftComponent(new JScrollPane(userTable));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        idField = new JTextField(15);
        idField.setEditable(false);
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        roleComboBox = new JComboBox<>(User.Role.values());

        gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(idField, gbc);
        gbc.gridy++; formPanel.add(nameField, gbc);
        gbc.gridy++; formPanel.add(emailField, gbc);
        gbc.gridy++; formPanel.add(roleComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNew = new JButton("New");
        btnSave = new JButton("Save");
        btnDelete = new JButton("Delete");
        buttonPanel.add(btnNew);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnDelete);

        gbc.gridy++; gbc.gridwidth=2; formPanel.add(buttonPanel, gbc);

        splitPane.setRightComponent(formPanel);

        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        btnNew.addActionListener(e -> clearForm());
        btnSave.addActionListener(e -> saveUser());
        btnDelete.addActionListener(e -> deleteUser());

        setContentPane(splitPane);

        loadUsersIntoTable();
    }

    private void loadUsersIntoTable() {
        tableModel.setRowCount(0);
        List<User> users = userService.findAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole()
            });
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = userTable.getSelectedRow();
        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        roleComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
    }

    private void clearForm() {
        userTable.clearSelection();
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        roleComboBox.setSelectedIndex(0);
        nameField.requestFocus();
    }

    private void saveUser() {

        if (idField.getText().isEmpty()) {

            userService.createUser(
                    nameField.getText(),
                    emailField.getText(),
                    "password123",
                    (User.Role) roleComboBox.getSelectedItem()
            );
            JOptionPane.showMessageDialog(this, "User created successfully!");
        } else {
            User user = new User();
            user.setUserId(Integer.parseInt(idField.getText()));
            user.setFullName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setRole((User.Role) roleComboBox.getSelectedItem());
            userService.updateUser(user);
            JOptionPane.showMessageDialog(this, "User updated successfully!");
        }

        loadUsersIntoTable();
        clearForm();
    }

    private void deleteUser() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            int userId = Integer.parseInt(idField.getText());
            userService.deleteUser(userId);
            JOptionPane.showMessageDialog(this, "User deleted successfully.");
            loadUsersIntoTable();
            clearForm();
        }
    }
}