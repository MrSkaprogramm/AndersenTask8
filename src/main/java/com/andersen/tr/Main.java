package com.andersen.tr;

import com.andersen.tr.config.SpringConfig;
import com.andersen.tr.model.User;
import com.andersen.tr.service.impl.TicketService;
import com.andersen.tr.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public Main(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Main main = context.getBean(Main.class);
        main.run();
    }

    private void run(){
        System.out.println("Hello! You in Postgre SQL Demo App");
        saveUser();
        User user = userService.getUser();

        saveTickets(user.getId(), user.getName());
        printUserTicketsInfo(user.getId());
        updateTicketType();
        printTicketInfo();
        translateName(user.getId(), user.getName());
        deleteAllUserTickets(user.getId());
        updateUserName(user.getId());
    }

    private void saveUser(){
            userService.saveUser();
    }

    private void saveTickets(int userId, String userName){
        for(int i = 0; i < 3; i++) {
            ticketService.createTicket(userId, userName);
        }
    }

    private void updateTicketType(){
        ticketService.updateTicketType();
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

    private void updateUserName(int userId){
        userService.updateUserAndTickets(userId);
    }
}
