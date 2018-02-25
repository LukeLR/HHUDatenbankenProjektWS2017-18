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
			//Check address and insert if necessary.
			int addressID = AddressIDHelper.getAddressIDWithChangedAddress((String) data.get("Adresse.Straße"),
					(String) data.get("Adresse.Hausnummer"), Integer.valueOf((String) data.get("Adresse.PLZ")),
					(String) data.get("Adresse.Ort"), (String) data1.get("Adresse.Straße"),
					(String) data1.get("Adresse.Hausnummer"), Integer.valueOf((String) data1.get("Adresse.PLZ")),
					(String) data1.get("Adresse.Ort"), con);

			/* Check if E-Mail-Address has changed. Re-Insert and delete customer if it did, to ensure
			 * foreign key constraints on occurrence of that customer in other tables during the update.
			 */
			PreparedStatement updateKundeStatement;
			if (!((String) data.get("Kunde.E-Mail-Adresse")).equals((String) data1.get("Kunde.E-Mail-Adresse"))) {
				logger.info("E-Mail-Address changed!");
				updateKundeStatement = con.prepareStatement(
						"INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)"
								+ "VALUES (?, ?, ?, ?, ?)");
				updateKundeStatement.setString(1, (String) data1.get("Kunde.E-Mail-Adresse"));
				updateKundeStatement.setString(2, (String) data1.get("Kunde.Vorname"));
				updateKundeStatement.setString(3, (String) data1.get("Kunde.Nachname"));
				updateKundeStatement.setString(4, (String) data1.get("Kunde.Passwort"));
				updateKundeStatement.setInt(5, addressID);
				updateKundeStatement.executeUpdate();

				String eMailOld = (String) data.get("Kunde.E-Mail-Adresse");
				String eMailNew = (String) data1.get("Kunde.E-Mail-Adresse");

				for (String tablename:DatabaseInfo.TABLES_WITH_E_MAIL_ADDRESS_WITHOUT_KUNDE) {
					AccountDataHelper.updateEMailAddressInTable(tablename, eMailOld, eMailNew, con);
				}

				PreparedStatement removeOldKundeStatement = con.prepareStatement(
						"DELETE FROM Kunde WHERE E_Mail_Adresse = ?");
				removeOldKundeStatement.setString(1, eMailOld);
				removeOldKundeStatement.executeUpdate();
				Project.getInstance().getData().replace("email", (String) data1.get("Kunde.E-Mail-Adresse"));
			} else {
				logger.info("E-Mail-Address unchanged!");
				updateKundeStatement = con.prepareStatement(
						"UPDATE Kunde SET Passwort = ?, Vorname = ?, Nachname = ?, Adressen_ID = ? "
								+ "WHERE E_Mail_Adresse = ?");
				updateKundeStatement.setString(1, (String) data1.get("Kunde.Passwort"));
				updateKundeStatement.setString(2, (String) data1.get("Kunde.Vorname"));
				updateKundeStatement.setString(3, (String) data1.get("Kunde.Nachname"));
				updateKundeStatement.setInt(4, addressID);
				updateKundeStatement.setString(5, (String) data.get("Kunde.E-Mail-Adresse"));
				updateKundeStatement.executeUpdate();
			}

			con.getRawConnection().commit();
			con.getRawConnection().setAutoCommit(true);
		} catch (Exception ex) {
			con.getRawConnection().rollback();
			con.getRawConnection().setAutoCommit(true);
			throw ex;
		}

		logger.info("Done changing account data for account " + (String) data1.get("Kunde.E-Mail-Adresse") + ".");
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
