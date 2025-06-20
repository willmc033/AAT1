package org.example.view;

import org.example.model.Request;
import org.example.service.RequestService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleView extends JFrame {

    private JComboBox<String> lobComboBox;
    private JPanel calendarPanel;
    private JLabel monthLabel;

    private final RequestService requestService = new RequestService();

    private LocalDate currentDate;

    public ScheduleView() {
        setTitle("Workforce Schedule");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        currentDate = LocalDate.now();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel lobPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lobPanel.add(new JLabel("Line of Work:"));
        lobComboBox = new JComboBox<>(new String[]{"All", "Mob", "EPP", "Sales", "SOS", "FFH"});
        lobPanel.add(lobComboBox);

        JPanel monthNavigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevMonthButton = new JButton("<");
        JButton nextMonthButton = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        monthNavigationPanel.add(prevMonthButton);
        monthNavigationPanel.add(monthLabel);
        monthNavigationPanel.add(nextMonthButton);

        topPanel.add(monthNavigationPanel, BorderLayout.CENTER);
        topPanel.add(lobPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        mainPanel.add(new JScrollPane(calendarPanel), BorderLayout.CENTER);

        setContentPane(mainPanel);

        lobComboBox.addActionListener(e -> refreshSchedule());
        prevMonthButton.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            refreshSchedule();
        });
        nextMonthButton.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            refreshSchedule();
        });

        refreshSchedule();
    }

    private void refreshSchedule() {
        String selectedLob = (String) lobComboBox.getSelectedItem();

        monthLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        List<Request> requests = requestService.findRequests(null, null, null, selectedLob);

        Map<LocalDate, List<Request>> requestsByDate = requests.stream()
                .collect(Collectors.groupingBy(Request::getRequestDate));

        drawCalendar(requestsByDate);
    }

    private void drawCalendar(Map<LocalDate, List<Request>> requestsByDate) {

        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : daysOfWeek) {
            JLabel header = new JLabel(day, SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 14));
            calendarPanel.add(header);
        }

        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue();
        int emptySlots = dayOfWeekValue % 7;

        for (int i = 0; i < emptySlots; i++) {
            calendarPanel.add(new JPanel());
        }

        int daysInMonth = currentDate.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentDate.withDayOfMonth(day);

            JPanel dayCell = new JPanel(new BorderLayout());
            dayCell.setBorder(new LineBorder(Color.LIGHT_GRAY));
            dayCell.setBackground(Color.WHITE);

            JLabel dayLabel = new JLabel(" " + day);
            dayCell.add(dayLabel, BorderLayout.NORTH);

            if (requestsByDate.containsKey(date)) {
                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (Request r : requestsByDate.get(date)) {
                    listModel.addElement(r.getAgent().getUser().getFullName() + " (" + r.getRequestType() + ")");
                }
                JList<String> requestsList = new JList<>(listModel);
                requestsList.setFont(new Font("Arial", Font.PLAIN, 10));
                dayCell.add(new JScrollPane(requestsList), BorderLayout.CENTER);
            }

            calendarPanel.add(dayCell);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScheduleView().setVisible(true));
    }
}