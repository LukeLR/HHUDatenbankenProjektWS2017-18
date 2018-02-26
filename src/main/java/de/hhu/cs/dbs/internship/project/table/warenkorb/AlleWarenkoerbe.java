package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

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
		throw new SQLException("Das Anlegen von Warenkörben ist in der 'Alle Warenkörbe'-Ansicht "
				+ "nicht vorgesehen! Jeder Kunde kann seine eigenen Warenkörbe in der Ansicht "
				+ "'Warenkörbe' bearbeiten.");
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
