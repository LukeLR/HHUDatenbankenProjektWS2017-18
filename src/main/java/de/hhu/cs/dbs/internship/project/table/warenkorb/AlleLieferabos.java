package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleLieferabos extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Warenkorb.E_Mail_Adresse, "
				+ "Lieferabo.Warenkorb_ID, "
				+ "Lieferabo.Intervall, "
				+ "Lieferabo.Beginn, "
				+ "Lieferabo.Ende "
				+ "FROM Lieferabo "
				+ "JOIN Warenkorb on Lieferabo.Warenkorb_ID = Warenkorb.Warenkorb_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Warenkorb.E_Mail_Adresse LIKE '%" + filter + "%' OR"
					+ " Lieferabo.Warenkorb_ID LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Lieferabo.Warenkorb_ID, "
				+ "Lieferabo.Intervall, "
				+ "Lieferabo.Beginn, "
				+ "Lieferabo.Ende "
				+ "FROM Lieferabo "
				+ "WHERE Lieferabo.Warenkorb_ID = '"
				+ String.valueOf(data.get("Lieferabo.Warenkorb_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertLieferaboStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID) "
				+ "VALUES (?, ?, ?, ?)");
		insertLieferaboStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Lieferabo.Intervall"))));
		insertLieferaboStatement.setString(2, String.valueOf(data.get("Lieferabo.Beginn")));
		insertLieferaboStatement.setString(3, String.valueOf(data.get("Lieferabo.Ende")));
		insertLieferaboStatement.setInt(4, Integer.valueOf(String.valueOf(data.get("Lieferabo.Warenkorb_ID"))));
		insertLieferaboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Lieferabo.Warenkorb_ID")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateLieferaboStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Lieferabo SET Intervall = ?, Beginn = ?, Ende = ?, Warenkorb_ID = ? "
				+ "WHERE Intervall = ? AND Beginn = ? AND Ende = ? AND Warenkorb_ID = ?");
		updateLieferaboStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("Lieferabo.Intervall"))));
		updateLieferaboStatement.setString(2, String.valueOf(newData.get("Lieferabo.Beginn")));
		updateLieferaboStatement.setString(3, String.valueOf(newData.get("Lieferabo.Ende")));
		updateLieferaboStatement.setInt(4, Integer.valueOf(String.valueOf(newData.get("Lieferabo.Warenkorb_ID"))));
		updateLieferaboStatement.setInt(5, Integer.valueOf(String.valueOf(oldData.get("Lieferabo.Intervall"))));
		updateLieferaboStatement.setString(6, String.valueOf(oldData.get("Lieferabo.Beginn")));
		updateLieferaboStatement.setString(7, String.valueOf(oldData.get("Lieferabo.Ende")));
		updateLieferaboStatement.setInt(8, Integer.valueOf(String.valueOf(oldData.get("Lieferabo.Warenkorb_ID"))));
		updateLieferaboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData, String.valueOf(newData.get("Lieferabo.Warenkorb_ID")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteLieferaboStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Lieferabo "
				+ "WHERE Intervall = ? AND Beginn = ? AND Ende = ? AND Warenkorb_ID = ?");
		deleteLieferaboStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Lieferabo.Intervall"))));
		deleteLieferaboStatement.setString(2, String.valueOf(data.get("Lieferabo.Beginn")));
		deleteLieferaboStatement.setString(3, String.valueOf(data.get("Lieferabo.Ende")));
		deleteLieferaboStatement.setInt(4, Integer.valueOf(String.valueOf(data.get("Lieferabo.Warenkorb_ID"))));
		deleteLieferaboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Lieferabo.Warenkorb_ID")));
	}

}
