package com.andersen.tr.service.impl;

import com.andersen.tr.model.TicketType;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.impl.TicketDao;
import com.andersen.tr.model.Ticket;
import com.andersen.tr.dao.impl.UserDao;
import com.andersen.tr.service.TicketServiceInterface;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Service
public class TicketService implements TicketServiceInterface {
    static Scanner scanner = new Scanner(System.in);

    private final UserDao userDao;
    private final TicketDao ticketDao;

    @Autowired
    public TicketService(UserDao userDao, TicketDao ticketDao) {
        this.userDao = userDao;
        this.ticketDao = ticketDao;
    }

    public Ticket createTicket(int userId, String userName) {
        System.out.println("Enter ticket type:");
        String typeString = scanner.nextLine();
        TicketType ticketType = TicketType.valueOf(typeString.toUpperCase());

        LocalDate creationDate = detectTicketTime();

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setTicketType(ticketType);
        ticket.setCreationDate(creationDate);
        ticket.setUserName(userName);


        try {
            ticketDao.saveTicket(ticket);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Ticket created");

        return ticket;
    }

    @Override
    public void updateTicketType() {
        System.out.println("Enter id of ticket:");
        int ticketId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter type of ticket:");
        String type = scanner.nextLine();
        TicketType ticketType = TicketType.valueOf(type.toUpperCase());

        try {
            Ticket ticket = ticketDao.fetchTicketById(ticketId);
            ticket.setTicketType(ticketType);

            if (!(ticketDao.checkTicketExist(ticketId))) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                ticketDao.updateTicketType(ticket);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void printTicketInfo() {
        System.out.println("Enter id of ticket:");
        int id = scanner.nextInt();
        scanner.nextLine();

        Ticket ticket = null;
        try {
            if (!ticketDao.checkTicketExist(id)) {
                throw new IllegalArgumentException("Wrong id!");
            } else {

                ticket = ticketDao.fetchTicketById(id);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Ticket with " + "ticket id " + ticket.getId() + "\n" + "- userId: "
                + ticket.getUserId() + "\n" + "- ticketType: " + ticket.getTicketType() + "\n" + "- creationDate: "
                + ticket.getCreationDate() + "\n" + "- userName: "
                + ticket.getUserName());
    }

    @Override
    public List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = null;

        try {
            if (!userDao.checkUserExist(userId)) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                tickets = ticketDao.fetchTicketsByUserId(userId);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Tickets of User with id number " + userId);
        for (Ticket ticket : tickets) {
            System.out.println("Ticket" + "\n" + "- id: " + ticket.getId() + "\n"
                    + "- type: " + ticket.getTicketType() + "\n" + "- creation date: "
                    + ticket.getCreationDate() + "\n" + "- user name: "
                    + ticket.getUserName());
        }
        return tickets;
    }

    public LocalDate detectTicketTime(){
        LocalDate creationDate = LocalDate.now();
        return creationDate;
    }
}