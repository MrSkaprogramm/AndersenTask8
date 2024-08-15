package com.andersen.tr.dao.impl;
import com.andersen.tr.dao.connection.SessionFactoryProvider;
import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.UserDaoInterface;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.LocalDate;
import java.util.List;

public class UserDao implements UserDaoInterface {
    private static final String DELETE_TICKETS_BY_USER_ID_QUERY = "DELETE FROM Ticket WHERE userId = :userId";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM User WHERE id = :userId";
    private static final String CHECK_USER_EXIST_QUERY = "SELECT COUNT(*) FROM \"User\" WHERE id = :userId";
    private static final String USER_USERNAME_TRANSLATION_QUERY = "UPDATE \"User\" SET name = TRANSLATE(?, 'abcdefghijklmnopqrstuvwxyz', 'абвгдеёжзиклмнопрстуфхцчшщъыьэюя') " +
            "WHERE id = ?;";
    private static final String TICKET_USERNAME_TRANSLATION_QUERY = "UPDATE \"Ticket\" " +
            "SET user_name = TRANSLATE(?, 'abcdefghijklmnopqrstuvwxyz', 'абвгдеёжзиклмнопрстуфхцчшщъыьэюя') " +
            "WHERE user_id = ?;";


    @Override
    public void saveUser(String name, LocalDate creationDate) throws DaoException {
        User user = new User(name, creationDate.atStartOfDay());
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public User fetchUserById(int id) throws DaoException {
        return SessionFactoryProvider
                .getSessionFactory()
                .openSession()
                .get(User.class, id);
    }

    @Override
    public void deleteUserAndTicketsById(int userId) throws DaoException {
        Transaction transaction = null;
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query ticketDeleteQuery = session.createQuery(DELETE_TICKETS_BY_USER_ID_QUERY);
            ticketDeleteQuery.setParameter("userId", userId);
            ticketDeleteQuery.executeUpdate();

            Query userDeleteQuery = session.createQuery(DELETE_USER_BY_ID_QUERY);
            userDeleteQuery.setParameter("userId", userId);
            userDeleteQuery.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void translateName(String name, int userId) throws DaoException {
        Transaction transaction = null;
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query userNameQuery = session.createNativeQuery(USER_USERNAME_TRANSLATION_QUERY);
            userNameQuery.setParameter(1, name.toLowerCase());
            userNameQuery.setParameter(2, userId);
            userNameQuery.executeUpdate();

            Query ticketNameQuery = session.createNativeQuery(TICKET_USERNAME_TRANSLATION_QUERY);
            ticketNameQuery.setParameter(1, name.toLowerCase());
            ticketNameQuery.setParameter(2, userId);
            ticketNameQuery.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateUserAndTickets(User user, List<Ticket> tickets) throws DaoException {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.update(user);

            for (Ticket ticket : tickets) {
                session.update(ticket);
            }
            transaction.commit();

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public boolean checkUserExist(int userId) throws DaoException {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            long count = (long) session.createNativeQuery(CHECK_USER_EXIST_QUERY)
                    .setParameter("userId", userId)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }
}
