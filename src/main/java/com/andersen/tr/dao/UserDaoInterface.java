package com.andersen.tr.dao;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserDaoInterface {

    public void saveUser(String name, LocalDate creationDate) throws DaoException;

    public User fetchUserById(int id) throws DaoException;

    public void deleteUserAndTicketsById(int id) throws DaoException;

    public boolean checkUserExist(int userId) throws DaoException;

    public void translateName(String name, int userId) throws DaoException;

    public void updateUserAndTickets(User user, List<Ticket> tickets) throws DaoException;
}
