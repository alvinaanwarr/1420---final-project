# Campus Event Booking System

## Project Overview
The Campus Event Booking System is a Java-based application designed to manage the lifecycle of campus events and user
reservations. The system enables administrators to create and manage users and events, while supporting booking
workflows that include confirmations, waitlists, cancellations, and automatic promotions.

The project demonstrates strong object-oriented design principles, including inheritance, encapsulation, and modular
architecture, and provides an interactive Swing-based graphical user interface (GUI) for system interaction.


## Team Members
- Eman Siddiqui
- Alvina Anwar
- Rinad Abu Zaid
- Hana Rave
- Lana Ghonem
- Sara Zevar


## System Features
#### User Management
- Supports three user types: Student, Staff, Guest
- Stores user information (ID, name, email)
- Enforces booking limits:
    - Student → 3 confirmed bookings
    - Staff → 5 confirmed bookings
    - Guest → 1 confirmed booking

#### Event Management
- Supports three event types:
    - Workshop (topic)
    - Seminar (speaker)
    - Concert (age restriction)
- Stores event details:
    - Event ID, title, date/time, location, capacity, status
- Validates input (e.g., capacity must be greater than 0)

#### Booking Management
- Allows users to book events
- Prevents duplicate bookings
- Assigns booking status:
    - Confirmed (if capacity available)
    - Waitlisted (if event is full)
    - Cancelled
- Enforces booking limits per user type

#### Waitlist Management
- Automatically places users on a waitlist when events are full
- Promotes the first waitlisted user when a confirmed booking is cancelled

#### GUI
- Implemented using Java Swing
- Organized into four main tabs:
    - UserPanel
    - Event Management
    - Booking Management
    - Waitlist Management
- Provides an intuitive interface for interacting with system features


## Data Set
The system uses predefined datasets:
- bookings.csv
- events.csv
- users.csv

#### Data Set Summary
- 24 users (Students, Staff, Guests)
- 12 events (Workshops, Seminars, Concerts)
- 42 bookings (Confirmed, Waitlisted, Cancelled)

#### Key Rules
- User IDs and Event IDs are unique
- Event capacity must be greater than 0
- Booking statuses include Confirmed, Waitlisted, and Cancelled
- Waitlist order is determined by booking timestamp

#### Data Set Error
An error occured while attempting to incorporate the data sets, in the EventService, BookingService, and UserService codes. The line <private static final String FILE = "scr/DataSet/events.csv"; > was added (same for booking and user, change to /booking.csv and /user.csv).


## JUnit Testing
This project contains a JUnit 5 test suite covering core system behaviour.

#### Implemented Tests
- User booking limits (Student, Staff, Guest)
- Event validation (invalid capacity handling)
- Booking object correctness
- Event type verification

#### Workflow Tests
- Booking under capacity → Confirmed
- Booking when full → Waitlisted
- Cancellation of confirmed booking → Waitlist promotion
- Duplicate booking prevention


## Technologies Used
- IntelliJ IDEA (Development environment)
- Java (Core language)
- Swing (GUI framework)
- JUnit 5 (Testing)
- Git & GitHub (Version control)


## How to Run the program
Using IntelliJ IDEA
1. Open the project in IntelliJ
2. Navigate to: **src/campusbooking/Launcher.java**
3. Run the Main class
4. The GUI will launch
5. Navigate through the tabs (User, Event, Booking, Waitlist) to interact with the system, including:
    - adding users
    - creating events
    - making bookings
    - viewing waitlists


## How to Run Tests
1. Navigate to the test directory
2. Right-click **BookingServiceTest.java**
3. Select: **Run BookingServiceTest**
4. Review results in the test runner


## Project Structure
    src/
      campusbooking/
        Launcher.java
        MainFrame.java
        gui/
          BookingPanel.java
          EventPanel.java
          UserPanel.java
          WaitlistPanel.java

      DataSet/
        users.csv
        events.csv
        bookings.csv

      model/
        Booking.java
        BookingStatus.java
        Concert.java
        Event.java
        EventStatus.java
        Guest.java
        Seminar.java
        Staff.java
        Student.java
        User.java
        Workshop.java

      service/
        BookingService.java
        EventService.java
        UserService.java

      test/
        BookingServiceTest.java


## GitHub
Repository link: **https://github.com/alvinaanwarr/1420---final-project**


## Limitations
- Some GUI components rely on backend service integration and may contain placeholder functionality
- Collaboration through GitHub was not fully distributed due to initial setup challenges
- Limited persistence beyond initial CSV dataset loading


## Future Improvements
- Improve GUI usability and layout design
- Expand JUnit test coverage for all possible booking workflows
- Enhance validation and error handling across modules


## Collaboration Notes
During development, the team initially encountered difficulties with GitHub setup and repository synchronization.
As a result, a significant portion of development and integration work was completed collaboratively on shared
devices or by exchanging code directly between team members.

While GitHub was used for final integration and submission, many contributions were developed collectively before
being uploaded by a single team member. This approach allowed the team to maintain steady progress despite early
technical challenges with version control.

All team members contributed to the design, implementation, and testing of the system.


## Summary
This project successfully demonstrates a complete event booking system with a well-structured object-oriented design,
real-world booking and waitlist logic, a functional graphical interface, and automated testing for system validation.
