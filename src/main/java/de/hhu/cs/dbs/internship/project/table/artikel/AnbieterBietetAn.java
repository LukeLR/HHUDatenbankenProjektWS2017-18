package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AnbieterBietetAn extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery =
				"SELECT "
					+ "Anbieter_bietet_an.Anbieterbezeichnung, "
					+ "Anbieter_bietet_an.Angebots_ID, "
					+ "Anbieter_bietet_an.Bestand, "
					+ "Angebot.Angebots_ID, "
					+ "Angebot.Artikel_ID, "
					+ "Angebot.Preis, "
					+ "Artikel.Bezeichnung, "
					+ "Artikel.Beschreibung, "
					+ "Artikel.Bild "
				+ "FROM Anbieter_bietet_an "
				+ "JOIN Angebot "
				+ "ON Anbieter_bietet_an.Angebots_ID = Angebot.Angebots_ID "
				+ "JOIN Artikel "
				+ "ON Angebot.Artikel_ID = Artikel.Artikel_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Artikel.Bezeichnung LIKE '%" + filter + "%' OR "
					+ "Artikel.Beschreibung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Anbieterbezeichnung, Angebots_ID, Bestand "
				+ "FROM Anbieter_bietet_an "
				+ "WHERE Anbieterbezeichnung = '" + String.valueOf(data.get("Anbieter_bietet_an.Anbieterbezeichnung"))
				+ "' AND Angebots_ID = '" + String.valueOf(data.get("Anbieter_bietet_an.Angebots_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAnbieterBietetAnStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand) "
				+ "VALUES (?, ?, ?)");
		insertAnbieterBietetAnStatement.setString(1, String.valueOf(data.get("Anbieter_bietet_an.Anbieterbezeichnung")));
		insertAnbieterBietetAnStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Anbieter_bietet_an.Angebots_ID"))));
		insertAnbieterBietetAnStatement.setInt(3, Integer.valueOf(String.valueOf(data.get("Anbieter_bietet_an.Bestand"))));
		insertAnbieterBietetAnStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Anbieter_bietet_an.Anbieterbezeichnung")) + "-"
				+ String.valueOf(data.get("Anbieter_bietet_an.Angebots_ID")) + "-"
				+ String.valueOf(data.get("Anbieter_bietet_an.Bestand")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateAnbieterBietetAnStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Anbieter_bietet_an "
				+ "SET Anbieterbezeichnung = ?, "
					+ "Angebots_ID = ?, "
					+ "Bestand = ? "
				+ "WHERE Anbieterbezeichnung = ?, "
					+ "Angebots_ID = ?, "
					+ "Bestand = ?");
		updateAnbieterBietetAnStatement.setString(1, String.valueOf(newData.get("Anbieter_bietet_an.Anbieterbezeichnung")));
		updateAnbieterBietetAnStatement.setInt(2, Integer.valueOf(String.valueOf(newData.get("Anbieter_bietet_an.Angebots_ID"))));
		updateAnbieterBietetAnStatement.setInt(3, Integer.valueOf(String.valueOf(newData.get("Anbieter_bietet_an.Bestand"))));
		updateAnbieterBietetAnStatement.setString(4, String.valueOf(oldData.get("Anbieter_bietet_an.Anbieterbezeichnung")));
		updateAnbieterBietetAnStatement.setInt(5, Integer.valueOf(String.valueOf(oldData.get("Anbieter_bietet_an.Angebots_ID"))));
		updateAnbieterBietetAnStatement.setInt(6, Integer.valueOf(String.valueOf(oldData.get("Anbieter_bietet_an.Bestand"))));
		updateAnbieterBietetAnStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Anbieter_bietet_an.Anbieterbezeichnung")) + "-"
				+ String.valueOf(newData.get("Anbieter_bietet_an.Angebots_ID")) + "-"
				+ String.valueOf(newData.get("Anbieter_bietet_an.Bestand")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
