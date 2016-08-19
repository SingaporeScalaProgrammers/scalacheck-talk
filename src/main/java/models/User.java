package models;

public class User {



    public User(String name, String address) {
        if (name == null || address == null) {
            throw new IllegalArgumentException();
        }
    }


}
