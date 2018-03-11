package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Artikel extends Table {
	
	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Bezeichnung, Beschreibung, Bild, Artikel_ID FROM Artikel";
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Bezeichnung LIKE '%" + filter + "%' "
					+ "OR Beschreibung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Bezeichnung, Beschreibung, Bild, Artikel_ID FROM Artikel "
				+ "WHERE Artikel_ID = '" + String.valueOf(data.get("Artikel.Artikel_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		if (data.get("Artikel.Bild") != null) {
			logger.info("Artikel.Bild is NOT NULL on insert!");
			PreparedStatement insertArtikelStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID) "
					+ "VALUES (?, ?, ?, NULL)");
			insertArtikelStatement.setString(1, String.valueOf(data.get("Artikel.Bezeichnung")));
			insertArtikelStatement.setString(2, String.valueOf(data.get("Artikel.Beschreibung")));
			insertArtikelStatement.setObject(3, data.get("Artikel.Bild"));
			insertArtikelStatement.executeUpdate();
		} else {
			logger.info("Artikel.Bild is NULL on insert!");
			PreparedStatement insertArtikelStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID) "
					+ "VALUES (?, ?, NULL, NULL)");
			insertArtikelStatement.setString(1, String.valueOf(data.get("Artikel.Bezeichnung")));
			insertArtikelStatement.setString(2, String.valueOf(data.get("Artikel.Beschreibung")));
			insertArtikelStatement.executeUpdate();
		}
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Artikel.Bezeichnung")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		if (newData.get("Artikel.Bild") != null) {
			logger.info("Artikel.Bild is NOT NULL on update!");
			PreparedStatement updateArtikelStatement = Project.getInstance().getConnection().prepareStatement(
					"UPDATE Artikel SET Bezeichnung = ?, Beschreibung = ?, Bild = ? "
					+ "WHERE Artikel_ID = ?");
			updateArtikelStatement.setString(1, String.valueOf(newData.get("Artikel.Bezeichnung")));
			updateArtikelStatement.setString(2, String.valueOf(newData.get("Artikel.Beschreibung")));
			updateArtikelStatement.setObject(3, newData.get("Artikel.Bild"));
			updateArtikelStatement.setInt(4, (int) oldData.get("Artikel.Artikel_ID"));
			updateArtikelStatement.executeUpdate();
		} else {
			logger.info("Artikel.Bild is NULL on update!");
			PreparedStatement updateArtikelStatement = Project.getInstance().getConnection().prepareStatement(
					"UPDATE Artikel SET Bezeichnung = ?, Beschreibung = ?, Bild = NULL "
					+ "WHERE Artikel_ID = ?");
			updateArtikelStatement.setString(1, String.valueOf(newData.get("Artikel.Bezeichnung")));
			updateArtikelStatement.setString(2, String.valueOf(newData.get("Artikel.Beschreibung")));
			updateArtikelStatement.setInt(3, Integer.valueOf(String.valueOf(oldData.get("Artikel.Artikel_ID"))));
			updateArtikelStatement.executeUpdate();
		}
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Artikel.Bezeichnung")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Artikel WHERE Artikel_ID = ?");
		deleteArtikelStatement.setInt(1, (int) data.get("Artikel.Artikel_ID"));
		deleteArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), String.valueOf(data.get("Artikel.Artikel_ID")));
	}
}
