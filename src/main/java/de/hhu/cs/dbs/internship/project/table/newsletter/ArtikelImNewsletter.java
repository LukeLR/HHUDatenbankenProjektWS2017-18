package de.hhu.cs.dbs.internship.project.table.newsletter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
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
				+ "WHERE Artikel_im_Newsletter.Artikel_ID = '"
				+ String.valueOf(data.get("Artikel_im_Newsletter.Artikel_ID")) + "' "
				+ "AND Artikel_im_Newsletter.Newsletter_ID = '"
				+ String.valueOf(data.get("Artikel_im_Newsletter.Newsletter_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertArtikelImNewsletterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) "
				+ "VALUES (?, ?)");
		insertArtikelImNewsletterStatement.setInt(1,
				Integer.valueOf(String.valueOf(data.get("Artikel_im_Newsletter.Artikel_ID"))));
		insertArtikelImNewsletterStatement.setInt(2,
				Integer.valueOf(String.valueOf(data.get("Artikel_im_Newsletter.Newsletter_ID"))));
		insertArtikelImNewsletterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Artikel_im_Newsletter.Artikel_ID")) + "-"
				+ String.valueOf(data.get("Artikel_im_Newsletter.Newsletter_ID")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateArtikelImNewsletterStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Artikel_im_Newsletter SET "
				+ "Artikel_im_Newsletter.Artikel_ID = ?, "
				+ "Artikel_im_Newsletter.Newsletter_ID = ? "
				+ "WHERE Artikel_im_Newsletter.Artikel_ID = ?, "
				+ "Artikel_im_Newsletter.Newsletter_ID = ?");
		updateArtikelImNewsletterStatement.setInt(1,
				Integer.valueOf(String.valueOf(newData.get("Artikel_im_Newsletter.Artikel_ID"))));
		updateArtikelImNewsletterStatement.setInt(2,
				Integer.valueOf(String.valueOf(newData.get("Artikel_im_Newsletter.Newsletter_ID"))));
		updateArtikelImNewsletterStatement.setInt(3,
				Integer.valueOf(String.valueOf(oldData.get("Artikel_im_Newsletter.Artikel_ID"))));
		updateArtikelImNewsletterStatement.setInt(4,
				Integer.valueOf(String.valueOf(oldData.get("Artikel_im_Newsletter.Newsletter_ID"))));
		updateArtikelImNewsletterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Artikel_im_Newsletter.Artikel_ID")) + "-"
				+ String.valueOf(newData.get("Artikel_im_Newsletter.Newsletter_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteArtikelImNewsletterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Artikel_im_Newsletter "
				+ "WHERE Artikel_im_Newsletter.Artikel_ID = ?, "
				+ "Artikel_im_Newsletter.Newsletter_ID = ?");
		deleteArtikelImNewsletterStatement.setInt(3,
				Integer.valueOf(String.valueOf(data.get("Artikel_im_Newsletter.Artikel_ID"))));
		deleteArtikelImNewsletterStatement.setInt(4,
				Integer.valueOf(String.valueOf(data.get("Artikel_im_Newsletter.Newsletter_ID"))));
		deleteArtikelImNewsletterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data,
				String.valueOf(data.get("Artikel_im_Newsletter.Artikel_ID")) + "-"
				+ String.valueOf(data.get("Artikel_im_Newsletter.Newsletter_ID")));
	}

}
