package jdbc.dao;

import jdbc.model.User;
import jdbc.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(64), lastName VARCHAR(64), age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS User";
    private static final String  ROLLBACK_LOG = "Выполнен откат\n";

    private static final SessionFactory sessionFactory = ConnectionUtil.getSessionFactory();
    private static Session session = null;

    @Override
    public void createUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица User создана успешно");
        } catch (Exception e) {
            log.error("Произошла ошибка при создании таблиц User:\n", e);
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(DROP_TABLE).executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица User удалена успешно");
        } catch (Exception e) {
            log.error("Произошла оибка при удалении таблицы User:\n", e);
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
                log.warn(ROLLBACK_LOG);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            log.info("Пользователь с именем {} успешно добавлен в базу данных", name);
        } catch (Exception e) {
            log.warn("Произошла ошибка при попытке добавить пользователя с именем {} в базу данных", name);
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
                log.warn(ROLLBACK_LOG);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
            log.info("Пользователь {} успешно удален из базы данных", user);
        } catch (Exception e) {
            log.warn("Произошла ошибка при попытке удалить пользователя с id {} из базы данных", id);
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
                log.warn(ROLLBACK_LOG);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT q FROM User q", User.class).getResultList();
            session.getTransaction().commit();
            log.info("Получены все пользователи из базы данных: \n{}", list);
        } catch (Exception e) {
            log.error("Произошла ошибка при попытке получить всех пользователей из базы данных: \n", e);
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
                log.warn(ROLLBACK_LOG);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица User очищена успешно");
        } catch (Exception e) {
            log.info("Произошла ошибка при попытке очистки таблицы User");
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
                log.warn(ROLLBACK_LOG);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}