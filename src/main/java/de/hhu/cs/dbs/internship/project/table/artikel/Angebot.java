package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

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
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Artikel_ID, Preis, Angebots_ID FROM Angebot "
				+ "WHERE Angebots_ID = '" + String.valueOf(data.get("Angebot.Angebots_ID")) + "'";
		return selectQuery;
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
