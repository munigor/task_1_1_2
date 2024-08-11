package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.SqlUtil;
import jm.task.core.jdbc.util.Util;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final String TABLE_NAME = SqlUtil.getTableName(User.class);

    public UserServiceImpl() {
        userDao = Util.getUserDao();
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUsersTable() {
        userDao.createUsersTable();
        System.out.printf("Table `%s` was created\n", TABLE_NAME);
    }

    public void dropUsersTable() {
        userDao.dropUsersTable();
        System.out.printf("Table `%s` was dropped\n", TABLE_NAME);
    }

    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
        System.out.printf("Saved user: %s\n", name);
    }

    public void removeUserById(long id) {
        userDao.removeUserById(id);
        System.out.printf("Removed user with id: %d\n", id);
    }

    public List<User> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        users.forEach(System.out::println);
        return users;
    }

    public void cleanUsersTable() {
        userDao.cleanUsersTable();
        System.out.printf("Table `%s` was cleaned\n", TABLE_NAME);
    }
}
