package model;

import java.time.LocalDateTime;

/**
 * Represents a Concert event.
 */
public class Concert extends Event {

    private String ageRestriction;

    public Concert(String eventId, String title, LocalDateTime dateTime,
                   String location, int capacity, String ageRestriction) {
        super(eventId, title, dateTime, location, capacity);
        this.ageRestriction = ageRestriction;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    @Override
    public String getEventType() {
        return "Concert";
    }
}
