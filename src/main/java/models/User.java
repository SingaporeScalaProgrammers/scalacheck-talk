package models;

public class User {

    public User(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
