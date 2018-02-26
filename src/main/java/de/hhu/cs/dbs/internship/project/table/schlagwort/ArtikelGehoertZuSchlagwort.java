package de.hhu.cs.dbs.internship.project.table.schlagwort;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class ArtikelGehoertZuSchlagwort extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Artikel_ID, Schlagwort FROM Artikel_gehort_zu_Schlagwort";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Schlagwort = '" + filter + "'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Artikel_ID, Schlagwort FROM Artikel_gehort_zu_Schlagwort "
				+ "WHERE Artikel_ID = '"
				+ String.valueOf(data.get("Artikel_gehoert_zu_Schlagwort.Artikel_ID"))
				+ "' AND Schlagwort = '"
				+ String.valueOf(data.get("Artikel_gehoert_zu_Schlagwort.Schlagwort")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertArtikelSchlagwortStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Artikel_gehort_zu_Schlagwort (Artikel_ID, Schlagwort) "
				+ "VALUES (?, ?)");
		insertArtikelSchlagwortStatement.setInt(1, Integer.valueOf(String.valueOf("Artikel_gehoert_zu_Schlagwort.Artikel_ID")));
		insertArtikelSchlagwortStatement.setString(2, String.valueOf(data.get("Artikel_gehoert_zu_Schlagwort.Schlagwort")));
		insertArtikelSchlagwortStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf("Artikel_gehoert_zu_Schlagwort.Artikel_ID") + "-"
				+ String.valueOf(data.get("Artikel_gehoert_zu_Schlagwort.Schlagwort")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
