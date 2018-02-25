package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

public class Artikel extends Table {
	
	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Showing " + this.getClass().getName());
		
		String selectQuery = "SELECT Bezeichnung, Beschreibung, Bild, Artikel_ID FROM Artikel";
		if (filter != null && !filter.isEmpty()) {
			logger.info("Searching for " + filter + " in " + this.getClass().getName() + " table");
			selectQuery += " WHERE Bezeichnung LIKE '%" + filter + "%' "
					+ "OR Beschreibung LIKE '%" + filter + "%'";
		}
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to get Data for Dataset " + data.toString() + " in " + this.getClass().getName() + ".");
		
		String selectQuery = "SELECT Bezeichnung, Beschreibung, Bild FROM Artikel "
				+ "WHERE Artikel_ID = '" +
				(data.get("Artikel.Artikel_ID") == null ? "null" : data.get("Artikel.Artikel_ID").toString()) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Trying to insert new Dataset with data: " + data.toString());
		
		PreparedStatement insertArtikelStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID) "
				+ "VALUES (?, ?, ?, NULL)");
		insertArtikelStatement.setString(1, (String) data.get("Artikel.Bezeichnung"));
		insertArtikelStatement.setString(2, (String) data.get("Artikel.Beschreibung"));
		insertArtikelStatement.setString(3, (String) data.get("Artikel.Bild"));
		insertArtikelStatement.executeUpdate();
		
		logger.info("Dataset inserted into " + this.getClass().getName() + "!");
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
