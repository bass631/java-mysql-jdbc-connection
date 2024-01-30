package jdbc.dao;

import jdbc.model.User;
import jdbc.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(64), lastName VARCHAR(64), age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS User";
    private static final String SAVE_USER = "INSERT INTO  User (name, lastName, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER = "DELETE FROM User WHERE id = ?";
    private static final String GET_USERS = "SELECT * FROM User";
    private static final String CLEAN_TABLE = "TRUNCATE TABLE User";
    private static final String  ROLLBACK_LOG = "Выполнен откат\n";
    private static final String  CLOSE_CONNECTION_ERROR_LOG = "Произошла ошибка при закрытии соединения\n";
    private static final Connection connection = ConnectionUtil.getConnectionJDBC();
    private static PreparedStatement preparedStatement = null;

    public void createUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(CREATE_TABLE);
            preparedStatement.execute();
            log.info("Таблица User создана успешно");
        } catch (Exception e) {
            log.error("Произошла ошибка при создании таблиц User:\n", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(ROLLBACK_LOG, ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                log.warn(CLOSE_CONNECTION_ERROR_LOG, ex);
            }
        }
    }

    public void dropUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(DROP_TABLE);
            preparedStatement.execute();
            log.info("Таблица User удалена успешно");
        } catch (Exception e) {
            log.error("Произошла оибка при удалении таблицы User:\n", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(ROLLBACK_LOG, ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                log.warn(CLOSE_CONNECTION_ERROR_LOG, ex);
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
            log.info("Пользователь с именем {} успешно добавлен в базу данных", name);
        } catch (Exception e) {
            log.warn("Произошла ошибка при попытке добавить пользователя с именем {} в базу данных", name);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(ROLLBACK_LOG, ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                log.warn(CLOSE_CONNECTION_ERROR_LOG, ex);
            }
        }
    }

    public void removeUserById(long id) {
        try {
            preparedStatement = connection.prepareStatement(REMOVE_USER);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            log.info("Пользователь с id = {} успешно удален из базы данных", id);
        } catch (Exception e) {
            log.warn("Произошла ошибка при попытке удалить пользователя с id {} из базы данных", id);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(ROLLBACK_LOG, ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                log.warn(CLOSE_CONNECTION_ERROR_LOG, ex);
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
            log.info("Получены все пользователи из базы данных: \n{}", list);
        } catch (Exception e) {
            log.error("Произошла ошибка при попытке получить всех пользователей из базы данных: \n", e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(ROLLBACK_LOG, ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                log.warn(CLOSE_CONNECTION_ERROR_LOG, ex);
            }
        }
        return list;
    }

    public void cleanUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(CLEAN_TABLE);
            preparedStatement.execute();
            log.info("Таблица User очищена успешно");
        } catch (Exception e) {
            log.info("Произошла ошибка при попытке очистки таблицы User");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.warn(ROLLBACK_LOG, ex);
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                log.warn(CLOSE_CONNECTION_ERROR_LOG, ex);
            }
        }
    }
}