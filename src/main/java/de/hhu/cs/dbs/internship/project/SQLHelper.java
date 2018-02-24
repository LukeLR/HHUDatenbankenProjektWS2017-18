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
	
	public static int getAddressIDWithChangedAddress
	(String streetOld, String houseNumberOld, String zipCodeOld, String cityOld,
	String streetNew, String houseNumberNew, String zipCodeNew, String cityNew, Connection con) throws SQLException
	{
		//Check if address has changed
    	if (!streetOld.equals(streetNew) || !houseNumberOld.equals(houseNumberNew) || !zipCodeOld.equals(zipCodeNew)
    		|| !cityOld.equals(cityNew))
    	{
    		//If address has changed: insert new address into table.
    		//TODO: Maybe delete old address?
    		PreparedStatement insertAddressStatement = con.prepareStatement(
    				"INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID) "
    				+ "VALUES (?, ?, ?, ?, NULL)");
    		insertAddressStatement.setString(1, streetNew);
    		insertAddressStatement.setString(2, houseNumberNew);
    		insertAddressStatement.setString(3, zipCodeNew);
    		insertAddressStatement.setString(4, cityNew);
    		
    		insertAddressStatement.executeUpdate();
    	}
    	return getAddressIDByAddress (streetNew, houseNumberNew, zipCodeNew, cityNew);
	}
	
	public static int getAddressIDWithChangedAddress
	(String streetOld, String houseNumberOld, String zipCodeOld, String cityOld,
	String streetNew, String houseNumberNew, String zipCodeNew, String cityNew) throws SQLException
	{
		return getAddressIDWithChangedAddress (streetOld, houseNumberOld, zipCodeOld, cityOld,
				streetNew, houseNumberNew, zipCodeNew, cityNew, Project.getInstance().getConnection());
	}
}
