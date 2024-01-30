package jdbc.util;

import jdbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionUtil {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/User";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static final String SHOW_SQL = "true";
    private static final String HBM2DDL_AUTO = "none";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            log.info("Соединение с базой данных успешно установлено (JDBC)!");
        } catch (SQLException ex) {
            log.error("Произошла ошибака при попытке создать соединение с базой данных (JDBC)!\n", ex);
        }
    }

    public static Connection getConnectionJDBC() {
        return connection;
    }

    private static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration()
                .setProperty(Environment.DRIVER, DRIVER)
                .setProperty(Environment.URL, URL)
                .setProperty(Environment.USER, USERNAME)
                .setProperty(Environment.PASS, PASSWORD)
                .setProperty(Environment.DIALECT, DIALECT)
                .setProperty(Environment.SHOW_SQL, SHOW_SQL)
                .setProperty(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder() //получение реестра и сервисов
                .applySettings(configuration.getProperties())     //настройка подключения
                .build();
        try {
            sessionFactory = configuration
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory(registry);
            log.info("Соединение с базой данных успешно установлено (SF)!");
        } catch (Exception ex) {
            log.error("Произошла ошибака при попытке создать соединение с базой данных (SF)!\n", ex);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeConnection() {
        try {
            connection.close();
            log.info("Соединение закрыто (JDBC)? " + connection.isClosed());
            sessionFactory.close();
            log.info("Соединение закрыто (SF)? " + connection.isClosed());
        } catch (SQLException ex) {
            log.error("Произошла ошибака при попытке закрыть соединение с базой данных!\n", ex);
        }
    }

    public static void getConnectionStatus() {
        try {
            log.info("Соединение закрыто? " + connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}