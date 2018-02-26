package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class ArtikelempfiehltArtikel extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery =
				"SELECT "
						+ "artikel1.Bezeichnung AS 'Artikel1-Bezeichnung', "
						+ "artikel1.Beschreibung AS 'Artikel1-Beschreibung', "
						+ "artikel1.Bild AS 'Artikel1-Bild', "
						+ "artikel1.Artikel_ID AS 'Artikel1-Artikel_ID', "
						+ "artikel2.Artikel_ID AS 'Artikel2-Artikel_ID', "
						+ "artikel2.Bezeichnung AS 'Artikel2-Bezeichnung', "
						+ "artikel2.Beschreibung AS 'Artikel2-Beschreibung', "
						+ "artikel2.Bild AS 'Artikel2-Bild'"
				+ "FROM Artikel_empfiehlt_Artikel empfiehlt "
				+ "JOIN Artikel artikel1 "
				+ "ON empfiehlt.Artikel_ID1 = artikel1.Artikel_ID "
				+ "JOIN Artikel artikel2 "
				+ "ON empfiehlt.Artikel_ID2 = artikel2.Artikel_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE artikel1.Bezeichnung LIKE '%" + filter + "%' OR "
					+ "artikel2.Bezeichnung LIKE '%" + filter + "%'";
		}
		
		//TODO: Use UnifiedLoggingHelper.logShowDone on other Tables
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Artikel_ID1, Artikel_ID2 FROM Artikel_empfiehlt_Artikel "
				+ "WHERE Artikel_ID1 = '" + String.valueOf(data.get("Artikel.Artikel1-Artikel_ID")) + "' "
				+ "AND Artikel_ID2 = '" + String.valueOf(data.get("Artikel.Artikel2-Artikel_ID")) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertArtikelEmpfiehltArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2)"
				+ "VALUES (?, ?)");
		insertArtikelEmpfiehltArtikelStatement.setInt
			(1, Integer.valueOf(String.valueOf(data.get("Artikel.Artikel1-Artikel_ID"))));
		insertArtikelEmpfiehltArtikelStatement.setInt
			(2, Integer.valueOf(String.valueOf(data.get("Artikel.Artikel2-Artikel_ID"))));
		insertArtikelEmpfiehltArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Artikel.Artikel1-Artikel_ID")) + "-"
				+ String.valueOf(data.get("Artikel.Artikel2-Artikel_ID")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateArtikelEmpfiehltArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Artikel_empfiehlt_Artikel SET Artikel_ID1 = ?, Artikel_ID2 = ? "
				+ "WHERE Artikel_ID1 = ? AND Artikel_ID2 = ?");
		updateArtikelEmpfiehltArtikelStatement.setInt
			(1, Integer.valueOf(String.valueOf(newData.get("Artikel.Artikel1-Artikel_ID"))));
		updateArtikelEmpfiehltArtikelStatement.setInt
			(2, Integer.valueOf(String.valueOf(newData.get("Artikel.Artikel2-Artikel_ID"))));
		updateArtikelEmpfiehltArtikelStatement.setInt
			(3, Integer.valueOf(String.valueOf(oldData.get("Artikel.Artikel1-Artikel_ID"))));
		updateArtikelEmpfiehltArtikelStatement.setInt
			(4, Integer.valueOf(String.valueOf(oldData.get("Artikel.Artikel2-Artikel_ID"))));
		updateArtikelEmpfiehltArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Artikel.Artikel1-Artikel_ID")) + "-"
				+ String.valueOf(newData.get("Artikel.Artikel2-Artikel_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
