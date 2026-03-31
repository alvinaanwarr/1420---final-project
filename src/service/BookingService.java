package service;

import model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private List<Booking> bookings = new ArrayList<>();
    private int nextId = 1001;
    private static final String FILE = "src/DataSet/bookings.csv";

    public BookingService() {
        load();
    }


    public String book(String userId, String eventId,
                       UserService userService, EventService eventService) {
        User user = userService.findById(userId);
        if (user == null) return "Error: User not found.";

        Event event = eventService.findById(eventId);
        if (event == null) return "Error: Event not found.";

        if (event.getStatus() == EventStatus.CANCELLED)
            return "Error: Event is cancelled.";

        for (Booking b : bookings) {
            if (b.getUserId().equals(userId)
                    && b.getEventId().equals(eventId)
                    && b.getBookingStatus() != BookingStatus.CANCELLED) {
                return "Error: User already booked this event.";
            }
        }

        long confirmed = bookings.stream()
                .filter(b -> b.getUserId().equals(userId)
                        && b.getBookingStatus() == BookingStatus.CONFIRMED)
                .count();

        if (confirmed >= user.getMaxConfirmedBookings())
            return "Error: Booking limit reached for this user type.";

        long bookedCount = bookings.stream()
                .filter(b -> b.getEventId().equals(eventId)
                        && b.getBookingStatus() == BookingStatus.CONFIRMED)
                .count();

        BookingStatus status = bookedCount < event.getCapacity()
                ? BookingStatus.CONFIRMED
                : BookingStatus.WAITLISTED;

        String id = "B" + nextId++;
        bookings.add(new Booking(id, userId, eventId, LocalDateTime.now(), status));
        save();

        return "Booking " + formatStatus(status) + " (ID: " + id + ")";
    }

    public String cancel(String bookingId) {
        Booking target = bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);

        if (target == null) return "Error: Booking not found.";
        if (target.getBookingStatus() == BookingStatus.CANCELLED)
            return "Error: Already cancelled.";

        boolean wasConfirmed = target.getBookingStatus() == BookingStatus.CONFIRMED;
        target.setBookingStatus(BookingStatus.CANCELLED);

        if (wasConfirmed) {
            bookings.stream()
                    .filter(b -> b.getEventId().equals(target.getEventId())
                            && b.getBookingStatus() == BookingStatus.WAITLISTED)
                    .findFirst()
                    .ifPresent(b -> b.setBookingStatus(BookingStatus.CONFIRMED));
        }

        save();
        return "Booking cancelled.";
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public List<Booking> search(String query) {
        String q = (query == null) ? "" : query.toLowerCase();

        return bookings.stream()
                .filter(b -> q.isBlank()
                        || b.getBookingId().toLowerCase().contains(q)
                        || b.getUserId().toLowerCase().contains(q)
                        || b.getEventId().toLowerCase().contains(q))
                .toList();
    }

    public List<Booking> getWaitlistByEventId(String eventId) {
        return bookings.stream()
                .filter(b -> b.getEventId().equals(eventId)
                        && b.getBookingStatus() == BookingStatus.WAITLISTED)
                .toList();
    }

    private void load() {
        bookings.clear();

        File f = new File(FILE);
        if (!f.exists()) {
            System.out.println("bookings.csv not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 5) continue;

                String bookingId = p[0].trim();
                String userId = p[1].trim();
                String eventId = p[2].trim();
                LocalDateTime createdAt = LocalDateTime.parse(p[3].trim());
                BookingStatus status = parseStatus(p[4].trim());

                bookings.add(new Booking(bookingId, userId, eventId, createdAt, status));

                try {
                    int num = Integer.parseInt(bookingId.substring(1));
                    if (num >= nextId) {
                        nextId = num + 1;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.println("bookingId,userId,eventId,createdAt,bookingStatus");
            for (Booking b : bookings) {
                pw.println(b.getBookingId() + "," +
                        b.getUserId() + "," +
                        b.getEventId() + "," +
                        b.getCreatedAt() + "," +
                        formatStatus(b.getBookingStatus()));
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    private BookingStatus parseStatus(String value) {
        return switch (value.toLowerCase()) {
            case "confirmed" -> BookingStatus.CONFIRMED;
            case "waitlisted" -> BookingStatus.WAITLISTED;
            case "cancelled" -> BookingStatus.CANCELLED;
            default -> BookingStatus.CONFIRMED;
        };
    }

    private String formatStatus(BookingStatus status) {
        return switch (status) {
            case CONFIRMED -> "Confirmed";
            case WAITLISTED -> "Waitlisted";
            case CANCELLED -> "Cancelled";
        };
    }
}