package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(64), lastName VARCHAR(64), age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS User";
    private static final String SAVE_USER = "INSERT INTO  User (name, lastName, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER = "DELETE FROM User WHERE id = ?";
    private static final String GET_USERS = "SELECT * FROM User";
    private static final String CLEAN_TABLE = "TRUNCATE TABLE User";

    private static final Connection connection = new Util().getConnectionJDBC();
    private static PreparedStatement preparedStatement = null;

    public void createUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(CREATE_TABLE);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println("Table was not created!\n" + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("There was an error making a rollback\n" + ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("There was an error making a connection close\n" + ex);
            }
        }
    }

    public void dropUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(DROP_TABLE);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println("Table was not dropped!\n" + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("There was an error making a rollback\n" + ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("There was an error making a connection close\n" + ex);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            preparedStatement = connection.prepareStatement(SAVE_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.execute();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            System.out.println("User was not added!\n" + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("There was an error making a rollback\n" + ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("There was an error making a connection close\n" + ex);
            }
        }
    }

    public void removeUserById(long id) {
        try {
            preparedStatement = connection.prepareStatement(REMOVE_USER);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("User was not removed!\n" + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("There was an error making a rollback\n" + ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("There was an error making a connection close\n" + ex);
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("name"), rs.getString("lastName"), rs.getByte("age"));
                user.setId(rs.getLong("id"));
                list.add(user);
            }
        } catch (Exception e) {
            System.out.println("Users were not get!\n" + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("There was an error making a rollback\n" + ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("There was an error making a connection close\n" + ex);
            }
        }
//        System.out.println(list);
        return list;
    }

    public void cleanUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(CLEAN_TABLE);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println("Table was not cleaned!\n" + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("There was an error making a rollback\n" + ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("There was an error making a connection close\n" + ex);
            }
        }
    }
}
