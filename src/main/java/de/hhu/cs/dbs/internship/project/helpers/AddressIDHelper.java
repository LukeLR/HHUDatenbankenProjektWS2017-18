package de.hhu.cs.dbs.internship.project.helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Connection;

import de.hhu.cs.dbs.internship.project.Project;
/**
 * Contains various helper functions for SQL statements.
 * @author lukas
 *
 */
public class AddressIDHelper {
	/**
	 * Searches the database for an address id by given address. If the address is not in the database, insert it.
	 * @param street The street of the address in question.
	 * @param houseNumber The housenumber of the address in question.
	 * @param zipCode The ZIP code of the address in question.
	 * @param city The city of the address in question.
	 * @param con The connection to use for searching. This is useful, if the search should include results for changes that have not been committed yet.
	 * @return The address id for the address found.
	 * @throws SQLException If the statement is malformed, or no address is found.
	 */
	public static int getAddressIDByAddress
	(String street, String houseNumber, int zipCode, String city, Connection con) throws SQLException
	{
		Logger logger = Logger.getLogger(AddressIDHelper.class.getName());
		logger.info("Searching for address ID for address: " + street
				+ " " + houseNumber + ", " + zipCode + " " + city);

		PreparedStatement addressRequestQuery = con.prepareStatement(
				"SELECT Adressen_ID FROM Adresse "
						+ "WHERE Strasse = ? AND Hausnummer = ? AND PLZ = ? AND Ort = ?");

		addressRequestQuery.setString(1, street);
		addressRequestQuery.setString(2, houseNumber);
		addressRequestQuery.setInt(3, zipCode);
		addressRequestQuery.setString(4, city);

		ResultSet addressResults = addressRequestQuery.executeQuery();
		if (!addressResults.isClosed()) {
			int addressID = addressResults.getInt("Adressen_ID");
			logger.info("Address ID found: " + String.valueOf(addressID));
			return addressID;
		} else {
			logger.info("Address not found! Inserting...");
			PreparedStatement addressInsertQuery = con.prepareStatement(
					"INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID) "
					+ "VALUES (?, ?, ?, ?, NULL)");
			addressInsertQuery.setString(1, street);
			addressInsertQuery.setString(2, houseNumber);
			addressInsertQuery.setInt(3, zipCode);
			addressInsertQuery.setString(4, city);
			addressInsertQuery.executeUpdate();
			// Get the Adressen_ID of the just inserted address.
			return getAddressIDByAddress(street, houseNumber, zipCode, city, con);
		}
	}
	/**
	 * Searches the database for an address id by given address. In this case, no database connection is given, so the default one is used.
	 * @param street The street of the address in question.
	 * @param houseNumber The housenumber of the address in question.
	 * @param zipCode The ZIP code of the address in question.
	 * @param city The city of the address in question.
	 * @return The address id for the address found.
	 * @throws SQLException If the statement is malformed, or no address is found.
	 */
	public static int getAddressIDByAddress
	(String street, String houseNumber, int zipCode, String city) throws SQLException
	{
		return getAddressIDByAddress(street, houseNumber, zipCode, city,
				Project.getInstance().getConnection());
	}

	/**
	 * Compares two addresses. If they have changed, insert the new one into the database. Gets the address ID of the current address, regardless if it has changed or not.
	 * @param streetOld The street of the old address.
	 * @param houseNumberOld The housenumber of the old address.
	 * @param zipCodeOld The zip code of the old address.
	 * @param cityOld The city of the old address.
	 * @param streetNew The street of the new address.
	 * @param houseNumberNew The housenumber of the new address.
	 * @param zipCodeNew The ZIP code of the new address.
	 * @param cityNew The city of the new address.
	 * @param con The connection to use for insertion or searching. This is useful if there are changes that have not been committed yet.
	 * @return The address ID of the new address, regardless if it has changed or not.
	 * @throws SQLException If the statement is malformed, or the address is unchanged but not in the database.
	 */
	public static int getAddressIDWithChangedAddress
	(String streetOld, String houseNumberOld, int zipCodeOld, String cityOld,
			String streetNew, String houseNumberNew, int zipCodeNew, String cityNew, Connection con) throws SQLException
	{
		Logger logger = Logger.getLogger(AddressIDHelper.class.getName());

		//Check if address has changed
		if (!streetOld.equals(streetNew) || !houseNumberOld.equals(houseNumberNew) || zipCodeOld != zipCodeNew
				|| !cityOld.equals(cityNew))
		{
			//Address has changed: insert new address into table.
			logger.info("Address has changed:\nstreet: " + streetOld + " -> " + streetNew
					+ "\nhouseNumber: " + houseNumberOld + " -> " + houseNumberNew
					+ "\nzipCode: " + zipCodeOld + " -> " + zipCodeNew
					+ "\n city: " + cityOld + " -> " + cityNew);
			
			PreparedStatement insertAddressStatement = con.prepareStatement(
					"INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID) "
							+ "VALUES (?, ?, ?, ?, NULL)");
			insertAddressStatement.setString(1, streetNew);
			insertAddressStatement.setString(2, houseNumberNew);
			insertAddressStatement.setInt(3, zipCodeNew);
			insertAddressStatement.setString(4, cityNew);

			insertAddressStatement.executeUpdate();
		} else {
			logger.info("Address unchanged:\nstreet: " + streetOld + " -> " + streetNew
					+ "\nhouseNumber: " + houseNumberOld + " -> " + houseNumberNew
					+ "\nzipCode: " + zipCodeOld + " -> " + zipCodeNew
					+ "\n city: " + cityOld + " -> " + cityNew);
			//TODO: Maybe insert the old address, if it is not in the database yet? Or provide an additional function for that?
		}
		return getAddressIDByAddress (streetNew, houseNumberNew, zipCodeNew, cityNew, con);
	}

	/**
	 * Compares two addresses. If they have changed, insert the new one into the database. Gets the address ID of the current address, regardless if it has changed or not. In this case, no database connection is given, so the default one is used.
	 * @param streetOld The street of the old address.
	 * @param houseNumberOld The housenumber of the old address.
	 * @param zipCodeOld The zip code of the old address.
	 * @param cityOld The city of the old address.
	 * @param streetNew The street of the new address.
	 * @param houseNumberNew The housenumber of the new address.
	 * @param zipCodeNew The ZIP code of the new address.
	 * @param cityNew The city of the new address.
	 * @return The address ID of the new address, regardless if it has changed or not.
	 * @throws SQLException If the statement is malformed, or the address is unchanged but not in the database.
	 */
	public static int getAddressIDWithChangedAddress
	(String streetOld, String houseNumberOld, int zipCodeOld, String cityOld,
			String streetNew, String houseNumberNew, int zipCodeNew, String cityNew) throws SQLException
	{
		return getAddressIDWithChangedAddress (streetOld, houseNumberOld, zipCodeOld, cityOld,
				streetNew, houseNumberNew, zipCodeNew, cityNew, Project.getInstance().getConnection());
	}
}
