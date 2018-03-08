package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Warenkoerbe extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Warenkorb.Warenkorb_ID, "
				+ "Warenkorb.E_Mail_Adresse, "
				+ "Warenkorb.Bestelldatum, "
				+ "Warenkorb.Bestellstatus, "
				+ "Warenkorb.Lieferdienst_Bezeichnung, "
				+ "Warenkorb.Lieferdatum "
				+ "FROM Warenkorb";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Warenkorb.E_Mail_Adresse LIKE '%" + filter + "%' OR "
					+ "Warenkorb.Warenkorb_ID LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Warenkorb.Warenkorb_ID "
				+ "Warenkorb.E_Mail_Adresse, "
				+ "Warenkorb.Bestelldatum, "
				+ "Warenkorb.Bestellstatus, "
				+ "Warenkorb.Lieferdienst_Bezeichnung, "
				+ "Warenkorb.Lieferdatum "
				+ "FROM Warenkorb "
				+ "WHERE Warenkorb.Warenkorb_ID = '" + String.valueOf(data.get("Warenkorb.Warenkorb_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, "
				+ "E_Mail_Adresse, Lieferdienst_Bezeichnung, Lieferdatum) "
				+ "VALUES (?, ?, NULL, ?, ?, ?)");
		insertWarenkoerbeStatement.setString(1, String.valueOf(data.get("Warenkorb.Bestelldatum")));
		insertWarenkoerbeStatement.setString(2, String.valueOf(data.get("Warenkorb.Bestellstatus")));
		insertWarenkoerbeStatement.setString(3, String.valueOf(data.get("Warenkorb.E_Mail_Adresse")));
		insertWarenkoerbeStatement.setString(4, String.valueOf(data.get("Warenkorb.Lieferdienst_Bezeichnung")));
		insertWarenkoerbeStatement.setString(5, String.valueOf(data.get("Warenkorb.Lieferdatum")));
		insertWarenkoerbeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Warenkorb.E_Mail_Adresse")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Warenkoerbe SET "
				+ "Bestelldatum = ?, Bestellstatus = ?, E_Mail_Adresse = ?, "
				+ "Lieferdienst_Bezeichnung = ?, Lieferdatum = ? "
				+ "WHERE Warenkorb_ID = ?");
		updateWarenkoerbeStatement.setString(1, String.valueOf(newData.get("Warenkorb.Bestelldatum")));
		updateWarenkoerbeStatement.setString(2, String.valueOf(newData.get("Warenkorb.Bestellstatus")));
		updateWarenkoerbeStatement.setString(3, String.valueOf(newData.get("Warenkorb.E_Mail_Adresse")));
		updateWarenkoerbeStatement.setString(4, String.valueOf(newData.get("Warenkorb.Lieferdienst_Bezeichnung")));
		updateWarenkoerbeStatement.setString(5, String.valueOf(newData.get("Warenkorb.Lieferdatum")));
		updateWarenkoerbeStatement.setString(6, String.valueOf(oldData.get("Warenkorb.Warenkorb_ID")));
		updateWarenkoerbeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(oldData.get("Warenkorb.Warenkorb_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement insertWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Warenkoerbe WHERE Warenkorb_ID = ?");
		insertWarenkoerbeStatement.setString(1, String.valueOf(data.get("Warenkorb.Warenkorb_ID")));
		insertWarenkoerbeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Warenkorb.Warenkorb_ID")));
	}

}
