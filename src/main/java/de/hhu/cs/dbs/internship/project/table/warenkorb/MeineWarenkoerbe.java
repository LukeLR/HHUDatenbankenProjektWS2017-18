package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class MeineWarenkoerbe extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Warenkorb.Warenkorb_ID, "
				+ "Warenkorb.E_Mail_Adresse, "
				+ "Warenkorb.Bestelldatum, "
				+ "Warenkorb.Bestellstatus, "
				+ "Warenkorb.Lieferdienst_Bezeichnung, "
				+ "Warenkorb.Lieferdatum "
				+ "FROM Warenkorb "
				+ "WHERE Warenkorb.E_Mail_Adresse = '"
				+ String.valueOf(Project.getInstance().getData().get("email")) + "'";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " AND (Warenkorb.E_Mail_Adresse LIKE '%" + filter + "%' OR "
					+ "Warenkorb.Warenkorb_ID LIKE '%" + filter + "%')";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Warenkorb.Warenkorb_ID, "
				+ "Warenkorb.E_Mail_Adresse, "
				+ "Warenkorb.Bestelldatum, "
				+ "Warenkorb.Bestellstatus, "
				+ "Warenkorb.Lieferdienst_Bezeichnung, "
				+ "Warenkorb.Lieferdatum "
				+ "FROM Warenkorb "
				+ "WHERE Warenkorb.Warenkorb_ID = '"
				+ String.valueOf(data.get("Warenkorb.Warenkorb_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.CUSTOMER, this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, "
				+ "E_Mail_Adresse, Lieferdienst_Bezeichnung, Lieferdatum) "
				+ "VALUES (NULL, NULL, NULL, ?, NULL, NULL)"); // Create with default values
		insertWarenkoerbeStatement.setString(1, String.valueOf(Project.getInstance().getData().get("email")));
		insertWarenkoerbeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Warenkorb.E_Mail_Adresse")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.CUSTOMER, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Warenkorb SET "
				+ "Bestelldatum = ?, Bestellstatus = ?, E_Mail_Adresse = ?, "
				+ "Lieferdienst_Bezeichnung = ?, Lieferdatum = ? "
				+ "WHERE Warenkorb_ID = ? AND E_Mail_Adresse = ?");
		updateWarenkoerbeStatement.setString(1, String.valueOf(newData.get("Warenkorb.Bestelldatum")));
		updateWarenkoerbeStatement.setString(2, String.valueOf(newData.get("Warenkorb.Bestellstatus")));
		updateWarenkoerbeStatement.setString(3, String.valueOf(Project.getInstance().getData().get("email")));
		updateWarenkoerbeStatement.setString(4, String.valueOf(newData.get("Warenkorb.Lieferdienst_Bezeichnung")));
		updateWarenkoerbeStatement.setString(5, String.valueOf(newData.get("Warenkorb.Lieferdatum")));
		updateWarenkoerbeStatement.setString(6, String.valueOf(oldData.get("Warenkorb.Warenkorb_ID")));
		updateWarenkoerbeStatement.setString(7, String.valueOf(Project.getInstance().getData().get("email")));
		updateWarenkoerbeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(oldData.get("Warenkorb.Warenkorb_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.CUSTOMER, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement insertWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Warenkorb WHERE Warenkorb_ID = ?");
		insertWarenkoerbeStatement.setString(1, String.valueOf(data.get("Warenkorb.Warenkorb_ID")));
		insertWarenkoerbeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Warenkorb.Warenkorb_ID")));
	}

}
