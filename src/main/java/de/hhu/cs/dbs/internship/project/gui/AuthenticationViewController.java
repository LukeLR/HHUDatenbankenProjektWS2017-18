package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Connection;
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
    	logger.info("User tries to login: " + data.toString());
    	
    	PreparedStatement loginQuery = Project.getInstance().getConnection().prepareStatement(
    			"SELECT E_Mail_Adresse, Passwort FROM Kunde WHERE E_Mail_Adresse = ?");
    	loginQuery.setString(1, data.get("email").toString());
        ResultSet loginResults = loginQuery.executeQuery();
        while (loginResults.next()) {
            logger.info("Found user with E-Mail: " + loginResults.getString("E_Mail_Adresse") +
            		" and password: " + loginResults.getString("Passwort") + ", entered password was: "
            		+ data.get("password").toString());
            if (loginResults.getString("Passwort").equals(data.get("password").toString())) {
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
    	logger.info("User tries to register: " + data.toString());
    	
    	if (!data.get("password1").equals(data.get("password2")))
    		throw new SQLException("Passwords do not match!");
    	
    	Connection con = Project.getInstance().getConnection();
    	con.getRawConnection().setAutoCommit(false);
    	
    	PreparedStatement addressInsertQuery = con.prepareStatement(
    			"INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID) "
    			+ "VALUES (?, ?, ?, ?, NULL)");
    	addressInsertQuery.setString(1, data.get("street").toString());
    	addressInsertQuery.setString(2, data.get("houseNumber").toString());
    	addressInsertQuery.setString(3, data.get("zipCode").toString());
    	addressInsertQuery.setString(4, data.get("city").toString());
    	
    	PreparedStatement customerInsertQuery = con.prepareStatement(
    			"INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID) "
    			+ "VALUES (?, ?, ?, ?, NULL)");
    	customerInsertQuery.setString(1, data.get("eMail").toString());
    	customerInsertQuery.setString(2, data.get("firstName").toString());
    	customerInsertQuery.setString(3, data.get("lastName").toString());
    	customerInsertQuery.setString(4, data.get("password1").toString());
    	
    	try {
    		addressInsertQuery.executeUpdate();
    		customerInsertQuery.executeUpdate();
    		con.getRawConnection().commit();
    		con.getRawConnection().setAutoCommit(true);
    	} catch (SQLException ex) {
    		con.getRawConnection().rollback();
    		con.getRawConnection().setAutoCommit(true);
    		throw ex;
    	}
    }
}
