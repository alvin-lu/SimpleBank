package com.bank.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.bank.dao.*;
import com.bank.pojos.User;

public class Service {

    // login or register

    DAO dao = new DaoTextImpl();
    // access data through here and not through service
    static String filename = "src/com/data/files/userdata.txt";

    @SuppressWarnings({ "unused", "resource" })
    public void login() {
        Scanner scan = new Scanner(System.in);
        User currentUser;
        String em, pwd;
        Boolean login = false;

        System.out.println("Enter your email associated with your account");
        em = scan.nextLine().trim();
        System.out.println("Enter your password");
        pwd = scan.nextLine().trim();

        if (dao.accountExist(em, pwd)) {
            currentUser = new User(em, pwd);
            login = true;
            try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    String[] userData = line.split(":");
                    if (userData[3].equals(em) && userData[4].equals(pwd)) {
                        currentUser.setId(userData[0]); // id
                        currentUser.setFirstName(userData[1]); // first name
                        currentUser.setLastName(userData[2]); // last name
                        currentUser.setBalance(Double.parseDouble(userData[5])); // setting balance

                        // TODO show login menu
                        // TODO edit
                        // TODO delete
                        // deposit
                        // withdraw
                        // display current funds
                        Boolean loggedMenu = true;
                        while (loggedMenu == true) {
                            short userChoice;
                            System.out.println(
                                    "1 - Deposit\n2 - Withdraw\n3 - Current Balance\n4 - Edit Account\n5 - Delete Account\n0 - Logout");
                            userChoice = scan.nextShort();
                            if (userChoice == 1) {
                                dao.addFunds(currentUser);
                            } else if (userChoice == 2) {
                                dao.withdrawFunds(currentUser);
                            } else if (userChoice == 3) {
                                dao.displayFunds(currentUser);
                            } else if (userChoice == 4) {
                                dao.editUser(currentUser);
                            } else if (userChoice == 5) {
                                dao.deleteUser(currentUser);
                                loggedMenu = false;
                            } else if (userChoice == 0) {
                                System.out.println("Successfully Logged Out\nWould you like to ...");
                                loggedMenu = false;
                            } else {
                                System.out.println("Invalid Input");
                            }

                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // if either username and/or password is wrong, notify the user
        if (!dao.accountExist(em, pwd)) {
            System.out.println("Account Does Not Exists");
        }
    }

    public void register() {
        dao.addUser();
    }

    @SuppressWarnings("resource")
    public void userDecision() {
        Scanner scan = new Scanner(System.in);
        short userChoice;
        Boolean status = true;

        while (status == true) {
            System.out.println("\n1 - Login\n2 - Register\n0 - Exit");
            userChoice = scan.nextShort();

            if (userChoice == 1) {
                // login
                this.login();
                status = true;
            } else if (userChoice == 2) {
                // register
                this.register();
                status = true;
            } else if (userChoice == 0) {
                System.out.println("fin");
                System.exit(0);
            } else {
                System.out.println("Invalid Input");
            }

        }
    }

}