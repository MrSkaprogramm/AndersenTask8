package com.andersen.tr.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "\"Ticket\"", schema = "public")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "ticket_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TicketType ticketType;
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
    @Column(name = "user_name", nullable = false)
    private String userName;

    public Ticket() {}

    public Ticket(int userId, TicketType ticketType, LocalDate creationDate, String userName) {
        this.userId = userId;
        this.ticketType = ticketType;
        this.creationDate = creationDate;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
