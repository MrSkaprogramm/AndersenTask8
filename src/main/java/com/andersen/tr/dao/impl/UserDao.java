package com.andersen.tr.dao.impl;
import com.andersen.tr.bean.User;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.UserDaoInterface;
import com.andersen.tr.dao.connection.ConnectionPool;
import com.andersen.tr.dao.connection.ConnectionPoolException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UserDao implements UserDaoInterface {
    private static final ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
    private static final int USER_ID_COLUMN_INDEX = 1;
    private static final int USERNAME_COLUMN_INDEX = 2;
    private static final int USER_CREATIONDATE_COLUMN_INDEX = 3;
    private static final String SAVE_USER_QUERY = "INSERT INTO \"User\" (name, creation_date) VALUES (?, ?);";
    private static final String FETCH_USER_BY_ID = "SELECT * FROM \"User\" WHERE id =?";
    private static final String DELETE_TICKETS_BY_USER_ID = "DELETE FROM \"Ticket\" WHERE user_id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM \"User\" WHERE id = ?";
    private static final String CHECK_USER_EXIST_QUERY = "SELECT COUNT(*) FROM \"User\" WHERE id = ?";
    private static final String USER_USERNAME_TRANSLATION_QUERY = "UPDATE \"User\" SET name = TRANSLATE(?, 'abcdefghijklmnopqrstuvwxyz', 'абвгдеёжзиклмнопрстуфхцчшщъыьэюя') " +
            "WHERE id = ?;";
    private static final String TICKET_USERNAME_TRANSLATION_QUERY = "UPDATE \"Ticket\" " +
            "SET user_name = TRANSLATE(?, 'abcdefghijklmnopqrstuvwxyz', 'ZYXWVUTSRQPONMLKJIHGFEDCBA') " +
            "WHERE user_id = ?;";


    @Override
    public void saveUser(String name, LocalDate creationDate) throws DaoException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            System.out.println("root1");
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SAVE_USER_QUERY);
            preparedStatement.setString(1, name);

            Timestamp timestamp = Timestamp.from(creationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println("timestamp " + timestamp);

            preparedStatement.setTimestamp(2, timestamp);
            System.out.println("root2");

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
    public User fetchUserById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        System.out.println("root1");

        User user = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(FETCH_USER_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println("root2");

            resultSet = preparedStatement.executeQuery();

            System.out.println("root3");

            if (resultSet.next()) {
                int userId = resultSet.getInt(USER_ID_COLUMN_INDEX);
                String name = resultSet.getString(USERNAME_COLUMN_INDEX);
                Timestamp createdAt = resultSet.getTimestamp(USER_CREATIONDATE_COLUMN_INDEX);
                LocalDateTime creationDateTime = createdAt.toLocalDateTime();
                user = new User(userId, name, creationDateTime);
            }

            return user;
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
    public void deleteUserAndTicketsById(int userId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();

            preparedStatement = connection.prepareStatement(DELETE_TICKETS_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException | ConnectionPoolException e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connectionPool.closeConnection(connection, preparedStatement, null);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean checkUserExist(int userId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(CHECK_USER_EXIST_QUERY);
            preparedStatement.setInt(1, userId);
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

    @Override
    public void translateName(String name, int userId) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Savepoint savepoint = null;

        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(USER_USERNAME_TRANSLATION_QUERY);
            preparedStatement.setString(1, name.toLowerCase());
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();

            savepoint = connection.setSavepoint();

            preparedStatement = connection.prepareStatement(TICKET_USERNAME_TRANSLATION_QUERY);
            preparedStatement.setString(1, name.toLowerCase());
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException | ConnectionPoolException e) {
            if (connection != null) {
                try {
                    if (savepoint != null) {
                        connection.rollback(savepoint);
                    } else {
                        connection.rollback();
                    }
                } catch (SQLException ex) {
                    throw new DaoException(ex.getMessage());
                }
            }
            throw new DaoException(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connectionPool.closeConnection(connection, preparedStatement, null);
                } catch (SQLException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        }
    }
}
