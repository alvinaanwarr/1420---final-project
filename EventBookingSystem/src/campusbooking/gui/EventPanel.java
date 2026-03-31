package campusbooking.gui;

import javax.swing.*;
import java.awt.*;
import service.EventService;

public class EventPanel extends JPanel {

    private JTextField idField       = new JTextField();
    private JTextField titleField    = new JTextField();
    private JTextField dateField     = new JTextField();
    private JTextField locationField = new JTextField();
    private JTextField capacityField = new JTextField();
    private JTextField searchField   = new JTextField();
    private JComboBox<String> typeBox       = new JComboBox<>(new String[]{"Workshop", "Seminar", "Concert"});
    private JComboBox<String> filterTypeBox = new JComboBox<>(new String[]{"All", "Workshop", "Seminar", "Concert"});
    private JTextArea eventList      = new JTextArea();

    public EventPanel() { this(new EventService()); }

    public EventPanel(EventService service) {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 2));
        form.add(new JLabel("Event ID"));        form.add(idField);
        form.add(new JLabel("Title"));           form.add(titleField);
        form.add(new JLabel("Date (yyyyMMdd)")); form.add(dateField);
        form.add(new JLabel("Location"));        form.add(locationField);
        form.add(new JLabel("Capacity"));        form.add(capacityField);
        form.add(new JLabel("Event Type"));      form.add(typeBox);
        form.add(new JButton("Create Event") {{
            addActionListener(e -> onCreate(service));
        }});
        form.add(new JLabel());

        JPanel searchPanel = new JPanel(new GridLayout(1, 3));
        searchPanel.add(searchField);
        searchPanel.add(filterTypeBox);
        searchPanel.add(new JButton("Search") {{
            addActionListener(e -> {
                String q    = searchField.getText().trim();
                String type = (String) filterTypeBox.getSelectedItem();
                var results = service.search(q, type);
                StringBuilder sb = new StringBuilder();
                if (results.isEmpty()) { sb.append("No events found."); }
                else for (var ev : results) sb.append(formatEvent(ev)).append("\n");
                eventList.setText(sb.toString());
            });
        }});

        eventList.setEditable(false);
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(eventList), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        var all = service.search("", "All");
        StringBuilder sb = new StringBuilder();
        if (all.isEmpty()) sb.append("No events found.");
        else for (var ev : all) sb.append(formatEvent(ev)).append("\n");
        eventList.setText(sb.toString());
    }

    private void onCreate(EventService service) {
        String id       = idField.getText().trim();
        String title    = titleField.getText().trim();
        String raw      = dateField.getText().trim();
        String location = locationField.getText().trim();
        String capacity = capacityField.getText().trim();
        String type     = (String) typeBox.getSelectedItem();

        if (id.isBlank() || title.isBlank() || raw.isBlank() ||
                location.isBlank() || capacity.isBlank()) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return;
        }
        if (raw.length() != 8) {
            JOptionPane.showMessageDialog(null, "Date must be 8 digits e.g. 20260301");
            return;
        }

        String date = raw.substring(0,4) + "-" + raw.substring(4,6) + "-" + raw.substring(6,8);
        JOptionPane.showMessageDialog(null, service.addEvent(id, title,
                date + "T00:00", location, capacity, type, type));

        idField.setText(""); titleField.setText(""); dateField.setText("");
        locationField.setText(""); capacityField.setText("");

        var all = service.search("", "All");
        StringBuilder sb = new StringBuilder();
        if (all.isEmpty()) sb.append("No events found.");
        else for (var ev : all) sb.append(formatEvent(ev)).append("\n");
        eventList.setText(sb.toString());
    }

    private String formatEvent(model.Event ev) {
        String detail = switch (ev.getEventType()) {
            case "Workshop" -> "Topic: "   + ((model.Workshop) ev).getTopic();
            case "Seminar"  -> "Speaker: " + ((model.Seminar) ev).getSpeakerName();
            default         -> "Age: "     + ((model.Concert) ev).getAgeRestriction();
        };
        return "ID: " + ev.getEventId() + " | " + ev.getTitle() + " | " +
                ev.getDateTime() + " | " + ev.getLocation() + " | Cap: " +
                ev.getCapacity() + " | " + ev.getStatus() + " | " + detail;
    }
}