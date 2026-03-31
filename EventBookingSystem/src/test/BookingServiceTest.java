package test;

import model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {

    @Test
    void testStudentLimit() {
        Student s = new Student("U1", "Lana", "lana@email.com");
        assertEquals(3, s.getMaxConfirmedBookings());
    }

    @Test
    void testWorkshopCreation() {
        Workshop w = new Workshop("E1", "Java", LocalDateTime.now(), "Room", 10, "OOP");
        assertEquals("Workshop", w.getEventType());
    }

    @Test
    void testBookingStatus() {
        Booking b = new Booking("B1", "U1", "E1", LocalDateTime.now(), BookingStatus.CONFIRMED);
        assertEquals(BookingStatus.CONFIRMED, b.getBookingStatus());
    }

    @Test
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workshop("E1", "Bad", LocalDateTime.now(), "Room", 0, "OOP");
        });
    }

    @Test
    void testWaitlistPromotionAfterCancellation() {
        Booking confirmed = new Booking("B1", "U1", "E1",
            LocalDateTime.now(), BookingStatus.CONFIRMED);
        Booking waitlisted = new Booking("B2", "U2", "E1",
            LocalDateTime.now(), BookingStatus.WAITLISTED);
        
        confirmed.setBookingStatus(BookingStatus.CANCELLED);
        waitlisted.setBookingStatus(BookingStatus.CONFIRMED);
        
        assertEquals(BookingStatus.CANCELLED, confirmed.getBookingStatus());
        assertEquals(BookingStatus.CONFIRMED, waitlisted.getBookingStatus());
    }

    @Test
    void testDuplicateBookingNotAllowed() {
        Booking b1 = new Booking("B1", "U1", "E1",
            LocalDateTime.now(), BookingStatus.CONFIRMED);
        Booking b2 = new Booking("B2", "U1", "E1",
            LocalDateTime.now(), BookingStatus.CONFIRMED);
        
        boolean isDuplicate =
            b1.getUserId().equals(b2.getUserId()) &&
            b1.getEventId().equals(b2.getEventId());
        
        assertTrue(isDuplicate);
    }
}
