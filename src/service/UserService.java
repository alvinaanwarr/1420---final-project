package service;

import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users = new ArrayList<>();
    private static final String FILE = "src/DataSet/users.csv";

    public UserService() {
        load();
    }


    public String addUser(String id, String name, String email, String type) {
        for (User u : users) {
            if (u.getUserId().equals(id)) {
                return "Error: User ID already exists.";
            }
        }

        User user = switch (type) {
            case "Student" -> new Student(id, name, email);
            case "Staff" -> new Staff(id, name, email);
            default -> new Guest(id, name, email);
        };

        users.add(user);
        save();
        return "User added successfully.";
    }

    public User findById(String id) {
        return users.stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public List<User> search(String query) {
        String q = (query == null) ? "" : query.toLowerCase();

        return users.stream()
                .filter(u -> q.isBlank()
                        || u.getUserId().toLowerCase().contains(q)
                        || u.getName().toLowerCase().contains(q)
                        || u.getEmail().toLowerCase().contains(q))
                .toList();
    }

    private void load() {
        users.clear();

        File f = new File(FILE);
        if (!f.exists()) {
            System.out.println("users.csv not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;

                String userId = p[0].trim();
                String name = p[1].trim();
                String email = p[2].trim();
                String userType = p[3].trim();

                User u = switch (userType) {
                    case "Student" -> new Student(userId, name, email);
                    case "Staff" -> new Staff(userId, name, email);
                    default -> new Guest(userId, name, email);
                };

                users.add(u);
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.println("userId,name,email,userType");
            for (User u : users) {
                pw.println(u.getUserId() + "," +
                        u.getName() + "," +
                        u.getEmail() + "," +
                        u.getUserType());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}