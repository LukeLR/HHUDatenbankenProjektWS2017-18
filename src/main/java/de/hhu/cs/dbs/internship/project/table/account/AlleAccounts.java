package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

public class AlleAccounts extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Vorname, Nachname, "
    			+ "Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
    			+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID";
		if (filter != null && !filter.isEmpty()) {
			selectQuery += " AND Kunde.E_Mail_Adresse LIKE '%" + filter + "'";
		}
        return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Vorname, Nachname, "
    			+ "Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
    			+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID WHERE E_Mail_Adresse = '"
    			+ data.get("Kunde.E-Mail-Adresse") + "'";
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
