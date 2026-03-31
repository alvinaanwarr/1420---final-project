import javax.swing.*;
import java.awt.*;

import campusbooking.gui.BookingPanel;
import campusbooking.gui.EventPanel;
import campusbooking.gui.UserPanel;
import campusbooking.gui.WaitlistPanel;
import service.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Campus Event Booking System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UserService userService       = new UserService();
        EventService eventService     = new EventService();
        BookingService bookingService = new BookingService();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("User Management",     new UserPanel(userService));
        tabs.add("Event Management",    new EventPanel(eventService));
        tabs.add("Booking Management",  new BookingPanel(bookingService, userService, eventService));
        tabs.add("Waitlist Management", new WaitlistPanel(bookingService));
        add(tabs, BorderLayout.CENTER);
    }
}