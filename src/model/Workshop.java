package model;

import java.time.LocalDateTime;

/**
 * Represents a Workshop event.
 */
public class Workshop extends Event {

    private String topic;

    public Workshop(String eventId, String title, LocalDateTime dateTime,
                    String location, int capacity, String topic) {
        super(eventId, title, dateTime, location, capacity);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String getEventType() {
        return "Workshop";
    }
}
