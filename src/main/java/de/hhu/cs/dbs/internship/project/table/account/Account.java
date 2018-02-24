package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.SQLHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Account extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Login event");
    	logger.info("Showing " + this.getClass().getName() + " for E-Mail "
    			+ Project.getInstance().getData().get("email") + ".");
    	String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Vorname, Nachname, "
    			+ "Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
    			+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID WHERE E_Mail_Adresse = '" 
    			+ Project.getInstance().getData().get("email") + "'";
        return selectQuery;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Login event");
    	logger.info("Showing " + this.getClass().getName() + " for Data " + data.toString());
    	String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Passwort, Vorname,"
    			+ "Nachname, Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
    			+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID WHERE E_Mail_Adresse = '" 
    			+ data.get("Kunde.E-Mail-Adresse") + "'";
        return selectQuery;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        throw new SQLException("Es können keine weiteren Accounts für einen Kunden angelegt werden!");
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Login event");
    	logger.info("Trying to change account data from " + data + " to " + data1 + ".");
    	
    	Connection con = Project.getInstance().getConnection();
    	con.getRawConnection().setAutoCommit(false);
    	
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
        	String eMailNew = data.get("Kunde.E-Mail-Adresse").toString();
        	
        	SQLHelper.updateEMailAdressInTable( "Premiumkunde", eMailOld, eMailNew, con);
        	SQLHelper.updateEMailAdressInTable( "Angestellter", eMailOld, eMailNew, con);
        	SQLHelper.updateEMailAdressInTable(    "Warenkorb", eMailOld, eMailNew, con);
        	SQLHelper.updateEMailAdressInTable(   "Newsletter", eMailOld, eMailNew, con);
        	SQLHelper.updateEMailAdressInTable("Newsletterabo", eMailOld, eMailNew, con);
        	
        	PreparedStatement removeOldKundeStatement = con.prepareStatement(
        			"DELETE FROM Kunde WHERE E_Mail_Adresse = ?");
        	removeOldKundeStatement.setString(1, eMailOld);
        	removeOldKundeStatement.executeUpdate();
        	Project.getInstance().getData().replace("email", data1.get("Kunde.E-Mail-Adresse"));
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
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException(getClass().getName() + ".deleteRowWithData(Data) nicht implementiert.");
    }
}
