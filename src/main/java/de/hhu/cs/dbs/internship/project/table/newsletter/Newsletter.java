package de.hhu.cs.dbs.internship.project.table.newsletter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Newsletter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Betreff, Text, Datum, Newsletter_ID, E_Mail_Adresse "
				+ "FROM Newsletter";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Betreff LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Betreff, Text, Newsletter_ID, E_Mail_Adresse "
				+ "FROM Newsletter "
				+ "WHERE Newsletter_ID = '"
				+ String.valueOf(data.get("Newsletter.Newsletter_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertNewsletterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Newsletter (Betreff, Text, Newsletter_ID, E_Mail_Adresse) "
				+ "VALUES (?, ?, NULL, ?)");
		insertNewsletterStatement.setString(1, String.valueOf(data.get("Newsletter.Betreff")));
		insertNewsletterStatement.setString(2, String.valueOf(data.get("Newsletter.Text")));
		insertNewsletterStatement.setString(3, String.valueOf(data.get("Newsletter.E_Mail_Adresse")));
		insertNewsletterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Newsletter.Betreff")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateNewsletterStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Newsletter "
				+ "SET Betreff = ?, "
					+ "Text = ?, "
					+ "E_Mail_Adresse = ? "
				+ "WHERE Newsletter_ID = ?");
		updateNewsletterStatement.setString(1, String.valueOf(newData.get("Newsletter.Betreff")));
		updateNewsletterStatement.setString(2, String.valueOf(newData.get("Newsletter.Text")));
		updateNewsletterStatement.setString(3, String.valueOf(newData.get("Newsletter.E_Mail_Adresse")));
		updateNewsletterStatement.setInt(4, Integer.valueOf(String.valueOf(oldData.get("Newsletter.Newsletter_ID"))));
		updateNewsletterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Newsletter.Betreff")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteNewsletterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Newsletter "
				+ "WHERE Newsletter_ID = ?");
		deleteNewsletterStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Newsletter.Newsletter_ID"))));
		deleteNewsletterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Newsletter.Newsletter_ID")));
	}

}
