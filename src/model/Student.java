package model;

/**
 * Represents a Student user.
 */
public class Student extends User {

    public Student(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxConfirmedBookings() {
        return 3;
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}