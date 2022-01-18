package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/User";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root12345";

    /////////////////// JDBCUtil /////////////////////

    private static Connection connection;

    static {

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection to DataBase was successful(JDBL)!");
            System.out.println(connection.isClosed());
        } catch (SQLException ex) {
            System.out.println("JDBC driver is not found!\n" + ex);
        }
    }

    public static void test() {
        try {
            System.out.println("connection closed? " + connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnectionJDBC() {
        return connection;
    }


/////////////////// HibernateUtil /////////////////////

    private static SessionFactory sessionFactory;

    static {

        Configuration configuration = new Configuration()
                .setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver")
                .setProperty(Environment.URL, URL)
                .setProperty(Environment.USER, USERNAME)
                .setProperty(Environment.PASS, PASSWORD)
                .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect")
                .setProperty(Environment.SHOW_SQL, "true")
                .setProperty(Environment.HBM2DDL_AUTO, "none");

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder() //получение реестра и сервисов
                .applySettings(configuration.getProperties())     //настройка подключения
                .build();
        try {
            sessionFactory = configuration
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory(registry);
            System.out.println("Connection to DataBase was successful(SF)!");
            System.out.println(sessionFactory.isClosed());
        } catch (Exception ex) {
            System.out.println("The sessionFactory had trouble building\n" + ex);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeConnection() {
        try {
            connection.close();
            sessionFactory.close();
            System.out.println("Was JDBC connection closed? " + connection.isClosed());
            System.out.println("Was SessionFactory closed? " + sessionFactory.isClosed());
        } catch (SQLException ex) {
            System.out.println("Close connection exception!\n" + ex);
        }
    }

}