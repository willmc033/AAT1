package org.example.view;

import org.example.model.Agent;
import org.example.model.ManagementUnit;
import org.example.model.Request;
import org.example.service.AgentService;
import org.example.service.ManagementUnitService;
import org.example.service.RequestService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddPtoView extends JFrame {


    private JComboBox<ManagementUnit> muComboBox;
    private JComboBox<Agent> agentComboBox;
    private JComboBox<Request.RequestType> typeComboBox;
    private JList<LocalDate> dateList;
    private DefaultListModel<LocalDate> dateListModel;
    private JButton submitButton;

    private final ManagementUnitService muService = new ManagementUnitService();
    private final AgentService agentService = new AgentService();
    private final RequestService requestService = new RequestService();

    public AddPtoView() {
        setTitle("Add PTO Request");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel muPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        muPanel.add(new JLabel("Management Unit:"));
        muComboBox = new JComboBox<>();
        muPanel.add(muComboBox);

        JPanel agentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agentPanel.add(new JLabel("Agent:"));
        agentComboBox = new JComboBox<>();
        agentPanel.add(agentComboBox);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Request Type:"));
        typeComboBox = new JComboBox<>(Request.RequestType.values());
        typePanel.add(typeComboBox);

        formPanel.add(muPanel);
        formPanel.add(agentPanel);
        formPanel.add(typePanel);

        dateListModel = new DefaultListModel<>();
        dateList = new JList<>(dateListModel);

        dateList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LocalDate) {
                    setText(((LocalDate) value).format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
                }
                return renderer;
            }
        });

        JScrollPane dateScrollPane = new JScrollPane(dateList);
        dateScrollPane.setBorder(BorderFactory.createTitledBorder("Select Dates"));

        submitButton = new JButton("Submit Request");

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(dateScrollPane, BorderLayout.CENTER);
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        setContentPane(mainPanel);


        muComboBox.addActionListener(e -> onMuSelected());
        submitButton.addActionListener(e -> onSubmit());

         loadInitialData();
    }

    private void loadInitialData() {
        List<ManagementUnit> mus = muService.findAll();
        muComboBox.removeAllItems();
        muComboBox.addItem(null);
        for (ManagementUnit mu : mus) {
            muComboBox.addItem(mu);
        }

        for (int i = 0; i < 30; i++) {
            dateListModel.addElement(LocalDate.now().plusDays(i));
        }
    }

    private void onMuSelected() {
        ManagementUnit selectedMu = (ManagementUnit) muComboBox.getSelectedItem();
        agentComboBox.removeAllItems();

        if (selectedMu != null) {
            List<Agent> agents = agentService.findAgentsByMu(selectedMu.getMuId());
            for (Agent agent : agents) {
                agentComboBox.addItem(agent);
            }
        }
    }

    private void onSubmit() {
        Agent selectedAgent = (Agent) agentComboBox.getSelectedItem();
        Request.RequestType selectedType = (Request.RequestType) typeComboBox.getSelectedItem();
        List<LocalDate> selectedDates = dateList.getSelectedValuesList();


        if (selectedAgent == null) {
            JOptionPane.showMessageDialog(this, "Please select an agent.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedDates.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            for (LocalDate date : selectedDates) {
                requestService.createNewRequest(selectedAgent, date, selectedType);
            }
            JOptionPane.showMessageDialog(this, "Successfully submitted " + selectedDates.size() + " request(s) for " + selectedAgent.getUser().getFullName(), "Success", JOptionPane.INFORMATION_MESSAGE);
             this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while submitting the requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddPtoView().setVisible(true));
    }
}