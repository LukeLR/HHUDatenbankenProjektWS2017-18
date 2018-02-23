package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Account extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
    	PreparedStatement selectQuery = Project.getInstance().getConnection().prepareStatement(
    			"SELECT * FROM Kunde WHERE E_Mail_Adresse = ?");
    	selectQuery.setString(1, Project.getInstance().getData().get("email").toString());
    	Logger logger = Logger.getLogger(this.getClass().getName());
    	logger.info("Query: " + selectQuery.toString());
        return selectQuery.toString();
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        throw new SQLException(getClass().getName() + ".getSelectQueryForRowWithData(Data) nicht implementiert.");
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        throw new SQLException(getClass().getName() + ".insertRowWithData(Data) nicht implementiert.");
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        throw new SQLException(getClass().getName() + ".updateRowWithData(Data, Data) nicht implementiert.");
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException(getClass().getName() + ".deleteRowWithData(Data) nicht implementiert.");
    }
}
