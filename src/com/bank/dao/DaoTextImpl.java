package com.bank.dao;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.bank.pojos.User;

@SuppressWarnings({ "unused", "resource" })
public class DaoTextImpl implements DAO {

    // access data through here and not through service
    String filename = "src/com/data/files/userdata.txt";

    @Override
    public void addUser() {
        Scanner scan = new Scanner(System.in);
        String fn, ln, em, pwd;
        Boolean status = true;
        while (status != false) {
            System.out.println("Enter your first name");
            fn = scan.nextLine().trim();
            System.out.println("Enter your last name");
            ln = scan.nextLine().trim();
            System.out.println("Enter your email");
            em = scan.nextLine().trim();
            System.out.println("Enter desired password");
            String tempPassword = scan.nextLine().trim();
            System.out.println("Confirm desired password");
            String tempConfirmPassword = scan.nextLine().trim();
            if (tempPassword.equals(tempConfirmPassword)) {
                // Need to check if such user doesn't exist also
                // because if that person does, we need to reject them from
                // registering, but if not, we will register them with a unique id
                pwd = tempPassword;
                if (this.userExist(fn, ln, em) != true) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));) {

                        MessageDigest md = MessageDigest.getInstance("MD5");
                        String hash = fn + ln + em + pwd;
                        md.update(hash.getBytes());
                        byte byteData[] = md.digest();

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < byteData.length; i++) {
                            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                        }

                        String text = "";
                        text = text + sb.toString() + ":";
                        text = text + fn + ":";
                        text = text + ln + ":";
                        text = text + em + ":";
                        text = text + pwd + ":";
                        text = text + "0.0\n";
                        bw.write(text);

                        System.out.println("Successfully Registered\nWould you like to ...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                status = false;
            } else {
                System.out.println("PASSWORDS DON'T MATCH - TRY AGAIN");

            }
        }
    }

    @Override
    public Boolean userExist(String fn, String ln, String em) {
        Boolean exists = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(":");
                if (userData[1].equals(fn) && userData[2].equals(ln) && userData[3].equals(em)) {
                    System.out.println("Such user exists in our database,\nplease try to login instead");
                    exists = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public Boolean accountExist(String em, String pwd) {
        Boolean exists = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(":");
                if (userData[3].equals(em) && userData[4].equals(pwd)) {
                    exists = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    // TODO
    @Override
    public void addFunds(User currentUser) {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> userList = new ArrayList<User>();
        Boolean valid = false;

        while (valid != true) {
            double deposit;
            System.out.println("Enter The Amount You Would Like To Deposit:");
            deposit = scan.nextDouble();
            if (deposit < 0) {
                System.out.println("You Cannot Deposit Negative Dollars");
                valid = false;
            } else {
                currentUser.setBalance(currentUser.getBalance() + deposit);
                valid = true;
            }
        }
        System.out.println("\nCurrent Balance: $" + currentUser.getBalance() + "\n");

        try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
            String line = null;
            while ((line = br.readLine()) != null) {

                String[] states = line.split(":");
                User temp = new User(states[0], states[1], states[2], states[3], states[4],
                        Double.parseDouble(states[5]));
                userList.add(temp);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // wiping file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO saving the new data back into the text file
        for (User u : userList) {
            Double instanceDeposit = 0.0;

            if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                instanceDeposit = currentUser.getBalance();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                String text = "";
                text = text + u.getId() + ":";
                text = text + u.getFirstName() + ":";
                text = text + u.getLastName() + ":";
                text = text + u.getEmail() + ":";
                text = text + u.getPassword() + ":";
                if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                    text = text + instanceDeposit + "\n";
                } else {
                    text = text + u.getBalance() + "\n";
                }
                bw.write(text);
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void withdrawFunds(User currentUser) {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> userList = new ArrayList<User>();
        Boolean valid = false;

        while (valid != true) {
            double deposit;
            System.out.println("Enter The Amount You Would Like To Withdraw:");
            deposit = scan.nextDouble();
            if (deposit < 0 || deposit > currentUser.getBalance()) {
                System.out.println("You Cannot Withdraw Negative Dollars Or More Than What You Have In Your Account");
                valid = false;
            } else {
                currentUser.setBalance(currentUser.getBalance() - deposit);
                valid = true;
            }
        }
        System.out.println("\nCurrent Balance: $" + currentUser.getBalance() + "\n");

        try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
            String line = null;
            while ((line = br.readLine()) != null) {

                String[] states = line.split(":");
                User temp = new User(states[0], states[1], states[2], states[3], states[4],
                        Double.parseDouble(states[5]));
                userList.add(temp);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // wiping file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO saving the new data back into the text file
        for (User u : userList) {
            Double instanceDeposit = 0.0;

            if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                instanceDeposit = currentUser.getBalance();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                String text = "";
                text = text + u.getId() + ":";
                text = text + u.getFirstName() + ":";
                text = text + u.getLastName() + ":";
                text = text + u.getEmail() + ":";
                text = text + u.getPassword() + ":";
                if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                    text = text + instanceDeposit + "\n";
                } else {
                    text = text + u.getBalance() + "\n";
                }
                bw.write(text);
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void displayFunds(User currentUser) {

        System.out.println("\nCurrent Balance Consist Of:\n" + "$" + currentUser.getBalance() + "\n");

    }

    @Override
    // TODO Must complete after successful login
    public void editUser(User currentUser) {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> userList = new ArrayList<User>();
        String newEmail = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while ((line = br.readLine()) != null) {

                String[] states = line.split(":");
                User temp = new User(states[0], states[1], states[2], states[3], states[4],
                        Double.parseDouble(states[5]));
                userList.add(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("What Would You Like Your New Email To Be?");
        newEmail = scan.nextLine().trim();

        // wiping file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO saving the new data back into the text file
        for (User u : userList) {
            Double instanceDeposit = 0.0;

            if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                instanceDeposit = currentUser.getBalance();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                String text = "";
                text = text + u.getId() + ":";
                text = text + u.getFirstName() + ":";
                text = text + u.getLastName() + ":";
                if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                    text = text + newEmail + ":";
                } else {
                    text = text + u.getEmail() + ":";
                }

                text = text + u.getPassword() + ":";
                text = text + u.getBalance() + "\n";

                bw.write(text);
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteUser(User currentUser) {
        // TODO Auto-generated method stub
        Scanner scan = new Scanner(System.in);
        ArrayList<User> userList = new ArrayList<User>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while ((line = br.readLine()) != null) {

                String[] states = line.split(":");
                User temp = new User(states[0], states[1], states[2], states[3], states[4],
                        Double.parseDouble(states[5]));
                userList.add(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // wiping file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO saving the new data back into the text file
        for (User u : userList) {
            Double instanceDeposit = 0.0;

            if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {
                instanceDeposit = currentUser.getBalance();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                String text = "";
                if (u.getEmail().equals(currentUser.getEmail()) && u.getPassword().equals(currentUser.getPassword())) {

                } else {
                    text = text + u.getId() + ":";
                    text = text + u.getFirstName() + ":";
                    text = text + u.getLastName() + ":";
                    text = text + u.getEmail() + ":";
                    text = text + u.getPassword() + ":";
                    text = text + u.getBalance() + "\n";
                    bw.write(text);
                }

                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}