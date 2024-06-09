package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Name1", "Lastname1", (byte)10);
        userService.saveUser("Name2", "Lastname2", (byte)20);
        userService.saveUser("Name3", "Lastname3", (byte)30);
        userService.saveUser("Name4", "Lastname4", (byte)40);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
