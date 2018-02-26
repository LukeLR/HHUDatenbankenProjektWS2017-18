package de.hhu.cs.dbs.internship.project.table.newsletter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Newsletterabo extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Newsletterabo.E_Mail_Adresse AS 'Abonnent', "
					+ "Newsletterabo.Newsletter_ID, "
					+ "Newsletter.Betreff, "
					+ "Newsletter.Text, "
					+ "Newsletter.Datum, "
					+ "Newsletter.E_Mail_Adresse AS 'Herausgeber' "
				+ "FROM Newsletterabo "
				+ "JOIN Newsletter "
				+ "ON Newsletterabo.Newsletter_ID = Newsletter.Newsletter_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Newsletterabo.E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT E_Mail_Adresse, Newsletter_ID "
				+ "FROM Newsletterabo "
				+ "WHERE E_Mail_Adresse = '"
				+ String.valueOf(data.get("Newsletterabo.E_Mail_Adresse"))
				+ "' AND Newsletter_ID = '"
				+ String.valueOf(data.get("Newsletterabo.Newsletter_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertNewsletteraboStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Newsletterabo (E_Mail_Adresse, Newsletter_ID) "
				+ "VALUES (?, ?)");
		insertNewsletteraboStatement.setString(1, String.valueOf(data.get("Newsletterabo.E_Mail_Adresse")));
		insertNewsletteraboStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Newsletter.Newsletter_ID"))));
		insertNewsletteraboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Newsletterabo.E_Mail_Adresse")) + "-" +
				String.valueOf(data.get("Newsletter.Newsletter_ID")));
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
