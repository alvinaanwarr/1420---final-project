package campusbooking.gui;

import javax.swing.*;
import java.awt.*;
import service.UserService;

public class UserPanel extends JPanel {

    private JTextField idField        = new JTextField();
    private JTextField nameField      = new JTextField();
    private JTextField emailField     = new JTextField();
    private JTextField searchField    = new JTextField();
    private JComboBox<String> typeBox = new JComboBox<>(new String[]{"Student", "Staff", "Guest"});
    private JComboBox<String> filterTypeBox =
            new JComboBox<>(new String[]{"All", "Student", "Staff", "Guest"});
    private JTextArea userList        = new JTextArea();

    public UserPanel() { this(new UserService()); }

    public UserPanel(UserService service) {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2));
        form.add(new JLabel("User ID"));   form.add(idField);
        form.add(new JLabel("Name"));      form.add(nameField);
        form.add(new JLabel("Email"));     form.add(emailField);
        form.add(new JLabel("User Type")); form.add(typeBox);
        form.add(new JButton("Add User") {{
            addActionListener(e -> {
                String id    = idField.getText().trim();
                String name  = nameField.getText().trim();
                String email = emailField.getText().trim();
                String type  = (String) typeBox.getSelectedItem();
                if (id.isBlank() || name.isBlank() || email.isBlank()) {
                    JOptionPane.showMessageDialog(null, "All fields are required.");
                    return;
                }
                JOptionPane.showMessageDialog(null, service.addUser(id, name, email, type));
                idField.setText(""); nameField.setText(""); emailField.setText("");
                refreshList(service, "", "All");
            });
        }});
        form.add(new JLabel());
// search features
        JPanel searchPanel = new JPanel(new GridLayout(1, 3));
        searchPanel.add(searchField);
        searchPanel.add(filterTypeBox);
        searchPanel.add(new JButton("Search") {{
            addActionListener(e -> refreshList(
                    service,
                    searchField.getText().trim(),
                    (String) filterTypeBox.getSelectedItem()
            ));
        }});

        userList.setEditable(false);
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(userList), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        refreshList(service, "", "All");
    }

    private void refreshList(UserService service, String query, String typeStr) {
        var results = query.isBlank() ? service.getAllUsers() : service.search(query);

        if (!typeStr.equals("All")) {
            results = results.stream()
                    .filter(u -> u.getUserType().equals(typeStr))
                    .toList();
        }

        StringBuilder sb = new StringBuilder();
        if (results.isEmpty()) { sb.append("No users found."); }
        else for (var u : results)
            sb.append(u.getUserId()).append(" | ").append(u.getName())
                    .append(" | ").append(u.getEmail())
                    .append(" | ").append(u.getUserType()).append("\n");
        userList.setText(sb.toString());
    }
}