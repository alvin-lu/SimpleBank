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
        System.out.println("__________                     __                        ");
        System.out.println("\\______   \\ _______  _______ _/  |_ __ _________   ____  ");
        System.out.println(" |       _// __ \\  \\/ /\\__  \\\\   __\\  |  \\_  __ \\_/ __ \\ ");
        System.out.println(" |    |   \\  ___/\\   /  / __ \\|  | |  |  /|  | \\/\\  ___/ ");
        System.out.println(" |____|_  /\\___  >\\_/  (____  /__| |____/ |__|    \\___  >");
        System.out.println("        \\/     \\/           \\/                        \\/ ");
        System.out.println("                Welcome to Revature Banking");
        bankService.userDecision();
    }

}