package de.hhu.cs.dbs.internship.project.table.newsletter;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class ArtikelImNewsletter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT "
				+ "Artikel.Bezeichnung, "
				+ "Artikel.Beschreibung, "
				+ "Artikel.Bild, "
				+ "Artikel_im_Newsletter.Artikel_ID, "
				+ "Artikel_im_Newsletter.Newsletter_ID, "
				+ "Newsletter.Betreff, "
				+ "Newsletter.Datum, "
				+ "Newsletter.E_Mail_Adresse AS Herausgeber "
				+ "FROM Artikel_im_Newsletter "
				+ "JOIN Artikel ON Artikel_im_Newsletter.Artikel_ID = Artikel.Artikel_ID "
				+ "JOIN Newsletter ON Artikel_im_Newsletter.Newsletter_ID = Newsletter.Newsletter_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Newsletter.E_Mail_Adresse LIKE '%" + filter + "%' OR"
					+ " Artikel.Bezeichnung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT "
				+ "Artikel_im_Newsletter.Artikel_ID, "
				+ "Artikel_im_Newsletter.Newsletter_ID "
				+ "FROM Artikel_im_Newsletter "
				+ "WHERE Artikel_im_Newsletter.Artikel_ID = '" + data.get("Artikel_im_Newsletter.Artikel_ID") + "' "
				+ "AND Artikel_im_Newsletter.Newsletter_ID = '" + data.get("Artikel_im_Newsletter.Newsletter_ID") + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
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
