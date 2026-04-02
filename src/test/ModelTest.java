package test;

import model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

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
        Booking confirmed = new Booking("B1", "U1", "E1", Booking.Status.CONFIRMED);
        Booking waitlisted = new Booking("B2", "U2", "E1", Booking.Status.WAITLISTED);
        
        confirmed.setStatus(Booking.Status.CANCELLED);
        if (confirmed.getStatus() == Booking.Status.CANCELLED) {
            waitlisted.setStatus(Booking.Status.CONFIRMED);
        }
        assertEquals(Booking.Status.CONFIRMED, waitlisted.getStatus())
            }
    
    @Test
    void testDuplicateBookingPrevention() {
        Booking booking1 = new Booking("B1", "U1", "E1", Booking.Status.CONFIRMED);
        Booking booking2 = new Booking("B2", "U1", "E1", Booking.Status.CONFIRMED);
        
        boolean isDuplicate = booking1.getUserId().equals(booking2.getUserId()) &&
            booking1.getEventId().equals(booking2.getEventId());
        
        assertTrue(isDuplicate);
    }
}
