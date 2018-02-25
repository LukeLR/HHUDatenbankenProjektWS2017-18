package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

public class Anbieter extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Showing " + this.getClass().getName());
		
		String selectQuery = "SELECT Anbieterbezeichnung FROM Anbieter";
		if (filter != null && !filter.isEmpty()) {
			logger.info("Searching for " + filter + " in " + this.getClass().getName() + " table");
			selectQuery += " WHERE Anbieterbezeichnung LIKE '%" + filter + "%'";
		}
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to get Data for Dataset " + data.toString() + " in " + this.getClass().getName() + ".");
		
		String selectQuery = "SELECT * FROM Anbieter "
				+ "WHERE Anbieterbezeichnung = '" +
				(data.get("Anbieter.Anbieterbezeichnung") == null ? "null" :
					data.get("Anbieter.Anbieterbezeichnung").toString()) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to insert new Dataset with data: " + data.toString());
		
		PreparedStatement insertAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Anbieter (Anbieterbezeichnung) VALUES (?);");
		insertAnbieterStatement.setString(1, data.get("Anbieter.Anbieterbezeichnung").toString());
		insertAnbieterStatement.executeUpdate();
		
		logger.info("Dataset inserted into " + this.getClass().getName() + "!");
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to change Anbieter data from " + oldData + " to " + newData + ".");
		
		PreparedStatement updateAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Anbieter SET Anbieterbezeichnung = ? WHERE Anbieterbezeichnung = ?");
		updateAnbieterStatement.setString(1, newData.get("Anbieter.Anbieterbezeichnung").toString());
		updateAnbieterStatement.setString(2, oldData.get("Anbieter.Anbieterbezeichnung").toString());
		updateAnbieterStatement.executeUpdate();
		
		logger.info("Done changing account data for Anbieter " +
				newData.get("Anbieter.Anbieterbezeichnung").toString() + ".");
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to delete Dataset with data: " + data.toString());
		
		PreparedStatement deleteAnbieterStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Anbieter WHERE Anbieterbezeichnung = ?");
		deleteAnbieterStatement.setString(1, data.get("Anbieter.Anbieterbezeichnung").toString());
		deleteAnbieterStatement.executeUpdate();
		
		logger.info("Dataset for Anbieterbezeichnung " + data.get("Anbieter.Anbieterbezeichnung").toString()
				+ " deleted from " + this.getClass().getName() + ".");
	}

}
