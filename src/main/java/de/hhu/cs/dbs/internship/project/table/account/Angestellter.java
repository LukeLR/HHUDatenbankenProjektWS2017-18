package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Angestellter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT * FROM (SELECT Jobbezeichnung, Gehalt, Kunde.E_Mail_Adresse, "
				+ "Vorname, Nachname, Passwort, Adressen_ID AS 'AID' FROM Angestellter "
				+ "JOIN Kunde ON Angestellter.E_Mail_Adresse = Kunde.E_Mail_Adresse) "
				+ "JOIN Adresse ON AID = Adresse.Adressen_ID";
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT * FROM Angestellter "
				+ "WHERE E_Mail_Adresse = '" + String.valueOf(data.get("Kunde.E_Mail_Adresse")) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAngestellterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse) "
				+ "VALUES (?, ?, ?)");
		insertAngestellterStatement.setString(1, String.valueOf(data.get("Angestellter.Jobbezeichnung")));
		insertAngestellterStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Angestellter.Gehalt"))));
		insertAngestellterStatement.setString(3, String.valueOf(data.get("Angestellter.E_Mail_Adresse")));
		insertAngestellterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Angestellter.E_Mail_Adresse")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateAngestellterStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Angestellter SET Jobbezeichnung = ?, Gehalt = ?, E_Mail_Adresse = ? "
				+ "WHERE E_Mail_Adresse = ?");
		updateAngestellterStatement.setString(1, String.valueOf(newData.get("Angestellter.Jobbezeichnung")));
		updateAngestellterStatement.setInt(2, Integer.valueOf(String.valueOf(newData.get("Angestellter.Gehalt"))));
		updateAngestellterStatement.setString(3, String.valueOf(newData.get("Angestellter.E_Mail_Adresse")));
		updateAngestellterStatement.setString(4, String.valueOf(oldData.get("Angestellter.E_Mail_Adresse")));
		updateAngestellterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Angestellter.E_Mail_Adresse")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteAngestellterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Angestellter WHERE E_Mail_Adresse = ?");
		deleteAngestellterStatement.setString(1, String.valueOf(data.get("Kunde.E_Mail_Adresse")));
		deleteAngestellterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Kunde.E_Mail_Adresse")));
	}

}
