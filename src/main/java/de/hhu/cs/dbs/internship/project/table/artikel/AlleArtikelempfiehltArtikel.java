package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleArtikelempfiehltArtikel extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
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
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Artikel_ID1, Artikel_ID2 FROM Artikel_empfiehlt_Artikel "
				+ "WHERE Artikel_ID1 = '" + String.valueOf(data.get("Artikel.Artikel1-Artikel_ID")) + "' "
				+ "AND Artikel_ID2 = '" + String.valueOf(data.get("Artikel.Artikel2-Artikel_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertArtikelEmpfiehltArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2) "
				+ "VALUES (?, ?)");
		insertArtikelEmpfiehltArtikelStatement.setInt
			(1, Integer.valueOf(String.valueOf(data.get("Artikel_empfiehlt_Artikel.Artikel_ID1"))));
		insertArtikelEmpfiehltArtikelStatement.setInt
			(2, Integer.valueOf(String.valueOf(data.get("Artikel_empfiehlt_Artikel.Artikel_ID2"))));
		insertArtikelEmpfiehltArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Artikel_empfiehlt_Artikel.Artikel_ID1")) + "-"
				+ String.valueOf(data.get("Artikel_empfiehlt_Artikel.Artikel_ID2")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateArtikelEmpfiehltArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Artikel_empfiehlt_Artikel SET Artikel_ID1 = ?, Artikel_ID2 = ? "
				+ "WHERE Artikel_ID1 = ? AND Artikel_ID2 = ?");
		updateArtikelEmpfiehltArtikelStatement.setInt
			(1, Integer.valueOf(String.valueOf(newData.get("Artikel_empfiehlt_Artikel.Artikel_ID1"))));
		updateArtikelEmpfiehltArtikelStatement.setInt
			(2, Integer.valueOf(String.valueOf(newData.get("Artikel_empfiehlt_Artikel.Artikel_ID2"))));
		updateArtikelEmpfiehltArtikelStatement.setInt
			(3, Integer.valueOf(String.valueOf(oldData.get("Artikel_empfiehlt_Artikel.Artikel_ID1"))));
		updateArtikelEmpfiehltArtikelStatement.setInt
			(4, Integer.valueOf(String.valueOf(oldData.get("Artikel_empfiehlt_Artikel.Artikel_ID2"))));
		updateArtikelEmpfiehltArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Artikel_empfiehlt_Artikel.Artikel_ID1")) + "-"
				+ String.valueOf(newData.get("Artikel_empfiehlt_Artikel.Artikel_ID2")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteArtikelEmpfiehltArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Artikel_empfiehlt_Artikel "
				+ "WHERE Artikel_ID1 = ? AND Artikel_ID2 = ?");
		deleteArtikelEmpfiehltArtikelStatement.setInt
			(1, Integer.valueOf(String.valueOf(data.get("Artikel.Artikel1-Artikel_ID"))));
		deleteArtikelEmpfiehltArtikelStatement.setInt
			(2, Integer.valueOf(String.valueOf(data.get("Artikel.Artikel2-Artikel_ID"))));
		deleteArtikelEmpfiehltArtikelStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data,
				String.valueOf(data.get("Artikel.Artikel1-Artikel_ID")) + "-"
				+ String.valueOf(data.get("Artikel.Artikel2-Artikel_ID")));
	}

}
