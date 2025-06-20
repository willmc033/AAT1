package org.example.view;

import org.example.model.Request;
import org.example.service.RequestService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ManageRequestsView extends JFrame {

    private JTable resultsTable;
    private DefaultTableModel tableModel;

    private JTextField dateFromField;
    private JTextField dateToField;
    private JComboBox<String> lobComboBox;
    private JComboBox<Request.Status> statusComboBox;
    private JTextField agentSearchField;
    private JButton searchButton;

    private JComboBox<Request.Status> newStatusComboBox;
    private JButton updateButton;

    private final RequestService requestService = new RequestService();

    public ManageRequestsView() {
        setTitle("Request Management Console");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));

        dateFromField = new JTextField();
        dateToField = new JTextField();
        lobComboBox = new JComboBox<>(new String[]{"All", "Mob", "EPP", "Sales", "SOS", "FFH"});

        statusComboBox = new JComboBox<>(Request.Status.values());
        statusComboBox.insertItemAt(null, 0);
        statusComboBox.setSelectedIndex(0);

        agentSearchField = new JTextField();
        searchButton = new JButton("Search");

        filterPanel.add(new JLabel("Date From (YYYY-MM-DD):"));
        filterPanel.add(dateFromField);
        filterPanel.add(new JLabel("Line of Work:"));
        filterPanel.add(lobComboBox);
        filterPanel.add(new JLabel("Search by Agent:"));
        filterPanel.add(agentSearchField);

        filterPanel.add(new JLabel("Date To (YYYY-MM-DD):"));
        filterPanel.add(dateToField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusComboBox);
        filterPanel.add(new JLabel(""));
        filterPanel.add(searchButton);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"Select", "Request ID", "Request Date", "Agent Name", "Request Type", "Status"};
        this.tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        this.resultsTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions for Selected Requests"));

        actionPanel.add(new JLabel("Change status to:"));
        newStatusComboBox = new JComboBox<>(new Request.Status[] {
                Request.Status.approved,
                Request.Status.cancelled,
                Request.Status.completed
        });
        actionPanel.add(newStatusComboBox);

        updateButton = new JButton("Update Selected");
        actionPanel.add(updateButton);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> loadRequestData());

        updateButton.addActionListener(e -> updateSelectedRequests());

        setContentPane(mainPanel);

        loadRequestData();
    }


    private void loadRequestData() {
        System.out.println("Buscando datos con filtros...");
        try {
             Request.Status statusFilter = (Request.Status) statusComboBox.getSelectedItem();
            String agentSearchTextFilter = agentSearchField.getText();

            String lobFilter = (String) lobComboBox.getSelectedItem();

            LocalDate dateFromFilter = null;
            if (!dateFromField.getText().trim().isEmpty()) {
                try {
                    dateFromFilter = LocalDate.parse(dateFromField.getText().trim());
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid 'Date From' format. Please use yyyy-MM-dd.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            List<Request> requests = requestService.findRequests(statusFilter, dateFromFilter, agentSearchTextFilter, lobFilter);

            updateTable(requests);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedRequests() {
        Request.Status newStatus = (Request.Status) newStatusComboBox.getSelectedItem();

        List<String> selectedRequestIds = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected) {
                String requestId = (String) tableModel.getValueAt(i, 1);
                selectedRequestIds.add(requestId);
            }
        }

        if (selectedRequestIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one request to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }



        JOptionPane.showMessageDialog(this, selectedRequestIds.size() + " requests have been updated to " + newStatus);

        loadRequestData();
    }

    private void updateTable(List<Request> requests) {
        tableModel.setRowCount(0);

        if (requests == null || requests.isEmpty()) {
            System.out.println("No requests found matching criteria.");
            return;
        }

        for (Request req : requests) {
            String agentName = (req.getAgent() != null && req.getAgent().getUser() != null)
                    ? req.getAgent().getUser().getFullName() : "N/A";

            Object[] rowData = {
                    false,
                    req.getRequestId(),
                    req.getRequestDate(),
                    agentName,
                    req.getRequestType(),
                    req.getStatus()
            };
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageRequestsView().setVisible(true));
    }
}