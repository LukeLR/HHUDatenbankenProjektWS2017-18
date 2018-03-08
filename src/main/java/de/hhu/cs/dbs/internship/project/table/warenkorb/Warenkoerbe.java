package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

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
		
		String selectQuery = "SELECT Warenkorb.Warenkorb_ID, "
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
