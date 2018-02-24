package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.DatabaseInfo;
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
    	SQLHelper.changeAccountData(data, data1);
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Login event");
    	logger.info("Trying to delete account " + data.get("Kunde.E-Mail-Adresse").toString() + "...");
    	
    	Connection con = Project.getInstance().getConnection();
    	con.getRawConnection().setAutoCommit(false);
    	
    	try {
    		for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS) {
        		SQLHelper.deleteAllEntriesWithEMailAddressInTable(tablename,
        				data.get("Kunde.E-Mail-Adresse").toString(), con);
        	}
    		con.getRawConnection().commit();
    		con.getRawConnection().setAutoCommit(true);
    	} catch (Exception ex) {
    		con.getRawConnection().rollback();
    		con.getRawConnection().setAutoCommit(true);
    		throw ex;
    	}
    	
    	logger.info("Deletion of account " + data.get("Kunde.E-Mail-Adresse").toString() + " done!");
    }
}
