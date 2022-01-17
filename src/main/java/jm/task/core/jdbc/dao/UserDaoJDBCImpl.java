package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(64), lastName VARCHAR(64), age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS User";
    private static final String SAVE_USER = "INSERT INTO  User (name, lastName, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER = "DELETE FROM User WHERE id = ?";
    private static final String GET_USERS = "SELECT * FROM User";
    private static final String CLEAN_TABLE = "TRUNCATE TABLE User";

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {

        try (PreparedStatement preparedStatement = new Util().getConnectionJDBC().prepareStatement(CREATE_TABLE);) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Table was not created!\n" + e);
        }
    }

    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = new Util().getConnectionJDBC().prepareStatement(DROP_TABLE);) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Table was not dropped!\n" + e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = new Util().getConnectionJDBC().prepareStatement(SAVE_USER);) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.execute();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.out.println("User was not added!\n" + e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = new Util().getConnectionJDBC().prepareStatement(REMOVE_USER);) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("User was not removed!\n" + e);
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = new Util().getConnectionJDBC().prepareStatement(GET_USERS);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("name"), rs.getString("lastName"), rs.getByte("age"));
                user.setId(rs.getLong("id"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Users were not get!\n" + e);
        }
//        System.out.println(list);
        return list;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = new Util().getConnectionJDBC().prepareStatement(CLEAN_TABLE);) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Table was not cleaned!\n" + e);
        }
    }
}
