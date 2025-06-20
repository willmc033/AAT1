package org.example.view;

import org.example.model.Agent;
import org.example.model.ManagementUnit;
import org.example.model.Request;
import org.example.service.AgentService;
import org.example.service.ManagementUnitService;
import org.example.service.RequestService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpdatePtoView extends JFrame {
    private JComboBox<ManagementUnit> muComboBox;
    private JComboBox<Agent> agentComboBox;
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton submitButton;

    private final ManagementUnitService muService = new ManagementUnitService();
    private final AgentService agentService = new AgentService();
    private final RequestService requestService = new RequestService();

    public UpdatePtoView() {
        setTitle("Update/Cancel PTO");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Select Agent"));

        filtersPanel.add(new JLabel("MU:"));
        muComboBox = new JComboBox<>();
        filtersPanel.add(muComboBox);

        filtersPanel.add(new JLabel("Agent:"));
        agentComboBox = new JComboBox<>();
        filtersPanel.add(agentComboBox);

        mainPanel.add(filtersPanel, BorderLayout.NORTH);

        String[] columnNames = {"Request ID", "Type", "Date", "Status", "Cancel?"};
        this.tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        requestsTable = new JTable(tableModel);
        mainPanel.add(new JScrollPane(requestsTable), BorderLayout.CENTER);

        submitButton = new JButton("Submit Cancellations");
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        muComboBox.addActionListener(e -> onMuSelected());
        agentComboBox.addActionListener(e -> onAgentSelected());
        submitButton.addActionListener(e -> onSubmit());

        loadMUs();
    }

    private void loadMUs() {
        List<ManagementUnit> mus = muService.findAll();
        muComboBox.removeAllItems();
        muComboBox.addItem(null);
        for (ManagementUnit mu : mus) {
            muComboBox.addItem(mu);
        }
    }

    private void onMuSelected() {
        ManagementUnit selectedMu = (ManagementUnit) muComboBox.getSelectedItem();
        agentComboBox.removeAllItems();
        tableModel.setRowCount(0);
        if (selectedMu != null) {
            List<Agent> agents = agentService.findAgentsByMu(selectedMu.getMuId());
            for (Agent agent : agents) {
                agentComboBox.addItem(agent);
            }
        }
    }

    private void onAgentSelected() {
        Agent selectedAgent = (Agent) agentComboBox.getSelectedItem();
        tableModel.setRowCount(0);
        if (selectedAgent != null) {
            List<Request> requests = requestService.findRequests(null, null, selectedAgent.getEmployeeNumber(), null);
            updateTable(requests);
        }
    }

    private void updateTable(List<Request> requests) {
        tableModel.setRowCount(0);
        for (Request req : requests) {
            if (req.getStatus() != Request.Status.cancelled && req.getStatus() != Request.Status.completed) {
                Object[] rowData = {
                        req.getRequestId(),
                        req.getRequestType(),
                        req.getRequestDate(),
                        req.getStatus(),
                        false
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void onSubmit() {
        List<String> idsToCancel = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 4);
            if (isSelected) {
                String requestId = (String) tableModel.getValueAt(i, 0);
                idsToCancel.add(requestId);
            }
        }

        if (idsToCancel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please check the box for at least one request to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            requestService.cancelRequests(idsToCancel);
            JOptionPane.showMessageDialog(this, "Cancellation requests processed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            onAgentSelected();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdatePtoView().setVisible(true));
    }
}