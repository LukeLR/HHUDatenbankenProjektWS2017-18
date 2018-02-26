package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Anbieter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
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
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT * FROM Anbieter "
				+ "WHERE Anbieterbezeichnung = '" +	String.valueOf(data.get("Anbieter.Anbieterbezeichnung")) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Anbieter (Anbieterbezeichnung) VALUES (?);");
		insertAnbieterStatement.setString(1, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
		insertAnbieterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
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
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		/*
		 * TODO: Auch hier gilt (Analog zum Angebot): Was ist mit Anbietern, die noch Angebote anbieten? Sollen diese
		 * Anbieter auch gelöscht werden können? Dann müssten auch alle Angebote gelöscht werden, die sie noch anbieten,
		 * und diese auch aus allen Warenkörben, etc. (Da sonst der FOREIGN KEY CONSTRAINT fehlschlägt.)
		 */
		PreparedStatement deleteAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Anbieter WHERE Anbieterbezeichnung = ?");
		deleteAnbieterStatement.setString(1, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
		deleteAnbieterStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Anbieter.Anbieterbezeichnung")));
	}

}
