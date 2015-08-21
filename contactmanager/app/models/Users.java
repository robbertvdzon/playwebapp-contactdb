package models;

import java.util.List;

public class Users {
    private List<User> users = null;

    public Users() {
    }

    public Users(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
