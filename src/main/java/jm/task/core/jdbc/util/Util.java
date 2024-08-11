package jm.task.core.jdbc.util;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private final static String CONFIG_FILE = "app.properties";
    private final static String DB_URL_KEY = "db.url";
    private final static String DB_USERNAME_KEY = "db.username";
    private final static String DB_PASSWORD_KEY = "db.password";
    private final static String DB_DRIVER_KEY = "db.driver";
    private final static String DB_MODE_KEY = "db.mode";
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

    public static SessionFactory getSessionFactory() {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        try {
            Configuration configuration = new Configuration();
            Arrays.stream(HibernateMapping.getClasses()).forEach(configuration::addAnnotatedClass);
            configuration
                .setProperty("hibernate.connection.driver_class", getProperty(DB_DRIVER_KEY))
                .setProperty("hibernate.connection.url", getProperty(DB_URL_KEY))
                .setProperty("hibernate.connection.username", getProperty(DB_USERNAME_KEY))
                .setProperty("hibernate.connection.password", getProperty(DB_PASSWORD_KEY));
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDao getUserDao() {
        int mode = Integer.parseInt(Optional.ofNullable(getProperty(DB_MODE_KEY)).orElse("0"));
        if(mode == 2) {
            return new UserDaoHibernateImpl();
        }
        return new UserDaoJDBCImpl();
    }
}
