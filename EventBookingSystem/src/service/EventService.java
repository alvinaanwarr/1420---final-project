package service;

import model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private List<Event> events = new ArrayList<>();
    private static final String FILE = "src/DataSet/events.csv";

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
            case "Concert" -> event = new Concert(
                    id, title, dateTime, location, capacity,
                    (typeSpecific == null || typeSpecific.isBlank()) ? "All Ages" : typeSpecific
            );
            default -> {
                return "Error: Invalid event type.";
            }
        }

        if (title.toLowerCase().startsWith("cancelled")) {
            event.setStatus(EventStatus.CANCELLED);
        }

        events.add(event);
        save();
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
        events.clear();

        File f = new File(FILE);
        if (!f.exists()) {
            System.out.println("events.csv not found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 10) continue;

                String eventId = p[0].trim();
                String title = p[1].trim();
                LocalDateTime dt = LocalDateTime.parse(p[2].trim());
                String location = p[3].trim();
                int capacity = Integer.parseInt(p[4].trim());
                String statusStr = p[5].trim();
                String eventType = p[6].trim();
                String topic = p[7].trim();
                String speakerName = p[8].trim();
                String ageRestriction = p[9].trim();

                Event event = switch (eventType) {
                    case "Workshop" -> new Workshop(eventId, title, dt, location, capacity, topic);
                    case "Seminar" -> new Seminar(eventId, title, dt, location, capacity, speakerName);
                    case "Concert" -> new Concert(eventId, title, dt, location, capacity, ageRestriction);
                    default -> new Workshop(eventId, title, dt, location, capacity, topic);
                };

                if (statusStr.equalsIgnoreCase("Cancelled")) {
                    event.setStatus(EventStatus.CANCELLED);
                } else {
                    event.setStatus(EventStatus.ACTIVE);
                }

                events.add(event);
            }

            System.out.println("Loaded " + events.size() + " events from CSV.");
        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.println("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");

            for (Event e : events) {
                String topic = "";
                String speakerName = "";
                String ageRestriction = "";

                if (e instanceof Workshop w) {
                    topic = w.getTopic();
                } else if (e instanceof Seminar s) {
                    speakerName = s.getSpeakerName();
                } else if (e instanceof Concert c) {
                    ageRestriction = c.getAgeRestriction();
                }

                String status = (e.getStatus() == EventStatus.CANCELLED) ? "Cancelled" : "Active";

                pw.println(
                        e.getEventId() + "," +
                                e.getTitle() + "," +
                                e.getDateTime() + "," +
                                e.getLocation() + "," +
                                e.getCapacity() + "," +
                                status + "," +
                                e.getEventType() + "," +
                                topic + "," +
                                speakerName + "," +
                                ageRestriction
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }
}