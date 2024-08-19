package com.andersen.tr.service;

import com.andersen.tr.model.User;
import com.andersen.tr.dao.DaoException;

import java.time.LocalDate;

public interface UserServiceInterface {

    public void saveUser() throws DaoException;

    public User getUser() throws DaoException;

    public void deleteUserAndTicketsById(int userId);

    public LocalDate detectUserTime();

    public void translate(int userId, String userName);

    public void updateUserAndTickets(int userId);
}
