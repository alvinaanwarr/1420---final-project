package model;

/**
 * Abstract base class representing a system user.
 */
public abstract class User {

    private final String userId;
    private String name;
    private String email;

    public User(String userId, String name, String email) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be empty.");
        }
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract int getMaxConfirmedBookings();

    public abstract String getUserType();
}
