package campusbooking.gui;

import model.User;
import model.Event;
import model.Booking;
import java.util.*;

public class BookingSystem {

    private Map<String, User> users = new HashMap<>();
    private Map<String, Event> events = new HashMap<>();
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Adds a user (Student, Staff, or Guest) to the system.
     */
    public void addUser(User user) {
        if (user != null) {
            users.put(user.getUserId(), user);
            // Debugging print to console
            System.out.println("System: Stored " + user.getUserType() + " [" + user.getName() + "]");
        }
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUserById(String id) {
        return users.get(id);
    }


}
