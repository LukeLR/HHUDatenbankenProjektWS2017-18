package de.hhu.cs.dbs.internship.project.helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.database.Data;

import de.hhu.cs.dbs.internship.project.DatabaseInfo;
import de.hhu.cs.dbs.internship.project.Project;

public class AccountDataHelper {

	public static void changeAccountData(Data data, Data data1) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Trying to change account data from " + data + " to " + data1 + ".");

		Connection con = Project.getInstance().getConnection();
		con.getRawConnection().setAutoCommit(false);

		try {
			//Check address and insert if neccessary.
			int addressID = AddressIDHelper.getAddressIDWithChangedAddress(data.get("Adresse.Straße").toString(),
					data.get("Adresse.Hausnummer").toString(), data.get("Adresse.PLZ").toString(),
					data.get("Adresse.Ort").toString(), data1.get("Adresse.Straße").toString(),
					data1.get("Adresse.Hausnummer").toString(), data1.get("Adresse.PLZ").toString(),
					data1.get("Adresse.Ort").toString(), con);

			/* Check if E-Mail-Address has changed. Re-Insert and delete customer if it did, to ensure
			 * foreign key constraints on occurrence of that customer in other tables during the update.
			 */
			PreparedStatement updateKundeStatement;
			if (!data.get("Kunde.E-Mail-Adresse").toString().equals(data1.get("Kunde.E-Mail-Adresse").toString())) {
				logger.info("E-Mail-Address changed!");
				updateKundeStatement = con.prepareStatement(
						"INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)"
								+ "VALUES (?, ?, ?, ?, ?)");
				updateKundeStatement.setString(1, data1.get("Kunde.E-Mail-Adresse").toString());
				updateKundeStatement.setString(2, data1.get("Kunde.Vorname").toString());
				updateKundeStatement.setString(3, data1.get("Kunde.Nachname").toString());
				updateKundeStatement.setString(4, data1.get("Kunde.Passwort").toString());
				updateKundeStatement.setInt(5, addressID);
				updateKundeStatement.executeUpdate();

				String eMailOld = data.get("Kunde.E-Mail-Adresse").toString();
				String eMailNew = data1.get("Kunde.E-Mail-Adresse").toString();

				for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS_WITHOUT_KUNDE) {
					AccountDataHelper.updateEMailAddressInTable(tablename, eMailOld, eMailNew, con);
				}

				PreparedStatement removeOldKundeStatement = con.prepareStatement(
						"DELETE FROM Kunde WHERE E_Mail_Adresse = ?");
				removeOldKundeStatement.setString(1, eMailOld);
				removeOldKundeStatement.executeUpdate();
				Project.getInstance().getData().replace("email", data1.get("Kunde.E-Mail-Adresse").toString());
			} else {
				logger.info("E-Mail-Address unchanged!");
				updateKundeStatement = con.prepareStatement(
						"UPDATE Kunde SET Passwort = ?, Vorname = ?, Nachname = ?, Adressen_ID = ? "
								+ "WHERE E_Mail_Adresse = ?");
				updateKundeStatement.setString(1, data1.get("Kunde.Passwort").toString());
				updateKundeStatement.setString(2, data1.get("Kunde.Vorname").toString());
				updateKundeStatement.setString(3, data1.get("Kunde.Nachname").toString());
				updateKundeStatement.setInt(4, addressID);
				updateKundeStatement.setString(5, data.get("Kunde.E-Mail-Adresse").toString());
				updateKundeStatement.executeUpdate();
			}

			con.getRawConnection().commit();
			con.getRawConnection().setAutoCommit(true);
		} catch (Exception ex) {
			con.getRawConnection().rollback();
			con.getRawConnection().setAutoCommit(true);
			throw ex;
		}

		logger.info("Done changing account data for account " + data1.get("Kunde.E-Mail-Adresse").toString() + ".");
	}

	public static void deleteAccountByEMail (String eMail) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Trying to delete account " + eMail + "...");

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

		logger.info("Deletion of account " + eMail + " done!");
	}

	public static void deleteAllEntriesWithEMailAddressInTable(String tablename, String eMail, Connection con) throws SQLException {
		Logger logger = Logger.getLogger(AccountDataHelper.class.getName());
		logger.info("Trying to delete all entries with E-Mail-Address " + eMail + " from table " + tablename + "...");

		PreparedStatement deleteEntriesStatement = con.prepareStatement(
				"DELETE FROM " + tablename + " WHERE E_Mail_Adresse = ?");
		deleteEntriesStatement.setString(1, eMail);
		deleteEntriesStatement.executeUpdate();

		logger.info("Deleting entries for E-Mail-Adress " + eMail + " in table " + tablename + " done!");
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

}
