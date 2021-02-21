package models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final String address;
    private final List<String> users;

    public Room(String name, String address) {
        this.name = name;
        this.address = address;
        this.users = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public List<String> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
