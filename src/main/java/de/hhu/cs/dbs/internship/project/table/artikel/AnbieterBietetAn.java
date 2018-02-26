package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

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
