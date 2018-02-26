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
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Bezeichnung, Beschreibung, Bild, Artikel_ID FROM Artikel "
				+ "WHERE Artikel_ID = '" + (String) data.get("Artikel.Artikel_ID") + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID) "
				+ "VALUES (?, ?, ?, NULL)");
		insertArtikelStatement.setString(1, (String) data.get("Artikel.Bezeichnung"));
		insertArtikelStatement.setString(2, (String) data.get("Artikel.Beschreibung"));
		insertArtikelStatement.setString(3, (String) data.get("Artikel.Bild"));
		insertArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, (String) data.get("Artikel.Bezeichnung"));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Artikel SET Bezeichnung = ?, Beschreibung = ?, Bild = ? "
				+ "WHERE Artikel_ID = ?");
		updateArtikelStatement.setString(1, (String) newData.get("Artikel.Bezeichnung"));
		updateArtikelStatement.setString(2, (String) newData.get("Artikel.Beschreibung"));
		updateArtikelStatement.setString(3, (String) newData.get("Artikel.Bild"));
		updateArtikelStatement.setInt(4, (int) oldData.get("Artikel.Artikel_ID"));
		updateArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, (String) newData.get("Artikel.Bezeichnung"));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Artikel WHERE Artikel_ID = ?");
		deleteArtikelStatement.setInt(1, (int) data.get("Artikel.Artikel_ID"));
		deleteArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), (String) data.get("Artikel.Artikel_ID"));
	}
}
