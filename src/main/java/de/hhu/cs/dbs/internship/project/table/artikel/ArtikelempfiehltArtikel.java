package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class ArtikelempfiehltArtikel extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery =
				"SELECT 'Artikel1-Bezeichnung', 'Artikel1-Beschreibung', 'Artikel1-Bild', "
				+ "'Artikel1-Artikel_ID', 'Artikel2-Artikel_ID', "
				+ "Bezeichnung AS 'Artikel2-Bezeichnung', "
				+ "Beschreibung AS 'Artikel2-Beschreibung', "
				+ "Bild AS 'Artikel2-Bild' FROM ("
					+ "SELECT Bezeichnung AS 'Artikel1-Bezeichnung', "
					+ "Beschreibung AS 'Artikel1-Beschreibung', "
					+ "Bild AS 'Artikel1-Bild', "
					+ "Artikel_ID1 AS 'Artikel1-Artikel_ID', "
					+ "Artikel_ID2 AS 'Artikel2-Artikel_ID' "
					+ "FROM Artikel_empfiehlt_Artikel JOIN Artikel "
					+ "ON Artikel_empfiehlt_Artikel.Artikel_ID1 = Artikel.Artikel_ID) "
				+ "JOIN Artikel ON 'Artikel2-Artikel_ID' = Artikel.Artikel_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Artikel1-Beschreibung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
