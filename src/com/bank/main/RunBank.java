package com.bank.main;

import com.bank.service.Service;

public class RunBank {

    // implementation to run the application
    public static void main(String[] args) {
        Service bankService = new Service();
        systemStart(bankService);

    }

    static void systemStart(Service bankService) {
		/*
		 * Requirements: As a user I can, - login - create an account - logout -
		 * withdraw - deposit - view my balance - edit my information
		 *
		 * Specifications - core java, file i/o
		 */
        System.out.println(" ____  __  _  _  ____  __    ____    ____   __   __ _  __ _ ");
        System.out.println("/ ___)(  )( \\/ )(  _ \\(  )  (  __)  (  _ \\ / _\\ (  ( \\(  / )");
        System.out.println("\\___ \\ )( / \\/ \\ ) __// (_/\\ ) _)    ) _ (/    \\/    / )  ( ");
        System.out.println("(____/(__)\\_)(_/(__)  \\____/(____)  (____/\\_/\\_/\\_)__)(__\\_)");
        System.out.println("                Welcome to Simple Banking");
        bankService.userDecision();
    }

}