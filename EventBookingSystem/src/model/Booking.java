package model;

import java.time.LocalDateTime;

/**
 * Represents a booking linking a user to an event.
 */
public class Booking {

    private final String bookingId;
    private final String userId;
    private final String eventId;
    private final LocalDateTime createdAt;
    private BookingStatus bookingStatus;

    public Booking(String bookingId, String userId, String eventId,
                   LocalDateTime createdAt, BookingStatus bookingStatus) {

        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID cannot be empty.");
        }

        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.bookingStatus = bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}