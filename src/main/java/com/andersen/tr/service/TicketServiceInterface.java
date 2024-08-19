package com.andersen.tr.service;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.dao.DaoException;

import java.time.LocalDate;
import java.util.List;

public interface TicketServiceInterface {

    public Ticket createTicket(int userId, String userName);

    public void updateTicketType();

    public void printTicketInfo() throws DaoException;

    public List<Ticket> getTicketsByUserId(int userId);

    public LocalDate detectTicketTime();

}
