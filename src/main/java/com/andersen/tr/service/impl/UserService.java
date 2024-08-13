package com.andersen.tr.service.impl;

import com.andersen.tr.bean.User;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.impl.UserDao;
import com.andersen.tr.service.UserServiceInterface;

import java.time.LocalDate;
import java.util.Scanner;

public class UserService implements UserServiceInterface {
    private final UserDao userDao = new UserDao();
    static Scanner scanner = new Scanner(System.in);

    @Override
    public void saveUser() {
        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        LocalDate creationDate = detectUserTime();
        try {
            userDao.saveUser(name, creationDate);
            System.out.println("User created");
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public User getUser() {
        System.out.println("Enter user id:");
        int userId = scanner.nextInt();
        User user = null;
        try {
            if (!userDao.checkUserExist(userId)) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                user = userDao.fetchUserById(userId);
            }
            System.out.println("User:" + "\n" + "- userId: " + user.getId() + "\n" + "- name: "
                    + user.getName() + "\n" + "- creationDate: " + user.getCreationDateTime());
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }

    @Override
    public void deleteUserAndTicketsById(int userId) {
        try {
            if (!userDao.checkUserExist(userId)) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                userDao.deleteUserAndTicketsById(userId);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public LocalDate detectUserTime() {
        LocalDate creationDate = LocalDate.now();
        return creationDate;
    }

    @Override
    public void translate(int userId, String userName) {
        try {
            userDao.translateName(userName, userId);
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }
}
