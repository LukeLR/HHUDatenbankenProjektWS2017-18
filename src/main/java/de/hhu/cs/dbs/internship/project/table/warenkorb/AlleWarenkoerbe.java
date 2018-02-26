package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleWarenkoerbe extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse, " + 
				"Lieferdienst_Bezeichnung, Lieferdatum FROM Warenkorb";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse, " + 
				"Lieferdienst_Bezeichnung, Lieferdatum FROM Warenkorb "
				+ "WHERE Warenkorb_ID = '"
				+ String.valueOf(data.get("Warenkorb.Warenkorb_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateWarenkorbStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Warenkorb "
					+ "SET Bestelldatum = ?, "
					+ "Bestellstatus = ?, "
					+ "E_Mail_Adresse = ?, "
					+ "Lieferdienst_Bezeichnung = ?, "
					+ "Lieferdatum = ? "
				+ "WHERE Warenkorb_ID = ?");
		updateWarenkorbStatement.setString(1, String.valueOf(newData.get("Warenkorb.Bestelldatum")));
		updateWarenkorbStatement.setString(2, String.valueOf(newData.get("Warenkorb.Bestellstatus")));
		updateWarenkorbStatement.setString(3, String.valueOf(newData.get("Warenkorb.E_Mail_Adresse")));
		updateWarenkorbStatement.setString(4, String.valueOf(newData.get("Warenkorb.Lieferdienst_Bezeichnung")));
		updateWarenkorbStatement.setString(5, String.valueOf(newData.get("Warenkorb.Lieferdatum")));
		updateWarenkorbStatement.setInt(6, Integer.valueOf(String.valueOf(oldData.get("Warenkorb.Warenkorb_ID"))));
		updateWarenkorbStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Warenkorb.E_Mail_Adresse")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
