package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

public class Angestellter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Showing " + this.getClass().getName());
		String selectQuery = "SELECT * FROM (SELECT Jobbezeichnung, Gehalt, Kunde.E_Mail_Adresse, "
				+ "Vorname, Nachname, Passwort, Adressen_ID AS 'AID' FROM Angestellter "
				+ "JOIN Kunde ON Angestellter.E_Mail_Adresse = Kunde.E_Mail_Adresse) "
				+ "JOIN Adresse ON AID = Adresse.Adressen_ID";
		if (filter != null && !filter.isEmpty()) {
			logger.info("Searching for " + filter + " in " + this.getClass().getName() + " table");
			selectQuery += " AND E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to get Data for Dataset " + data.toString() + " in " + this.getClass().getName() + ".");
		
		String selectQuery = "SELECT * FROM Angestellter "
				+ "WHERE E_Mail_Adresse = '" + data.get("Kunde.E_Mail_Adresse") + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
