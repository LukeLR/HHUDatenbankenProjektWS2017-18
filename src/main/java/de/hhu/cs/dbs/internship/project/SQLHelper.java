package de.hhu.cs.dbs.internship.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
/**
 * Contains various helper functions for SQL statements.
 * @author lukas
 *
 */
public class SQLHelper {
	/**
	 * Searches the database for an address id by given address.
	 * @param street The street of the address in question.
	 * @param houseNumber The housenumber of the address in question.
	 * @param zipCode The ZIP code of the address in question.
	 * @param city The city of the address in question.
	 * @param con The connection to use for searching. This is useful, if the search should include results for changes that have not been committed yet.
	 * @return The address id for the address found.
	 * @throws SQLException If the statement is malformed, or no address is found.
	 */
	public static int getAddressIDByAddress
	(String street, String houseNumber, String zipCode, String city, Connection con) throws SQLException
	{
		Logger logger = Logger.getLogger(SQLHelper.class.getName());
		logger.info("Searching for address ID for address: " + street
				+ " " + houseNumber + ", " + zipCode + " " + city);
		
		PreparedStatement addressRequestQuery = con.prepareStatement(
				"SELECT Adressen_ID FROM Adresse "
				+ "WHERE Strasse = ? AND Hausnummer = ? AND PLZ = ? AND Ort = ?");
		
		addressRequestQuery.setString(1, street.toString());
		addressRequestQuery.setString(2, houseNumber.toString());
		addressRequestQuery.setString(3, zipCode.toString());
		addressRequestQuery.setString(4, city.toString());
		
		ResultSet addressResults = addressRequestQuery.executeQuery();
		int addressID = addressResults.getInt("Adressen_ID");
		logger.info("Address ID found: " + String.valueOf(addressID));
		return addressID;
	}
	/**
	 * Searches the database for an address id by given address. In this case, no database connection is given, so the default one is used.
	 * @param street The street of the address in question.
	 * @param houseNumber The housenumber of the address in question.
	 * @param zipCode The ZIP code of the address in question.
	 * @param city The city of the address in question.
	 * @return The address id for the address found.
	 * @throws SQLException If the statement is malformed, or no address is found.
	 */
	public static int getAddressIDByAddress
	(String street, String houseNumber, String zipCode, String city) throws SQLException
	{
		return getAddressIDByAddress(street, houseNumber, zipCode, city,
				Project.getInstance().getConnection());
	}
	
	/**
	 * Compares two addresses. If they have changed, insert the new one into the database. Gets the address ID of the current address, regardless if it has changed or not.
	 * @param streetOld The street of the old address.
	 * @param houseNumberOld The housenumber of the old address.
	 * @param zipCodeOld The zip code of the old address.
	 * @param cityOld The city of the old address.
	 * @param streetNew The street of the new address.
	 * @param houseNumberNew The housenumber of the new address.
	 * @param zipCodeNew The ZIP code of the new address.
	 * @param cityNew The city of the new address.
	 * @param con The connection to use for insertion or searching. This is useful if there are changes that have not been committed yet.
	 * @return The address ID of the new address, regardless if it has changed or not.
	 * @throws SQLException If the statement is malformed, or the address is unchanged but not in the database.
	 */
	public static int getAddressIDWithChangedAddress
	(String streetOld, String houseNumberOld, String zipCodeOld, String cityOld,
	String streetNew, String houseNumberNew, String zipCodeNew, String cityNew, Connection con) throws SQLException
	{
		Logger logger = Logger.getLogger(SQLHelper.class.getName());
		
		//Check if address has changed
    	if (!streetOld.equals(streetNew) || !houseNumberOld.equals(houseNumberNew) || !zipCodeOld.equals(zipCodeNew)
    		|| !cityOld.equals(cityNew))
    	{
    		//Address has changed: insert new address into table.
    		logger.info("Address has changed:\nstreet: " + streetOld + " -> " + streetNew
    				+ "\nhouseNumber: " + houseNumberOld + " -> " + houseNumberNew
    				+ "\nzipCode: " + zipCodeOld + " -> " + zipCodeNew
    				+ "\n city: " + cityOld + " -> " + cityNew);
    		
    		//TODO: Maybe delete old address?
    		PreparedStatement insertAddressStatement = con.prepareStatement(
    				"INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID) "
    				+ "VALUES (?, ?, ?, ?, NULL)");
    		insertAddressStatement.setString(1, streetNew);
    		insertAddressStatement.setString(2, houseNumberNew);
    		insertAddressStatement.setString(3, zipCodeNew);
    		insertAddressStatement.setString(4, cityNew);
    		
    		insertAddressStatement.executeUpdate();
    	} else {
    		logger.info("Address unchanged:\nstreet: " + streetOld + " -> " + streetNew
    				+ "\nhouseNumber: " + houseNumberOld + " -> " + houseNumberNew
    				+ "\nzipCode: " + zipCodeOld + " -> " + zipCodeNew
    				+ "\n city: " + cityOld + " -> " + cityNew);
    		//TODO: Maybe insert the old address, if it is not in the database yet? Or provide an additional function for that?
    	}
    	return getAddressIDByAddress (streetNew, houseNumberNew, zipCodeNew, cityNew, con);
	}
	
	/**
	 * Compares two addresses. If they have changed, insert the new one into the database. Gets the address ID of the current address, regardless if it has changed or not. In this case, no database connection is given, so the default one is used.
	 * @param streetOld The street of the old address.
	 * @param houseNumberOld The housenumber of the old address.
	 * @param zipCodeOld The zip code of the old address.
	 * @param cityOld The city of the old address.
	 * @param streetNew The street of the new address.
	 * @param houseNumberNew The housenumber of the new address.
	 * @param zipCodeNew The ZIP code of the new address.
	 * @param cityNew The city of the new address.
	 * @return The address ID of the new address, regardless if it has changed or not.
	 * @throws SQLException If the statement is malformed, or the address is unchanged but not in the database.
	 */
	public static int getAddressIDWithChangedAddress
	(String streetOld, String houseNumberOld, String zipCodeOld, String cityOld,
	String streetNew, String houseNumberNew, String zipCodeNew, String cityNew) throws SQLException
	{
		return getAddressIDWithChangedAddress (streetOld, houseNumberOld, zipCodeOld, cityOld,
				streetNew, houseNumberNew, zipCodeNew, cityNew, Project.getInstance().getConnection());
	}
	
	public static void updateEMailAddressInTable(String tablename, String eMailOld, String eMailNew, Connection con) throws SQLException {
		Logger logger = Logger.getLogger(SQLHelper.class.getName());
		logger.info("Trying to change E-Mail-Addresses from " + eMailOld + " to " + eMailNew + " in " + tablename + ".");
		
		PreparedStatement updateEMailStatement = con.prepareStatement(
    			"UPDATE " + tablename + " SET E_Mail_Adresse = ? WHERE E_Mail_Adresse = ?");
    	updateEMailStatement.setString(1, eMailNew);
    	updateEMailStatement.setString(2, eMailOld);
    	updateEMailStatement.executeUpdate();
    	
    	logger.info("Changing E-Mail-Addresses done!");
	}
	
	public static void deleteAllEntriesWithEMailAddressInTable(String tablename, String eMail, Connection con) throws SQLException {
		Logger logger = Logger.getLogger(SQLHelper.class.getName());
		logger.info("Trying to delete all entries with E-Mail-Address " + eMail + " from table " + tablename + "...");
		
		PreparedStatement deleteEntriesStatement = con.prepareStatement(
				"DELETE FROM " + tablename + " WHERE E_Mail_Adresse = ?");
		deleteEntriesStatement.setString(1, eMail);
		deleteEntriesStatement.executeUpdate();
		
		logger.info("Deleting entries for E-Mail-Adress " + eMail + " in table " + tablename + " done!");
	}
	
	public static void changeAccountData(Data data, Data data1) throws SQLException {
		Logger logger = Logger.getLogger(SQLHelper.class.getName());
    	logger.info("Trying to change account data from " + data + " to " + data1 + ".");
    	
    	Connection con = Project.getInstance().getConnection();
    	con.getRawConnection().setAutoCommit(false);
    	
    	try {
    		//Check address and insert if neccessary.
        	int addressID = SQLHelper.getAddressIDWithChangedAddress(data.get("Adresse.Straße").toString(),
        			data.get("Adresse.Hausnummer").toString(), data.get("Adresse.PLZ").toString(),
        			data.get("Adresse.Ort").toString(), data1.get("Adresse.Straße").toString(),
        			data1.get("Adresse.Hausnummer").toString(), data1.get("Adresse.PLZ").toString(),
        			data1.get("Adresse.Ort").toString(), con);
        	
        	/* Check if E-Mail-Address has changed. Re-Insert and delete customer if it did, to ensure
        	 * foreign key constraints on occurrence of that customer in other tables during the update.
        	 */
        	PreparedStatement updateKundeStatement;
        	if (!data.get("Kunde.E-Mail-Adresse").toString().equals(data1.get("Kunde.E-Mail-Adresse").toString())) {
        		logger.info("E-Mail-Address changed!");
        		updateKundeStatement = con.prepareStatement(
            			"INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)"
            			+ "VALUES (?, ?, ?, ?, ?)");
            	updateKundeStatement.setString(1, data1.get("Kunde.E-Mail-Adresse").toString());
            	updateKundeStatement.setString(2, data1.get("Kunde.Vorname").toString());
            	updateKundeStatement.setString(3, data1.get("Kunde.Nachname").toString());
            	updateKundeStatement.setString(4, data1.get("Kunde.Passwort").toString());
            	updateKundeStatement.setInt(5, addressID);
            	updateKundeStatement.executeUpdate();
            	
            	String eMailOld = data.get("Kunde.E-Mail-Adresse").toString();
            	String eMailNew = data1.get("Kunde.E-Mail-Adresse").toString();
            	
            	for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS_WITHOUT_KUNDE) {
            		SQLHelper.updateEMailAddressInTable(tablename, eMailOld, eMailNew, con);
            	}
            	
            	PreparedStatement removeOldKundeStatement = con.prepareStatement(
            			"DELETE FROM Kunde WHERE E_Mail_Adresse = ?");
            	removeOldKundeStatement.setString(1, eMailOld);
            	removeOldKundeStatement.executeUpdate();
            	Project.getInstance().getData().replace("email", data1.get("Kunde.E-Mail-Adresse").toString());
        	} else {
        		logger.info("E-Mail-Address unchanged!");
        		updateKundeStatement = con.prepareStatement(
            			"UPDATE Kunde SET Passwort = ?, Vorname = ?, Nachname = ?, Adressen_ID = ? "
            			+ "WHERE E_Mail_Adresse = ?");
        		updateKundeStatement.setString(1, data1.get("Kunde.Passwort").toString());
            	updateKundeStatement.setString(2, data1.get("Kunde.Vorname").toString());
            	updateKundeStatement.setString(3, data1.get("Kunde.Nachname").toString());
            	updateKundeStatement.setInt(4, addressID);
            	updateKundeStatement.setString(5, data.get("Kunde.E-Mail-Adresse").toString());
            	updateKundeStatement.executeUpdate();
        	}
        	
        	con.getRawConnection().commit();
    		con.getRawConnection().setAutoCommit(true);
    	} catch (Exception ex) {
    		con.getRawConnection().rollback();
    		con.getRawConnection().setAutoCommit(true);
    		throw ex;
    	}
    	
		logger.info("Done changing account data for account " + data1.get("Kunde.E-Mail-Adresse").toString() + ".");
	}
	
	public static void deleteAccountByEMail (String eMail) throws SQLException {
		Logger logger = Logger.getLogger(SQLHelper.class.getName());
    	logger.info("Trying to delete account " + eMail + "...");
    	
    	Connection con = Project.getInstance().getConnection();
    	con.getRawConnection().setAutoCommit(false);
    	
    	try {
    		for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS) {
        		SQLHelper.deleteAllEntriesWithEMailAddressInTable(tablename, eMail, con);
        	}
    		con.getRawConnection().commit();
    		con.getRawConnection().setAutoCommit(true);
    	} catch (Exception ex) {
    		con.getRawConnection().rollback();
    		con.getRawConnection().setAutoCommit(true);
    		throw ex;
    	}
    	
    	logger.info("Deletion of account " + eMail + " done!");
	}
}
