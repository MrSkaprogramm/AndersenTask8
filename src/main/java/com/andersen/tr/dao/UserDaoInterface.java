package com.andersen.tr.dao;

import com.andersen.tr.bean.User;

import java.time.LocalDate;

public interface UserDaoInterface {

    public void saveUser(String name, LocalDate creationDate) throws DaoException;

    public User fetchUserById(int id) throws DaoException;

    public void deleteUserAndTicketsById(int id) throws DaoException;

    public boolean checkUserExist(int userId) throws DaoException;

    public void translateName(String name, int userId) throws DaoException;

}
