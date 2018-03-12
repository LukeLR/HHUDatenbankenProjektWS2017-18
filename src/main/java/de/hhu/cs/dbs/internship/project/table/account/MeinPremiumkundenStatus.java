package de.hhu.cs.dbs.internship.project.table.account;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class MeinPremiumkundenStatus extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		/* 
		 * Ansehen schon ab "READ_ONLY" erlaubt, da auch ehemalige Premiumkunden ihren abgelaufenen
		 * Status sehen können sollen.
		 */
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT * FROM (SELECT Ablaufdatum, Studierendenausweis, Kunde.E_Mail_Adresse, "
				+ "Vorname, Nachname, Passwort, Adressen_ID AS 'AID' FROM Premiumkunde "
				+ "JOIN Kunde ON Premiumkunde.E_Mail_Adresse = Kunde.E_Mail_Adresse) "
				+ "JOIN Adresse ON AID = Adresse.Adressen_ID "
				+ "WHERE Kunde.E_Mail_Adresse = '"
				+ String.valueOf(Project.getInstance().getData().get("email")) + "'";
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.READ_ONLY, this.getClass().getName());
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT * FROM Premiumkunde "
				+ "WHERE E_Mail_Adresse = '" + String.valueOf(data.get("Kunde.E_Mail_Adresse")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		Logger logger = Logger.getLogger(this.getClass().getName());
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		if (data.get("Premiumkunde.Studierendenausweis") != null) {
			logger.info("Inserting Non-Null Studierendenausweis into Premiumkunde!");
			PreparedStatement insertPremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse) "
					+ "VALUES (?, ?, ?)");
			insertPremiumkundeStatement.setString(1, String.valueOf(data.get("Premiumkunde.Ablaufdatum")));
			insertPremiumkundeStatement.setObject(2, data.get("Premiumkunde.Studierendenausweis"));
			insertPremiumkundeStatement.setString(3, String.valueOf(data.get("Premiumkunde.E_Mail_Adresse")));
			insertPremiumkundeStatement.executeUpdate();
		} else {
			logger.info("Premiumkunde.Studierendenausweis is NULL on insert.");
			PreparedStatement insertPremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse) "
					+ "VALUES (?, NULL, ?)");
			insertPremiumkundeStatement.setString(1, String.valueOf(data.get("Premiumkunde.Ablaufdatum")));
			insertPremiumkundeStatement.setString(2, String.valueOf(data.get("Premiumkunde.E_Mail_Adresse")));
			insertPremiumkundeStatement.executeUpdate();
		}
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data, String.valueOf(data.get("Premiumkunde.E_Mail_Adresse")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		Logger logger = Logger.getLogger(this.getClass().getName());
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		if (newData.get("Premiumkunde.Studierendenausweis") != null) {
			logger.info("Updating Non-Null Studierendenausweis in Premiumkunde!");
			PreparedStatement updatePremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Premiumkunde "
				+ "SET Ablaufdatum = ?, Studierendenausweis = ?, E_Mail_Adresse = ? "
				+ "WHERE E_Mail_Adresse = ?");
			updatePremiumkundeStatement.setString(1, String.valueOf(newData.get("Premiumkunde.Ablaufdatum")));
			updatePremiumkundeStatement.setObject(2, newData.get("Premiumkunde.Studierendenausweis"));
			updatePremiumkundeStatement.setString(3, String.valueOf(newData.get("Premiumkunde.E_Mail_Adresse")));
			updatePremiumkundeStatement.setString(4, String.valueOf(oldData.get("Premiumkunde.E_Mail_Adresse")));
			updatePremiumkundeStatement.executeUpdate();
		} else {
			logger.info("Premiumkunde.Studierendenausweis is NULL on update.");
			PreparedStatement updatePremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
					"UPDATE Premiumkunde "
					+ "SET Ablaufdatum = ?, Studierendenausweis = NULL, E_Mail_Adresse = ? "
					+ "WHERE E_Mail_Adresse = ?");
			updatePremiumkundeStatement.setString(1, String.valueOf(newData.get("Premiumkunde.Ablaufdatum")));
			updatePremiumkundeStatement.setString(2, String.valueOf(newData.get("Premiumkunde.E_Mail_Adresse")));
			updatePremiumkundeStatement.setString(3, String.valueOf(oldData.get("Premiumkunde.E_Mail_Adresse")));
			updatePremiumkundeStatement.executeUpdate();
		}
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Premiumkunde.E_Mail_Adresse")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		Permission.hasSufficientPermission(Permission.SHOP_ASSISTANT, this.getClass().getName());
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deletePremiumkundeStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Premiumkunde WHERE E_Mail_Adresse = ?");
		deletePremiumkundeStatement.setString(1, String.valueOf(data.get("Kunde.E_Mail_Adresse")));
		deletePremiumkundeStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data, String.valueOf(data.get("Kunde.E_Mail_Adresse")));
	}

}
