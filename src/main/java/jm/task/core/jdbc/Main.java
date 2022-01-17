package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();
//        userService.saveUser("Иван", "Иванов", (byte) 18);
//        userService.saveUser("Петр", "Петров", (byte) 20);
//        userService.saveUser("Дмитрий", "Дмитриев", (byte) 22);
//        userService.saveUser("Федор", "Федоров", (byte) 24);
//        userService.getAllUsers();
//        userService.cleanUsersTable();
//        userService.dropUsersTable();

    }
}
