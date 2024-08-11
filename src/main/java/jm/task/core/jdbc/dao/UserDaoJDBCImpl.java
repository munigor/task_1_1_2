package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.SqlUtil;
import jm.task.core.jdbc.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Class<User> className = User.class;

    static {
        System.out.println(UserDaoJDBCImpl.class.getName());
    }

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        execute(SqlUtil.createTableSql(className));
    }

    public void dropUsersTable() {
        execute(SqlUtil.dropTableSql(className));
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlUtil.insertSql(className))) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try(Connection connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlUtil.deleteByIdSql(className))) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(SqlUtil.selectAllSql(className));
            while (resultSet.next()) {
                User user = new User();
                Arrays.stream(className.getDeclaredFields())
                    .forEach(field -> fillUser(field, user, resultSet));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    private void fillUser(Field field, User user, ResultSet resultSet) {
        try {
            String methodName = "get" + field.getType().getSimpleName();
            Method method = ResultSet.class.getDeclaredMethod(methodName, String.class);
            field.setAccessible(true);
            field.set(user, method.invoke(resultSet, SqlUtil.getFieldColumnName(field)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        execute(SqlUtil.truncateTableSql(className));
    }

    private void execute(String query) {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()){
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
