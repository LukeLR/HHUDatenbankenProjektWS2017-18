package de.hhu.cs.dbs.internship.project.table.newsletter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Newsletterabos extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Newsletterabo.E_Mail_Adresse AS 'Abonnent', "
					+ "Newsletterabo.Newsletter_ID, "
					+ "Newsletter.Betreff, "
					+ "Newsletter.Text, "
					+ "Newsletter.Datum, "
					+ "Newsletter.E_Mail_Adresse AS 'Herausgeber' "
				+ "FROM Newsletterabo "
				+ "JOIN Newsletter "
				+ "ON Newsletterabo.Newsletter_ID = Newsletter.Newsletter_ID "
				+ "WHERE Newsletterabo.E_Mail_Adresse = '"
				+ String.valueOf(Project.getInstance().getData().get("email")) + "'";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " AND Newsletterabo.E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT E_Mail_Adresse, Newsletter_ID "
				+ "FROM Newsletterabo "
				+ "WHERE E_Mail_Adresse = '"
				+ String.valueOf(data.get("Newsletterabo.Abonnent"))
				+ "' AND Newsletter_ID = '"
				+ String.valueOf(data.get("Newsletterabo.Newsletter_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertNewsletteraboStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Newsletterabo (E_Mail_Adresse, Newsletter_ID) "
				+ "VALUES (?, ?)");
		insertNewsletteraboStatement.setString(1, String.valueOf(Project.getInstance().getData().get("email")));
		insertNewsletteraboStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Newsletterabo.Newsletter_ID"))));
		insertNewsletteraboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Newsletterabo.E_Mail_Adresse")) + "-" +
				String.valueOf(data.get("Newsletterabo.Newsletter_ID")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateNewsletteraboStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Newsletterabo "
				+ "SET Newsletter_ID = ? "
				+ "WHERE E_Mail_Adresse = ? AND "
					+ "Newsletter_ID = ?");
		updateNewsletteraboStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("Newsletterabo.Newsletter_ID"))));
		updateNewsletteraboStatement.setString(2, String.valueOf(Project.getInstance().getData().get("email")));
		updateNewsletteraboStatement.setInt(3, Integer.valueOf(String.valueOf(oldData.get("Newsletterabo.Newsletter_ID"))));
		updateNewsletteraboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Newsletterabo.E_Mail_Adresse")) + "-" +
				String.valueOf(newData.get("Newsletterabo.Newsletter_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteNewsletteraboStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Newsletterabo "
				+ "WHERE E_Mail_Adresse = ? AND "
					+ "Newsletter_ID = ?");
		deleteNewsletteraboStatement.setString(1, String.valueOf(Project.getInstance().getData().get("email")));
		deleteNewsletteraboStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Newsletterabo.Newsletter_ID"))));
		deleteNewsletteraboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data,
				String.valueOf(data.get("Newsletterabo.Abonnent")) + "-" +
				String.valueOf(data.get("Newsletterabo.Newsletter_ID")));
	}

}
