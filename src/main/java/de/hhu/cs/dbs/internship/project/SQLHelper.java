package de.hhu.cs.dbs.internship.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Connection;

public class SQLHelper {
	public static int getAddressIDByAddress
		(String street, String houseNumber, String zipCode, String city, Connection con) throws SQLException
	{
		PreparedStatement addressRequestQuery = con.prepareStatement(
				"SELECT Adressen_ID FROM Adresse "
				+ "WHERE Strasse = ? AND Hausnummer = ? AND PLZ = ? AND Ort )= ?");
		
		addressRequestQuery.setString(1, street.toString());
		addressRequestQuery.setString(2, houseNumber.toString());
		addressRequestQuery.setString(3, zipCode.toString());
		addressRequestQuery.setString(4, city.toString());
		
		ResultSet addressResults = addressRequestQuery.executeQuery();
		return addressResults.getInt("Adressen_ID");
	}
	
	public static int getAddressIDByAddress
	(String street, String houseNumber, String zipCode, String city) throws SQLException
	{
		return getAddressIDByAddress(street, houseNumber, zipCode, city,
				Project.getInstance().getConnection());
	}
}
