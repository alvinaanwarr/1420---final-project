package model;

import java.time.LocalDateTime;

/**
 * Represents a Seminar event.
 */
public class Seminar extends Event {

    private final String speakerName;

    public Seminar(String eventId, String title, LocalDateTime dateTime,
                   String location, int capacity, String speakerName) {
        super(eventId, title, dateTime, location, capacity);
        this.speakerName = speakerName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    @Override
    public String getEventType() {
        return "Seminar";
    }
}