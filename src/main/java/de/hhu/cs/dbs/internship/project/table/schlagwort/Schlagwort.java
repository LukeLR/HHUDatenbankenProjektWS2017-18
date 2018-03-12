package de.hhu.cs.dbs.internship.project.table.schlagwort;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Schlagwort extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Schlagwort FROM Schlagwort";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Schlagwort LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Schlagwort FROM Schlagwort "
				+ "WHERE Schlagwort = '"
				+ String.valueOf(data.get("Schlagwort.Schlagwort")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertSchlagwortStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Schlagwort (Schlagwort) VALUES (?)");
		insertSchlagwortStatement.setString(1, String.valueOf(data.get("Schlagwort.Schlagwort")));
		insertSchlagwortStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Schlagwort.Schlagwort")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateSchlagwortStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Schlagwort SET Schlagwort = ? WHERE Schlagwort = ?");
		updateSchlagwortStatement.setString(1, String.valueOf(newData.get("Schlagwort.Schlagwort")));
		updateSchlagwortStatement.setString(2, String.valueOf(oldData.get("Schlagwort.Schlagwort")));
		updateSchlagwortStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Schlagwort.Schlagwort")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteSchlagwortStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Schlagwort WHERE Schlagwort = ?");
		deleteSchlagwortStatement.setString(1, String.valueOf(data.get("Schlagwort.Schlagwort")));
		deleteSchlagwortStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Schlagwort.Schlagwort")));
	}

}
