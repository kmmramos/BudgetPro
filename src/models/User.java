package models;

public class User {
    private int id; // Assuming ID is an integer
    private String firstName;
    private String lastName;
    private String email;

    // Updated constructor to include ID
    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    // Optionally, if you need to set or change the ID later
    public void setId(int id) {
        this.id = id;
    }
}
