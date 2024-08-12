package com.andersen.tr;

import com.andersen.tr.bean.User;
import com.andersen.tr.service.impl.TicketService;
import com.andersen.tr.service.impl.UserService;

public class Main {
    private static final TicketService ticketService = new TicketService();
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        Main main = new Main();

        System.out.println("Hello! You in Postgre SQL Demo App");
        main.saveUser();
        User user = userService.getUser();

        main.saveTickets(user.getId(), user.getName());
        main.printUserTicketsInfo(user.getId());
        main.updateTicketType(user.getId());
        main.printTicketInfo();
        main.translateName(user.getId(), user.getName());
        main.deleteAllUserTickets(user.getId());
    }

    private void saveUser(){
            userService.saveUser();
    }

    private void saveTickets(int userId, String userName){
        for(int i = 0; i < 3; i++) {
            ticketService.createTicket(userId, userName);
        }
    }

    private void updateTicketType(int userId){
        ticketService.updateTicketType(userId);
    }

    private void printTicketInfo(){
        ticketService.printTicketInfo();
    }

    private void printUserTicketsInfo(int userId){
        ticketService.getTicketsByUserId(userId);
    }

    private void deleteAllUserTickets(int userId){
        userService.deleteUserAndTicketsById(userId);
    }

    private void translateName(int userId, String userName){
        userService.translate(userId, userName);
    }
}
