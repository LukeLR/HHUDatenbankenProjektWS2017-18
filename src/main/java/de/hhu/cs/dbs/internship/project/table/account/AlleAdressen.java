package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleAdressen extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
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
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Strasse, Hausnummer, PLZ, Ort, Adressen_ID "
				+ "FROM Adresse "
				+ "WHERE Adressen_ID = '" + String.valueOf(data.get("Adresse.Adressen_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
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
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateAdresseStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Adresse "
				+ "SET Strasse = ?, Hausnummer = ?, PLZ = ?, Ort = ? "
				+ "WHERE Adressen_ID = ?");
		updateAdresseStatement.setString(1, String.valueOf(newData.get("Adresse.Strasse")));
		updateAdresseStatement.setString(2, String.valueOf(newData.get("Adresse.Hausnummer")));
		updateAdresseStatement.setInt(3, Integer.valueOf(String.valueOf(newData.get("Adresse.PLZ"))));
		updateAdresseStatement.setString(4, String.valueOf(newData.get("Adresse.Ort")));
		updateAdresseStatement.setInt(5, Integer.valueOf(String.valueOf(oldData.get("Adresse.Adressen_ID"))));
		updateAdresseStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Adresse.Strasse")) + " "
				+ String.valueOf(newData.get("Adresse.Hausnummer")) + ", "
				+ String.valueOf(newData.get("Adresse.PLZ")) + " "
				+ String.valueOf(newData.get("Adresse.Ort")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		// Deletion fails if Customers with this address still exist.
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteAdresseStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Adresse WHERE Adressen_ID = ?");
		deleteAdresseStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Adresse.Adressen_ID"))));
		deleteAdresseStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data,
				String.valueOf(data.get("Adresse.Strasse")) + " "
				+ String.valueOf(data.get("Adresse.Hausnummer")) + ", "
				+ String.valueOf(data.get("Adresse.PLZ")) + " "
				+ String.valueOf(data.get("Adresse.Ort")));
	}

}
