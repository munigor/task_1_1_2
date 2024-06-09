package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final String TABLE_NAME = "user";
    private final String COLUMN_ID = "id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_LASTNAME = "last_name";
    private final String COLUMN_AGE = "age";

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String query = """
            CREATE TABLE IF NOT EXISTS `%s` (
                `%s` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                `%s` VARCHAR(40) NOT NULL,
                `%s` VARCHAR(40) NOT NULL,
                `%s` TINYINT NOT NULL
            );
            """.formatted(TABLE_NAME, COLUMN_ID, COLUMN_NAME, COLUMN_LASTNAME, COLUMN_AGE);
        execute(query);
        System.out.printf("Table `%s` was created\n", TABLE_NAME);
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS `%s`".formatted(TABLE_NAME);
        execute(query);
        System.out.printf("Table `%s` was dropped\n", TABLE_NAME);
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO `%s` (`%s`,`%s`,`%s`) VALUES (?, ?, ?);"
            .formatted(TABLE_NAME, COLUMN_NAME, COLUMN_LASTNAME, COLUMN_AGE);
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Saved user: %s\n", name);
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM `%s` WHERE `%s` = ?".formatted(TABLE_NAME, COLUMN_ID);
        try(Connection connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Removed user with id: %d\n", id);
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM `%s`".formatted(TABLE_NAME);
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(COLUMN_ID));
                user.setName(resultSet.getString(COLUMN_NAME));
                user.setLastName(resultSet.getString(COLUMN_LASTNAME));
                user.setAge(resultSet.getByte(COLUMN_AGE));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        users.forEach(System.out::println);
        return users;
    }

    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE `%s`".formatted(TABLE_NAME);
        execute(query);
        System.out.printf("Table `%s` was cleaned\n", TABLE_NAME);
    }

    private void execute(String query) {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeUpdate(String query) {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
