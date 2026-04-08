package campusbooking.gui;

import javax.swing.*;
import java.awt.*;
import model.BookingStatus;
import service.*;
import java.util.List;
import model.Booking;

public class BookingPanel extends JPanel {

    private JTextField userIdField    = new JTextField();
    private JTextField eventIdField   = new JTextField();
    private JTextField bookingIdField = new JTextField();
    private JTextField searchField    = new JTextField();
    private JComboBox<String> statusFilter =
            new JComboBox<>(new String[]{"All", "CONFIRMED", "WAITLISTED", "CANCELLED"});
    private JTextArea resultArea = new JTextArea();

    public BookingPanel() { this(new BookingService(), new UserService(), new EventService()); }

    public BookingPanel(BookingService bookingService,
                        UserService userService, EventService eventService) {
        setLayout(new BorderLayout());

        JPanel bookForm = new JPanel(new GridLayout(3, 2));
        bookForm.setBorder(BorderFactory.createTitledBorder("New Booking"));
        bookForm.add(new JLabel("User ID"));  bookForm.add(userIdField);
        bookForm.add(new JLabel("Event ID")); bookForm.add(eventIdField);
        bookForm.add(new JLabel(""));
        bookForm.add(new JButton("Book Event") {{
            addActionListener(e -> {
                String uid = userIdField.getText().trim();
                String eid = eventIdField.getText().trim();
                if (uid.isBlank() || eid.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Both User ID and Event ID are required.");
                    return;
                }
                String result = bookingService.book(uid, eid, userService, eventService);
                JOptionPane.showMessageDialog(null, result);
                userIdField.setText("");
                eventIdField.setText("");
                refreshList(bookingService, "", "All");
            });
        }});

        JPanel cancelForm = new JPanel(new GridLayout(2, 2));
        cancelForm.setBorder(BorderFactory.createTitledBorder("Cancel Booking"));
        cancelForm.add(new JLabel("Booking ID")); cancelForm.add(bookingIdField);
        cancelForm.add(new JLabel(""));
        cancelForm.add(new JButton("Cancel Booking") {{
            addActionListener(e -> {
                String bid = bookingIdField.getText().trim();
                if (bid.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Enter a Booking ID to cancel.");
                    return;
                }
                String result = bookingService.cancel(bid);
                JOptionPane.showMessageDialog(null, result);
                bookingIdField.setText("");
                refreshList(bookingService, "", "All");
            });
        }});

        JPanel topForms = new JPanel(new GridLayout(2, 1));
        topForms.add(bookForm);
        topForms.add(cancelForm);
// search features
        JPanel searchPanel = new JPanel(new GridLayout(1, 3));
        searchPanel.add(searchField);
        searchPanel.add(statusFilter);
        new JButton("Search") {{
            addActionListener(e -> refreshList(
                    bookingService,
                    searchField.getText().trim(),
                    (String) statusFilter.getSelectedItem()
            ));
        }}.add(searchPanel);

        resultArea.setEditable(false);
        add(topForms, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        refreshList(bookingService, "", "All");
    }

    private void refreshList(BookingService service, String query, String statusStr) {
        List<Booking> results = query.isBlank()
                ? service.getAllBookings()
                : service.search(query);

        if (!statusStr.equals("All")) {
            BookingStatus filterStatus = BookingStatus.valueOf(statusStr);
            results = results.stream()
                    .filter(b -> b.getBookingStatus() == filterStatus)
                    .toList();
        }

        StringBuilder sb = new StringBuilder();
        if (results.isEmpty()) sb.append("No bookings found.");
        else for (var b : results)
            sb.append(b.getBookingId()).append(" | ")
                    .append(b.getUserId()).append(" | ")
                    .append(b.getEventId()).append(" | ")
                    .append(b.getCreatedAt()).append(" | ")
                    .append(formatStatus(b.getBookingStatus())).append("\n");
        resultArea.setText(sb.toString());
    }

    private String formatStatus(BookingStatus status) {
        return switch (status) {
            case CONFIRMED -> "Confirmed";
            case WAITLISTED -> "Waitlisted";
            case CANCELLED -> "Cancelled";
        };
    }
}