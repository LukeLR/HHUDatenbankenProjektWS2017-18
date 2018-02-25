package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

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
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to insert new Dataset with data: " + data.toString());
		
		PreparedStatement insertAngestellterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse) "
				+ "VALUES (?, ?, ?)");
		insertAngestellterStatement.setString(1, data.get("Angestellter.Jobbezeichnung").toString());
		insertAngestellterStatement.setInt(2, Integer.valueOf(data.get("Angestellter.Gehalt").toString()));
		insertAngestellterStatement.setString(3, data.get("Angestellter.E_Mail_Adresse").toString());
		insertAngestellterStatement.executeUpdate();
		
		logger.info("Dataset inserted into " + this.getClass().getName() + "!");
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to change account data from " + oldData + " to " + newData + ".");
		
		PreparedStatement updateAngestellterStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Angestellter SET Jobbezeichnung = ?, Gehalt = ?, E_Mail_Adresse = ? "
				+ "WHERE E_Mail_Adresse = ?");
		updateAngestellterStatement.setString(1, newData.get("Angestellter.Jobbezeichnung").toString());
		updateAngestellterStatement.setString(2, newData.get("Angestellter.Gehalt").toString());
		updateAngestellterStatement.setString(3, newData.get("Angestellter.E_Mail_Adresse").toString());
		updateAngestellterStatement.setString(4, oldData.get("Angestellter.E_Mail_Adresse").toString());
		updateAngestellterStatement.executeUpdate();
		
		logger.info("Done changing account data for account " +
				newData.get("Kunde.E-Mail-Adresse").toString() + ".");
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to delete Dataset with data: " + data.toString());
		
		PreparedStatement deleteAngestellterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Angestellter WHERE E_Mail_Adresse = ?");
		deleteAngestellterStatement.setString(1, data.get("Kunde.E_Mail_Adresse").toString());
		deleteAngestellterStatement.executeUpdate();
		
		logger.info("Dataset for E-Mail " + data.get("Kunde.E_Mail_Adresse").toString()
				+ " deleted from " + this.getClass().getName() + ".");
	}

}
