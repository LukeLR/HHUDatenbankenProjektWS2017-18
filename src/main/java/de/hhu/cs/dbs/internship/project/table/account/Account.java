package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

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
    	String updateQuery = "UPDATE Kunde SET "
    			+ "E_Mail_Adresse = " + data1.get("E-Mail-Adresse") + ", "
    			+ "Passwort = " + data1.get("Passwort") + ", "
    			+ "Vorname = " + data1.get("Vorname") + ", "
    			+ "Nachname = " + data1.get("Nachname") + " "
    			+ "WHERE E_Mail_Adresse = " + data.get("E-Mail-Adresse") + "; ";
    	updateQuery += "UPDATE Adresse SET "
    			+ "Strasse = " + data1.get("Straße") + ", "
    			+ "Hausnummer = " + data1.get("Hausnummer") + ", "
    			+ "PLZ = " + data1.get("PLZ") + ", "
    			+ "Ort = " + data1.get("Ort") + " "
    			+ "WHERE Adressen_ID IN (SELECT Adressen_ID FROM Kunde "
    			+ "WHERE E_Mail_Adresse = " + data.get("E-Mail-Adresse") + ");";
    	return updateQuery;
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException(getClass().getName() + ".deleteRowWithData(Data) nicht implementiert.");
    }
}
