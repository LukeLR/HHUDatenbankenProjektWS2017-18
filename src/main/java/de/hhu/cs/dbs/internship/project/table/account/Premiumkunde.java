package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Premiumkunde extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		String selectQuery = "SELECT * FROM (SELECT Ablaufdatum, Studierendenausweis, Kunde.E_Mail_Adresse, "
				+ "Vorname, Nachname, Passwort, Adressen_ID AS 'AID' FROM Premiumkunde "
				+ "JOIN Kunde ON Premiumkunde.E_Mail_Adresse = Kunde.E_Mail_Adresse) "
				+ "JOIN Adresse ON AID = Adresse.Adressen_ID";
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " AND E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		String selectQuery = "SELECT * FROM Premiumkunde "
				+ "WHERE E_Mail_Adresse = '" + String.valueOf(data.get("Kunde.E_Mail_Adresse")) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		if (data.get("Premiumkunde.Studierendenausweis") != null &&
		!data.get("Premiumkunde.Studierendenausweis").toString().isEmpty()) {
			//TODO: Einfügen von Bildern implementieren
			/*PreparedStatement insertPremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse) "
					+ "VALUES (?, ?, ?)");
			insertPremiumkundeStatement.setString(1, String.valueOf(data.get("Premiumkunde.Ablaufdatum")));
			insertPremiumkundeStatement.setString(2, String.valueOf(data.get("Premiumkunde.Studierendenausweis")));
			insertPremiumkundeStatement.setString(3, String.valueOf(data.get("Premiumkunde.E_Mail_Adresse")));
			insertPremiumkundeStatement.executeUpdate();*/
		} else {
			PreparedStatement insertPremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse) "
					+ "VALUES (?, NULL, ?)");
			insertPremiumkundeStatement.setString(1, String.valueOf(data.get("Premiumkunde.Ablaufdatum")));
			insertPremiumkundeStatement.setString(2, String.valueOf(data.get("Premiumkunde.E_Mail_Adresse")));
			insertPremiumkundeStatement.executeUpdate();
		}
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Premiumkunde.E_Mail_Adresse")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		// TODO Premiumkunde updateRowWithData implementieren
		// TODO: Aktualisierung mit Bildern implementieren

	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deletePremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Premiumkunde WHERE E_Mail_Adresse = ?");
		deletePremiumkundeStatement.setString(1, String.valueOf(data.get("Kunde.E_Mail_Adresse")));
		deletePremiumkundeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Kunde.E_Mail_Adresse")));
	}

}
