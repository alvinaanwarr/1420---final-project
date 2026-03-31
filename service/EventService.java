package service;

import model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private List<Event> events = new ArrayList<>();
    private static final String FILE = "events.csv";

    public EventService() {
        load();
    }

    public String addEvent(String id, String title, String dateTimeStr,
                           String location, String capacityStr,
                           String type, String typeSpecific) {
        for (Event e : events) {
            if (e.getEventId().equals(id)) {
                return "Error: Event ID already exists.";
            }
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr.trim());
        } catch (NumberFormatException e) {
            return "Error: Capacity must be a number.";
        }

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeStr.trim());
        } catch (Exception e) {
            return "Error: Invalid date format.";
        }

        Event event;
        switch (type) {
            case "Workshop" -> event = new Workshop(
                    id, title, dateTime, location, capacity,
                    (typeSpecific == null || typeSpecific.isBlank()) ? title : typeSpecific
            );
            case "Seminar" -> event = new Seminar(
                    id, title, dateTime, location, capacity,
                    (typeSpecific == null || typeSpecific.isBlank()) ? "TBA Speaker" : typeSpecific
            );
            default -> event = new Concert(
                    id, title, dateTime, location, capacity,
                    (typeSpecific == null || typeSpecific.isBlank()) ? "All Ages" : typeSpecific
            );
        }

        if (title.toLowerCase().startsWith("cancelled")) {
            event.setStatus(EventStatus.CANCELLED);
        }

        events.add(event);

        // no save() so original CSV is not overwritten
        return "Event created successfully.";
    }

    public Event findById(String id) {
        return events.stream()
                .filter(e -> e.getEventId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Event> getAllEvents() {
        return events;
    }

    public List<Event> search(String query, String type) {
        String q = (query == null) ? "" : query.toLowerCase();

        return events.stream()
                .filter(e -> q.isBlank()
                        || e.getEventId().toLowerCase().contains(q)
                        || e.getTitle().toLowerCase().contains(q))
                .filter(e -> type.equals("All") || e.getEventType().equals(type))
                .toList();
    }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) {
            System.out.println("events.csv not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;

                String eventId = p[0].trim();
                String title = p[1].trim();
                LocalDateTime dt = LocalDateTime.parse(p[2].trim());
                String location = p[3].trim();
                int capacity = Integer.parseInt(p[4].trim());

                Event event = createEventFromCsv(eventId, title, dt, location, capacity);

                if (title.toLowerCase().startsWith("cancelled")) {
                    event.setStatus(EventStatus.CANCELLED);
                }

                events.add(event);
            }

            System.out.println("Loaded " + events.size() + " events from CSV.");
        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }

    private Event createEventFromCsv(String id, String title, LocalDateTime dt,
                                     String location, int capacity) {
        String lowerTitle = title.toLowerCase();

        if (lowerTitle.contains("concert")
                || lowerTitle.contains("jazz")
                || lowerTitle.contains("music")
                || lowerTitle.contains("showcase")) {
            return new Concert(id, title, dt, location, capacity, "All Ages");
        } else if (lowerTitle.contains("seminar")
                || lowerTitle.contains("talk")) {
            return new Seminar(id, title, dt, location, capacity, "TBA Speaker");
        } else {
            return new Workshop(id, title, dt, location, capacity, title);
        }
    }
}