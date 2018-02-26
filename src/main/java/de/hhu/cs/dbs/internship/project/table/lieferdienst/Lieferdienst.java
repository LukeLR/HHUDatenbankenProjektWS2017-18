package de.hhu.cs.dbs.internship.project.table.lieferdienst;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Lieferdienst extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Lieferdienst_Bezeichnung, Versandkosten "
				+ "FROM Lieferdienst";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Lieferdienst_Bezeichnung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Lieferdienst_Bezeichnung, Versandkosten "
				+ "FROM Lieferdienst";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertLieferdienstStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)"
				+ "VALUES (?, ?)");
		insertLieferdienstStatement.setString(1, String.valueOf(data.get("Lieferdienst.Lieferdienst_Bezeichnung")));
		insertLieferdienstStatement.setFloat(2, Float.valueOf(String.valueOf(data.get("Lieferdienst.Versandkosten"))));
		insertLieferdienstStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Lieferdienst.Lieferdienst_Bezeichnung")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateLieferdienstStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Lieferdienst "
				+ "SET Lieferdienst_Bezeichnung = ?, "
					+ "Versandkosten = ? "
				+ "WHERE Lieferdienstbezeichnung = ? AND "
					+ "Versandkosten = ?");
		updateLieferdienstStatement.setString(1, String.valueOf(newData.get("Lieferdienst.Lieferdienst_Bezeichnung")));
		updateLieferdienstStatement.setFloat(2, Float.valueOf(String.valueOf(newData.get("Lieferdienst.Versandkosten"))));
		updateLieferdienstStatement.setString(1, String.valueOf(oldData.get("Lieferdienst.Lieferdienst_Bezeichnung")));
		updateLieferdienstStatement.setFloat(2, Float.valueOf(String.valueOf(oldData.get("Lieferdienst.Versandkosten"))));
		updateLieferdienstStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Lieferdienst.Lieferdienst_Bezeichnung")));
		
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteLieferdienstStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Lieferdienst "
				+ "WHERE Lieferdienstbezeichnung = ?");
		deleteLieferdienstStatement.setString(1, String.valueOf(data.get("Lieferdienst.Lieferdienst_Bezeichnung")));
		deleteLieferdienstStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Lieferdienst.Lieferdienst_Bezeichnung")));
	}

}
