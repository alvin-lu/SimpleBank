package com.bank.dao;

import com.bank.pojos.User;

public interface DAO {

	/*
	 * The contract we make for the information that we will need to send to and
	 * receive from the data source
	 *
	 * addUser editUser - change fn, ln, email, pw, or bl getUser
	 */

    void addUser();

    void editUser(User currentUser);

    void deleteUser(User currentUser);

    void addFunds(User currentUser);

    void withdrawFunds(User currentUser);

    void displayFunds(User currentUser);

    Boolean userExist(String fn, String ln, String em);

    Boolean accountExist(String em, String pwd);

}