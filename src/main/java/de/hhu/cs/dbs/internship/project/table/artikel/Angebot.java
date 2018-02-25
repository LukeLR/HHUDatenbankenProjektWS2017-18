package de.hhu.cs.dbs.internship.project.table.artikel;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

public class Angebot extends Table {

	public Angebot() {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Showing " + this.getClass().getName());
		
		/*String selectQuery = "SELECT (Angebots_ID, Artikel_ID, Preis) FROM Angebot";
		if (filter != null && !filter.isEmpty()) {
			logger.info("Searching for " + filter + " in " + this.getClass().getName() + " table");
			selectQuery += " WHERE Anbieterbezeichnung LIKE '%" + filter + "%'";
		}
		return selectQuery;*/
	}

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub
		return null;
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
