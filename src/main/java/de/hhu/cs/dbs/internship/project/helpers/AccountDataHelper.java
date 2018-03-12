package de.hhu.cs.dbs.internship.project.helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;

import de.hhu.cs.dbs.internship.project.DatabaseInfo;
import de.hhu.cs.dbs.internship.project.Project;

public class AccountDataHelper {

	public static void changeAccountData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate("Account", oldData, newData);
		
		Connection con = Project.getInstance().getConnection();
		con.getRawConnection().setAutoCommit(false);

		try {
			//Check address and insert if necessary.
			int addressID = AddressIDHelper.getAddressIDWithChangedAddress(String.valueOf(oldData.get("Adresse.Straße")),
					String.valueOf(oldData.get("Adresse.Hausnummer")), Integer.valueOf(String.valueOf(oldData.get("Adresse.PLZ"))),
					String.valueOf(oldData.get("Adresse.Ort")), String.valueOf(newData.get("Adresse.Straße")),
					String.valueOf(newData.get("Adresse.Hausnummer")), Integer.valueOf(String.valueOf(newData.get("Adresse.PLZ"))),
					String.valueOf(newData.get("Adresse.Ort")), con);
			
			PreparedStatement updateKundeStatement = con.prepareStatement(
					"UPDATE Kunde SET E_Mail_Adresse = ?, Passwort = ?, Vorname = ?, Nachname = ?, Adressen_ID = ? "
							+ "WHERE E_Mail_Adresse = ?");
			updateKundeStatement.setString(1, String.valueOf(newData.get("Kunde.E-Mail-Adresse")));
			updateKundeStatement.setString(2, String.valueOf(newData.get("Kunde.Passwort")));
			updateKundeStatement.setString(3, String.valueOf(newData.get("Kunde.Vorname")));
			updateKundeStatement.setString(4, String.valueOf(newData.get("Kunde.Nachname")));
			updateKundeStatement.setInt(5, addressID);
			updateKundeStatement.setString(6, String.valueOf(oldData.get("Kunde.E-Mail-Adresse")));
			updateKundeStatement.executeUpdate();

			con.getRawConnection().commit();
			con.getRawConnection().setAutoCommit(true);
		} catch (Exception ex) {
			con.getRawConnection().rollback();
			con.getRawConnection().setAutoCommit(true);
			throw ex;
		}

		UnifiedLoggingHelper.logUpdateDone("Account", oldData, newData, String.valueOf(newData.get("Kunde.E-Mail-Adresse")));
	}

	public static void deleteAccountByEMail (String eMail) throws SQLException {
		UnifiedLoggingHelper.logDelete("Account", eMail);

		Connection con = Project.getInstance().getConnection();
		con.getRawConnection().setAutoCommit(false);

		try {
			for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS) {
				AccountDataHelper.deleteAllEntriesWithEMailAddressInTable(tablename, eMail, con);
			}
			con.getRawConnection().commit();
			con.getRawConnection().setAutoCommit(true);
		} catch (Exception ex) {
			con.getRawConnection().rollback();
			con.getRawConnection().setAutoCommit(true);
			throw ex;
		}

		UnifiedLoggingHelper.logDeleteDone("Account", eMail);
	}

	public static void deleteAllEntriesWithEMailAddressInTable(String tablename, String eMail, Connection con) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Trying to delete all entries with E-Mail-Address " + eMail + " from table " + tablename + "...");

		PreparedStatement deleteEntriesStatement = con.prepareStatement(
				"DELETE FROM " + tablename + " WHERE E_Mail_Adresse = ?");
		deleteEntriesStatement.setString(1, eMail);
		deleteEntriesStatement.executeUpdate();

		logger.info("Deleting entries for E-Mail-Adresse " + eMail + " in table " + tablename + " done!");
	}

	public static void updateEMailAddressInTable(String tablename, String eMailOld, String eMailNew, Connection con) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Trying to change E-Mail-Addresses from " + eMailOld + " to " + eMailNew + " in " + tablename + ".");

		PreparedStatement updateEMailStatement = con.prepareStatement(
				"UPDATE " + tablename + " SET E_Mail_Adresse = ? WHERE E_Mail_Adresse = ?");
		updateEMailStatement.setString(1, eMailNew);
		updateEMailStatement.setString(2, eMailOld);
		updateEMailStatement.executeUpdate();

		logger.info("Changing E-Mail-Addresses done!");
	}
	
	public static ResultSet getAllWarenkoerbeByEMailAddress(String eMail) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Searching for all Warenkoerbe by user " + eMail + "!");
		
		PreparedStatement getWarenkoerbeStatement = Project.getInstance().getConnection().prepareStatement(
				"SELECT Warenkorb_ID FROM Warenkorb "
				+ "WHERE E_Mail_Adresse = '" + eMail + "'");
		ResultSet getWarenkoerbeResults = getWarenkoerbeStatement.executeQuery();
		
		return getWarenkoerbeResults;
	}
	
	public static ResultSet getAllWarenkoerbeForCurrentUser() throws SQLException {
		return getAllWarenkoerbeByEMailAddress(String.valueOf(Project.getInstance().getData().get("email")));
	}
	
	public static boolean userWithEMailAddressHasWarenkorbWithID(String eMail, int warenkorbID) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Checking if user " + eMail + " has Warenkorb with ID " + String.valueOf(warenkorbID) + ".");
		ResultSet getWarenkoerbeResults = getAllWarenkoerbeByEMailAddress(eMail);
		if (!getWarenkoerbeResults.isClosed()) {
			while (!getWarenkoerbeResults.isLast()) {
				if (warenkorbID == getWarenkoerbeResults.getInt("Warenkorb.Warebkorb_ID"))
					logger.info("User " + eMail + " has Warenkorb with ID " + String.valueOf(warenkorbID));
					return true;
			}
			logger.info("Reached last Warenkorb for user with E_Mail_Adresse " + eMail);
		} else {
			SQLException ex = new SQLException ("Results for Warenkoerbe by E_Mail_Adresse " + eMail + " empty!");
			logger.log(Level.WARNING, "Results for Warenkoerbe by E_Mail_Adresse " + eMail + " empty!", ex);
			throw ex;
		}
		return false;
	}
	
	public static boolean currentUserHasWarenkorbWithID(int warenkorbID) throws SQLException {
		return userWithEMailAddressHasWarenkorbWithID(String.valueOf(Project.getInstance().getData().get("email")), warenkorbID);
	}
}
