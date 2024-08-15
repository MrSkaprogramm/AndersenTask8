package com.andersen.tr.dao.impl;
import com.andersen.tr.dao.connection.SessionFactoryProvider;
import com.andersen.tr.model.Ticket;
import com.andersen.tr.dao.TicketDaoInterface;
import com.andersen.tr.dao.DaoException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TicketDao implements TicketDaoInterface {
    private static final String FETCH_TICKETS_BY_USER_ID_QUERY = "FROM Ticket WHERE userId = :userId";
    private static final String CHECK_TICKET_EXIST_QUERY = "SELECT COUNT(*) FROM \"Ticket\" WHERE id = :ticketId";

    @Override
    public void saveTicket(Ticket ticket) throws DaoException {
        Transaction transaction = null;
        try(Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(ticket);
            transaction.commit();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void updateTicketType(Ticket ticket) throws DaoException {
        Transaction transaction = null;
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(ticket);
            transaction.commit();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Ticket fetchTicketById(int id) throws DaoException {
        return SessionFactoryProvider
                .getSessionFactory()
                .openSession()
                .get(Ticket.class, id);
    }

    @Override
    public List<Ticket> fetchTicketsByUserId(int userId) throws DaoException {
        try(Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            Query query = session.createQuery(FETCH_TICKETS_BY_USER_ID_QUERY, Ticket.class);
            query.setParameter("userId", userId);

            List<Ticket> tickets = query.getResultList();
            return tickets;
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    public boolean checkTicketExist(int ticketId) throws DaoException {
        try (Session session = SessionFactoryProvider.getSessionFactory().openSession()) {
            long count = (long) session.createNativeQuery(CHECK_TICKET_EXIST_QUERY)
                    .setParameter("ticketId", ticketId)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }
}
