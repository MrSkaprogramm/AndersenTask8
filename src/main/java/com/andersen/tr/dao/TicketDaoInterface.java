package com.andersen.tr.dao;

import com.andersen.tr.model.Ticket;

import java.util.List;

public interface TicketDaoInterface {

    public void saveTicket(Ticket ticket) throws DaoException;

    public void updateTicketType(Ticket ticket) throws DaoException;

    public Ticket fetchTicketById(int id) throws DaoException;

    public List<Ticket> fetchTicketsByUserId(int userId) throws DaoException;

    public boolean checkTicketExist(int ticketId) throws DaoException;

}
