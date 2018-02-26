package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.helpers.AccountDataHelper;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleAccounts extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Passwort, Vorname, Nachname, "
				+ "Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
				+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID";
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " AND Kunde.E_Mail_Adresse LIKE '%" + filter + "%'";
		}
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Passwort, Vorname, Nachname, "
				+ "Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
				+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID WHERE E_Mail_Adresse = '"
				+ String.valueOf(data.get("Kunde.E-Mail-Adresse")) + "'";
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		throw new SQLException ("Das Anlegen eines neuen Nutzeraccounts ist nur über das Registrierungsformular vorgesehen!");
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		AccountDataHelper.changeAccountData(oldData, newData);
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		AccountDataHelper.deleteAccountByEMail(String.valueOf(data.get("Kunde.E-Mail-Adresse")));
	}

}
