package campusbooking.gui;

import javax.swing.*;
import java.awt.*;
import service.BookingService;

public class WaitlistPanel extends JPanel {

    private JTextField eventIdField = new JTextField();
    private JTextArea  resultArea   = new JTextArea();

    public WaitlistPanel() { this(new BookingService()); }

    public WaitlistPanel(BookingService service) {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 2));
        form.add(new JLabel("Event ID"));
        form.add(eventIdField);

        form.add(new JButton("View Waitlist") {{
            addActionListener(e -> {
                String eventId = eventIdField.getText().trim();

                if (eventId.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Enter an Event ID.");
                    return;
                }

                var results = service.getWaitlistByEventId(eventId);

                StringBuilder sb = new StringBuilder();
                if (results.isEmpty()) {
                    sb.append("No waitlisted bookings for this event.");
                } else {
                    for (var b : results) {
                        sb.append(b.getBookingId()).append(" | ")
                                .append(b.getUserId()).append(" | ")
                                .append(b.getCreatedAt()).append("\n");
                    }
                }

                resultArea.setText(sb.toString());
            });
        }});

        form.add(new JLabel());

        resultArea.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }
}