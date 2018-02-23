package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
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
            	 
            	 // Saving user account information application-wide
            	 Project.getInstance().getData().put("email", data.get("email").toString());
            	 
            	 // Check if customer is shop assistant
            	 PreparedStatement shopAssistantQuery = Project.getInstance().getConnection().prepareStatement(
            			 "SELECT E_Mail_Adresse FROM Angestellter WHERE E_Mail_Adresse = ?");
            	 shopAssistantQuery.setString(1, data.get("email").toString());
            	 ResultSet shopAssistantResults = shopAssistantQuery.executeQuery();
            	 if (shopAssistantResults.getString("E_Mail_Adresse").equals(data.get("email").toString())) {
            		 Project.getInstance().getData().put("permission", Permission.SHOP_ASSISTANT);
            	 } else {
            		 // Check if customer is premium customer
            		 PreparedStatement premiumCustomerQuery = Project.getInstance().getConnection().prepareStatement(
            				 "SELECT E_Mail_Adresse FROM Premiumkunde WHERE E_Mail_Adresse = ?");
            		 premiumCustomerQuery.setString(1, data.get("email").toString());
            		 ResultSet premiumCustomerResults = premiumCustomerQuery.executeQuery();
            		 if (premiumCustomerResults.getString("E_Mail_Adresse").equals(data.get("email").toString())) {
            			 Project.getInstance().getData().put("permission", Permission.PREMIUM_CUSTOMER);
            		 } else {
            			 Project.getInstance().getData().put("permission", Permission.CUSTOMER);
            		 }
            	 }
            	 
            	 logger.info("User access level is: " + Permission.permissionLevelToString(Integer.valueOf(
            			 Project.getInstance().getData().get("permission").toString())) + ".");
            	 
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
    			+ "VALUES (?, ?, ?, ?, ?)");
    	customerInsertQuery.setString(1, data.get("eMail").toString());
    	customerInsertQuery.setString(2, data.get("firstName").toString());
    	customerInsertQuery.setString(3, data.get("lastName").toString());
    	customerInsertQuery.setString(4, data.get("password1").toString());
    	
    	try {
    		addressInsertQuery.executeUpdate();
    		
    		/* Getting the Adressen_ID of the just inserted address
    		 * and inserting it into the customer that is about to be
    		 * created.
    		 */
    		PreparedStatement addressRequestQuery = con.prepareStatement(
    				"SELECT Adressen_ID FROM Adresse "
    				+ "WHERE Strasse = ? AND Hausnummer = ? AND PLZ = ? AND Ort )= ?");
    		
    		addressRequestQuery.setString(1, data.get("street").toString());
    		addressRequestQuery.setString(2, data.get("houseNumber").toString());
    		addressRequestQuery.setString(3, data.get("zipCode").toString());
    		addressRequestQuery.setString(4, data.get("city").toString());
    		
    		ResultSet addressResults = addressRequestQuery.executeQuery();
    		int addressID = addressResults.getInt("Adressen_ID");
    		customerInsertQuery.setInt(5, addressID);
    		
    		customerInsertQuery.executeUpdate();
    		con.getRawConnection().commit();
    		con.getRawConnection().setAutoCommit(true);
    		logger.info("Registration successful! Addressen_ID was " + String.valueOf(addressID) + ".");
    	} catch (SQLException ex) {
    		con.getRawConnection().rollback();
    		con.getRawConnection().setAutoCommit(true);
    		logger.log(Level.SEVERE, "User failed to register!", ex);
    		throw ex;
    	}
    }
}
