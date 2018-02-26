package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.AccountDataHelper;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

import java.sql.SQLException;
import java.util.logging.Logger;

public class Account extends Table {
	@Override
	public String getSelectQueryForTableWithFilter(String s) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Vorname, Nachname, "
				+ "Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
				+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID WHERE E_Mail_Adresse = '" 
				+ Project.getInstance().getData().get("email") + "'";
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		String selectQuery = "SELECT E_Mail_Adresse AS 'E-Mail-Adresse', Passwort, Vorname,"
				+ "Nachname, Strasse AS 'Straße', Hausnummer, PLZ, Ort FROM Kunde JOIN Adresse "
				+ "ON Kunde.Adressen_ID = Adresse.Adressen_ID WHERE E_Mail_Adresse = '" 
				+ data.get("Kunde.E-Mail-Adresse") + "'";
		return selectQuery;
	}

	@Override
	//TODO: Enforce privileges
	public void insertRowWithData(Data data) throws SQLException {
		throw new SQLException("Es können keine weiteren Accounts für einen Kunden angelegt werden!");
	}

	@Override
	//TODO: Enforce privileges
	public void updateRowWithData(Data data, Data data1) throws SQLException {
		AccountDataHelper.changeAccountData(data, data1);
	}

	@Override
	//TODO: Enforce privileges
	public void deleteRowWithData(Data data) throws SQLException {
		AccountDataHelper.deleteAccountByEMail(String.valueOf(data.get("Kunde.E-Mail-Adresse")));
	}
}
