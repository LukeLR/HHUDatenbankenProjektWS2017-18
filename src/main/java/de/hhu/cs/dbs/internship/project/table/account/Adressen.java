package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Adressen extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Strasse, Hausnummer, PLZ, Ort, Adressen_ID "
				+ "FROM Adresse";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Strasse LIKE '%" + filter + "%' OR "
					+ "Hausnummer LIKE '%" + filter + "%' OR "
					+ "PLZ LIKE '%" + filter + "%' OR "
					+ "ORT LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Strasse, Hausnummer, PLZ, Ort, Adressen_ID "
				+ "FROM Adresse "
				+ "WHERE Adressen_ID = '" + String.valueOf(data.get("Adresse.Adressen_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAdresseStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID) "
				+ "VALUES (?, ?, ?, ?, NULL)");
		insertAdresseStatement.setString(1, String.valueOf(data.get("Adresse.Strasse")));
		insertAdresseStatement.setString(2, String.valueOf(data.get("Adresse.Hausnummer")));
		insertAdresseStatement.setInt(3, Integer.valueOf(String.valueOf(data.get("Adresse.PLZ"))));
		insertAdresseStatement.setString(4, String.valueOf(data.get("Adresse.Ort")));
		insertAdresseStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Adresse.Strasse")) + " "
				+ String.valueOf(data.get("Adresse.Hausnummer")) + ", "
				+ String.valueOf(data.get("Adresse.PLZ")) + " "
				+ String.valueOf(data.get("Adresse.Ort")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement insertAdresseStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Adresse "
				+ "SET Strasse = ?, Hausnummer = ?, PLZ = ?, Ort = ? "
				+ "WHERE Adressen_ID = ?");
		insertAdresseStatement.setString(1, String.valueOf(newData.get("Adresse.Strasse")));
		insertAdresseStatement.setString(2, String.valueOf(newData.get("Adresse.Hausnummer")));
		insertAdresseStatement.setInt(3, Integer.valueOf(String.valueOf(newData.get("Adresse.PLZ"))));
		insertAdresseStatement.setString(4, String.valueOf(newData.get("Adresse.Ort")));
		insertAdresseStatement.setInt(5, Integer.valueOf(String.valueOf(oldData.get("Adresse.Adressen_ID"))));
		insertAdresseStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Adresse.Strasse")) + " "
				+ String.valueOf(newData.get("Adresse.Hausnummer")) + ", "
				+ String.valueOf(newData.get("Adresse.PLZ")) + " "
				+ String.valueOf(newData.get("Adresse.Ort")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// Deletion fails if Customers with this address still exist.
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteAdresseStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Adresse WHERE Adressen_ID = ?");
		deleteAdresseStatement.setInt(5, Integer.valueOf(String.valueOf(data.get("Adresse.Adressen_ID"))));
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data,
				String.valueOf(data.get("Adresse.Strasse")) + " "
				+ String.valueOf(data.get("Adresse.Hausnummer")) + ", "
				+ String.valueOf(data.get("Adresse.PLZ")) + " "
				+ String.valueOf(data.get("Adresse.Ort")));
	}

}
