package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private final static String CONFIG_FILE = "app.properties";
    private final static String DB_URL_KEY = "db.url";
    private final static String DB_USERNAME_KEY = "db.username";
    private final static String DB_PASSWORD_KEY = "db.password";
    private final static String DB_DRIVER_KEY = "db.driver";
    private final static Properties PROPERTIES = new Properties();

    static  {
        loadConfig();
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(getProperty(DB_DRIVER_KEY));
            connection = DriverManager.getConnection(
                getProperty(DB_URL_KEY),
                getProperty(DB_USERNAME_KEY),
                getProperty(DB_PASSWORD_KEY)
            );
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private static void loadConfig() {
        try (InputStream resource = Util.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            PROPERTIES.load(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
