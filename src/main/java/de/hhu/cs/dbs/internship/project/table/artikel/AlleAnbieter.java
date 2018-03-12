package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleAnbieter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Anbieterbezeichnung FROM Anbieter";
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Anbieterbezeichnung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT * FROM Anbieter "
				+ "WHERE Anbieterbezeichnung = '" +	String.valueOf(data.get("Anbieter.Anbieterbezeichnung")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Anbieter (Anbieterbezeichnung) VALUES (?);");
		insertAnbieterStatement.setString(1, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
		insertAnbieterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Anbieter SET Anbieterbezeichnung = ? WHERE Anbieterbezeichnung = ?");
		updateAnbieterStatement.setString(1, String.valueOf(newData.get("Anbieter.Anbieterbezeichnung")));
		updateAnbieterStatement.setString(2, String.valueOf(oldData.get("Anbieter.Anbieterbezeichnung")));
		updateAnbieterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Anbieter.Anbieterbezeichnung")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Anbieter WHERE Anbieterbezeichnung = ?");
		deleteAnbieterStatement.setString(1, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
		deleteAnbieterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
	}

}
