package model;

import java.time.LocalDateTime;

/**
 * Abstract base class representing a campus event.
 */
public abstract class Event {

    private final String eventId;
    private final String title;
    private final LocalDateTime dateTime;
    private final String location;
    private final int capacity;
    private EventStatus status;

    public Event(String eventId, String title, LocalDateTime dateTime,
                 String location, int capacity) {

        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("Event ID cannot be empty.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }

        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.capacity = capacity;
        this.status = EventStatus.ACTIVE;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public abstract String getEventType();
}