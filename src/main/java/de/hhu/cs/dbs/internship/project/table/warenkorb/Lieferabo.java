package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class Lieferabo extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
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
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Lieferabo.Warenkorb_ID, "
				+ "Lieferabo.Intervall, "
				+ "Lieferabo.Beginn, "
				+ "Lieferabo.Ende "
				+ "FROM Lieferabo "
				+ "WHERE Lieferabo.Warenkorb_ID = '" + String.valueOf(data.get("Lieferabo.Warenkorb_ID")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertLieferaboStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID) "
				+ "VALUES (?, ?, ?, ?)");
		insertLieferaboStatement.setString(1, String.valueOf(data.get("Lieferabo.Intervall")));
		insertLieferaboStatement.setString(2, String.valueOf(data.get("Lieferabo.Beginn")));
		insertLieferaboStatement.setString(3, String.valueOf(data.get("Lieferabo.Ende")));
		insertLieferaboStatement.setInt(4, Integer.valueOf(String.valueOf(data.get("Lieferabo.Warenkorb_ID"))));
		insertLieferaboStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Lieferabo.Warenkorb_ID")));
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
