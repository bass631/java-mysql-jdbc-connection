package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS User (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(64), lastName VARCHAR(64), age TINYINT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS User";

    private static final SessionFactory sessionFactory = Util.getSessionFactory();
    private static Session session = null;

    @Override
    public void createUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
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
        } catch (Exception e) {
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
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
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
    public void removeUserById(long id) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
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
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT q FROM User q", User.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                    || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                session.getTransaction().rollback();
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
        } catch (Exception e) {
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
}
