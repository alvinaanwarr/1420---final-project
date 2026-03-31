package model;

/**
 * Represents a Staff user.
 */
public class Staff extends User {

    public Staff(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxConfirmedBookings() {
        return 5;
    }

    @Override
    public String getUserType() {
        return "Staff";
    }
}