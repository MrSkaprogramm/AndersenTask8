package com.andersen.tr.dao;

import com.andersen.tr.bean.Ticket;
import com.andersen.tr.bean.TicketType;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketDaoInterface {

    public void saveTicket(Ticket ticket) throws DaoException;

    public void updateTicketType(int ticketId, int userId, TicketType ticketType) throws DaoException;

    public Ticket fetchTicketById(int id) throws DaoException;

    public List<Ticket> fetchTicketsByUserId(int userId) throws DaoException;


}
