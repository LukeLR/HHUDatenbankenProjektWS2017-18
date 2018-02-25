package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

public class Premiumkunde extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Showing " + this.getClass().getName());
		String selectQuery = "SELECT * FROM (SELECT Ablaufdatum, Studierendenausweis, Kunde.E_Mail_Adresse, "
				+ "Vorname, Nachname, Passwort, Adressen_ID AS 'AID' FROM Premiumkunde "
				+ "JOIN Kunde ON Premiumkunde.E_Mail_Adresse = Kunde.E_Mail_Adresse) "
				+ "JOIN Adresse ON AID = Adresse.Adressen_ID";
		if (filter != null && !filter.isEmpty()) {
			selectQuery += " AND Kunde.E_Mail_Adresse LIKE '%" + filter + "'";
		}
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Showing " + this.getClass().getName() + " for Data " + data.toString());
		String selectQuery = "SELECT * FROM Premiumkunde "
				+ "WHERE E_Mail_Adresse = '" + data.get("Kunde.E_Mail_Adresse") + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to insert new Dataset with data: " + data.toString());
		
		if (data.get("Premiumkunde.Studierendenausweis") != null &&
		!data.get("Premiumkunde.Studierendenausweis").toString().isEmpty()) {
			//TODO: Einfügen von Bildern implementieren
			/*PreparedStatement insertPremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse) "
					+ "VALUES (?, ?, ?)");
			insertPremiumkundeStatement.setString(1, data.get("Premiumkunde.Ablaufdatum").toString());
			insertPremiumkundeStatement.setString(2, data.get("Premiumkunde.Studierendenausweis").toString());
			insertPremiumkundeStatement.setString(3, data.get("Premiumkunde.E_Mail_Adresse").toString());
			insertPremiumkundeStatement.executeUpdate();*/
		} else {
			PreparedStatement insertPremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse) "
					+ "VALUES (?, NULL, ?)");
			insertPremiumkundeStatement.setString(1, data.get("Premiumkunde.Ablaufdatum").toString());
			insertPremiumkundeStatement.setString(2, data.get("Premiumkunde.E_Mail_Adresse").toString());
			insertPremiumkundeStatement.executeUpdate();
		}
		
		logger.info("Inserted Premiumkunde data!");
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		// TODO Premiumkunde updateRowWithData implementieren
		// TODO: Aktualisierung mit Bildern implementieren

	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to delete Dataset with data: " + data.toString());
		
		PreparedStatement deletePremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Premiumkunde WHERE E_Mail_Adresse = ?");
		deletePremiumkundeStatement.setString(1, data.get("Kunde.E_Mail_Adresse").toString());
		deletePremiumkundeStatement.executeUpdate();
		
		logger.info("Dataset for E-Mail " + data.get("Kunde.E_Mail_Adresse").toString() + " deleted!");
	}

}
