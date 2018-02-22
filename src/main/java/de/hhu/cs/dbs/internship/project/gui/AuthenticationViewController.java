package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Data;

import de.hhu.cs.dbs.internship.project.Project;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class AuthenticationViewController extends com.alexanderthelen.applicationkit.gui.AuthenticationViewController {
    protected AuthenticationViewController(String name) {
        super(name);
    }

    public static AuthenticationViewController createWithName(String name) throws IOException {
        AuthenticationViewController viewController = new AuthenticationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @Override
    public void loginUser(Data data) throws SQLException {
    	boolean authenticated = false;
    	//TODO: Use finer log levels
    	//TODO: Password hashing, PLEASE!
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Login event");
    	logger.info("User tried to login: " + data.toString());
    	
    	PreparedStatement userAccountQuery = Project.getInstance().getConnection().prepareStatement("SELECT E_Mail_Adresse, Passwort FROM Kunde WHERE E_Mail_Adresse = ?");
    	userAccountQuery.setString(1, data.get("email").toString());
        ResultSet userAccountResults = userAccountQuery.executeQuery();
        while (userAccountResults.next()) {
        	String storedEmail = userAccountResults.getString("E_Mail_Adresse");
        	String storedPassword = userAccountResults.getString("Passwort");
        	String enteredPassword = data.get("password").toString();
            logger.info("Found user with E-Mail: " + storedEmail + " and password: " + storedPassword + ", entered password was: " + enteredPassword);
            if (storedPassword.equals(enteredPassword)) {
            	 logger.info("User credentials correct. Done searching for users.");
            	 authenticated = true;
            }
        }
        if (!authenticated) {
        	logger.warning("User credentials incorrect. Done searching for users.");
        	throw new SQLException("Username or password incorrect!");
        }
    }

    @Override
    public void registerUser(Data data) throws SQLException {
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Registration event");
        throw new SQLException(getClass().getName() + ".registerUser(Data) nicht implementiert.");
    }
}
