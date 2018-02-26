package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Angebot extends Table {
	
	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Bezeichnung, Beschreibung, Bild, Artikel.Artikel_ID, "
				+ "Angebots_ID, Preis FROM Angebot JOIN Artikel "
				+ "ON Angebot.Artikel_ID = Artikel.Artikel_ID";
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Bezeichnung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Artikel_ID, Preis, Angebots_ID FROM Angebot "
				+ "WHERE Angebots_ID = '" + String.valueOf(data.get("Angebot.Angebots_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAngebotStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis) VALUES (NULL, ?, ?)");
		insertAngebotStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Angebot.Artikel_ID"))));
		//TODO: Check for prices with decimals
		insertAngebotStatement.setFloat(2, Float.valueOf(String.valueOf(data.get("Angebot.Preis"))));
		insertAngebotStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Angebot.Artikel_ID")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateAngebotStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Angebot SET Artikel_ID = ?, Preis = ? WHERE Angebots_ID = ?");
		updateAngebotStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("Angebot.Artikel_ID"))));
		updateAngebotStatement.setLong(2, Long.valueOf(String.valueOf(newData.get("Angebot.Preis"))));
		updateAngebotStatement.setInt(3, Integer.valueOf(String.valueOf(oldData.get("Angebot.Angebots_ID"))));
		updateAngebotStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(oldData.get("Angebot.Angebots_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		/*
		 * TODO: Wenn Angebote noch in einem Warenkorb oder in einer anderen Tabelle liegen, können diese nicht gelöscht
		 * werden (Foreign Key Constraint). Zu entscheiden: Sollen diese Angebote dann auch aus allen Warenkörben gelöscht
		 * werden können, damit sie aus der Datenbank gelöscht werden können? Oder sollen solche Angebote einfach nicht
		 * gelöscht werden können?
		 */
		PreparedStatement deleteAngebotStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Angebot WHERE Angebots_ID = ?");
		deleteAngebotStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Angebot.Angebots_ID"))));
		deleteAngebotStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Angebot.Angebots_ID")));
	}

}
