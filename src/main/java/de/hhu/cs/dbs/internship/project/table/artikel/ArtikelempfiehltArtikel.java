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
			selectQuery += " WHERE 'Artikel1-Bezeichnung' LIKE '%" + filter + "%' OR "
					+ "'Artikel2-Bezeichnung' LIKE '%" + filter + "%'";
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
