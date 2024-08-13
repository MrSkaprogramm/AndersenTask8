package com.andersen.tr.dao.impl;
import com.andersen.tr.bean.Ticket;
import com.andersen.tr.bean.TicketType;
import com.andersen.tr.dao.TicketDaoInterface;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.connection.ConnectionPool;
import com.andersen.tr.dao.connection.ConnectionPoolException;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TicketDao implements TicketDaoInterface {
    private static final ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
    private static final int TICKET_ID_COLUMN_INDEX = 1;
    private static final int TICKET_USER_ID_COLUMN_INDEX = 2;
    private static final int TICKET_TYPE_COLUMN_INDEX = 3;
    private static final int TICKET_CREATIONTIME_COLUMN_INDEX = 4;
    private static final int TICKET_USERNAME_COLUMN_INDEX = 5;
    private static final String SAVE_TICKET_QUERY = "INSERT INTO \"Ticket\" (user_id, ticket_type, creation_date, user_name) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_TICKET_TYPE_QUERY = "UPDATE \"Ticket\" SET ticket_type =? WHERE id =?";
    private static final String FETCH_TICKETS_BY_ID = "SELECT * FROM \"Ticket\" WHERE id =?";
    private static final String FETCH_TICKETS_BY_USER_ID_QUERY = "SELECT * FROM \"Ticket\" WHERE user_id =?";
    private static final String CHECK_TICKET_EXIST_QUERY = "SELECT COUNT(*) FROM \"Ticket\" WHERE id = ?";

    @Override
    public void saveTicket(Ticket ticket) throws DaoException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SAVE_TICKET_QUERY);
            preparedStatement.setInt(1, ticket.getUserId());
            preparedStatement.setObject(2, ticket.getTicketType(), Types.OTHER);

            Timestamp timestamp = Timestamp.from(ticket.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setString(4, ticket.getUserName());


            preparedStatement.executeUpdate();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connectionPool.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
    }

    @Override
    public void updateTicketType(int ticketId, int userId, TicketType ticketType) throws DaoException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_TICKET_TYPE_QUERY);
            preparedStatement.setObject(1, ticketType, Types.OTHER);
            preparedStatement.setInt(2, ticketId);
            preparedStatement.executeUpdate();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connectionPool.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
    }

    @Override
    public Ticket fetchTicketById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ticket ticket = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(FETCH_TICKETS_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ticket = new Ticket();
                ticket.setId(resultSet.getInt(TICKET_ID_COLUMN_INDEX));
                ticket.setUserId(resultSet.getInt(TICKET_USER_ID_COLUMN_INDEX));
                String ticketTypeString = resultSet.getString(TICKET_TYPE_COLUMN_INDEX);
                ticket.setTicketType(TicketType.valueOf(ticketTypeString));
                Timestamp timestamp = resultSet.getTimestamp(TICKET_CREATIONTIME_COLUMN_INDEX);
                LocalDate creationDate = timestamp.toLocalDateTime().toLocalDate();
                ticket.setCreationDate(creationDate);
                ticket.setUserName(resultSet.getString(TICKET_USERNAME_COLUMN_INDEX));
            }

            return ticket;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connectionPool.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
    }

    @Override
    public List<Ticket> fetchTicketsByUserId(int userId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Ticket> tickets = new ArrayList<>();

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(FETCH_TICKETS_BY_USER_ID_QUERY);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(resultSet.getInt(TICKET_ID_COLUMN_INDEX));
                ticket.setUserId(resultSet.getInt(TICKET_USER_ID_COLUMN_INDEX));

                String ticketTypeString = resultSet.getString(TICKET_TYPE_COLUMN_INDEX);
                ticket.setTicketType(TicketType.valueOf(ticketTypeString));

                Timestamp timestamp = resultSet.getTimestamp(TICKET_CREATIONTIME_COLUMN_INDEX);
                LocalDate creationDate = timestamp.toLocalDateTime().toLocalDate();
                ticket.setCreationDate(creationDate);
                ticket.setUserName(resultSet.getString(TICKET_USERNAME_COLUMN_INDEX));
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connectionPool.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
    }

    public boolean checkTicketExist(int ticketId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(CHECK_TICKET_EXIST_QUERY);
            preparedStatement.setInt(1, ticketId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connectionPool.closeConnection(connection, preparedStatement, resultSet);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
        return false;
    }
}
