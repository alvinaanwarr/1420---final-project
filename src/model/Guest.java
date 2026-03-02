package model;

/**
 * Represents a Guest user.
 */
public class Guest extends User {

    public Guest(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxConfirmedBookings() {
        return 1;
    }

    @Override
    public String getUserType() {
        return "Guest";
    }
}
