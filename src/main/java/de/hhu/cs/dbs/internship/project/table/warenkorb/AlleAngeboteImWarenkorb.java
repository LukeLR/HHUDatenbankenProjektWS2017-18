package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AlleAngeboteImWarenkorb extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Angebot_im_Warenkorb.Warenkorb_ID, "
//				+ "Warenkorb.Bestelldatum, "
//				+ "Warenkorb.Bestellstatus, "
				+ "Warenkorb.E_Mail_Adresse, "
//				+ "Warenkorb.Lieferdienst_Bezeichnung, "
//				+ "Warenkorb.Lieferdatum, "
				+ "Angebot_im_Warenkorb.Angebots_ID, "
				+ "Angebot.Artikel_ID, "
				+ "Angebot.Preis, "
				+ "Artikel.Bezeichnung, "
//				+ "Artikel.Beschreibung, "
				+ "Artikel.Bild, "
				+ "Angebot_im_Warenkorb.Anbieterbezeichnung, "
				+ "Angebot_im_Warenkorb.Anzahl "
				+ "FROM Angebot_im_Warenkorb "
				+ "JOIN Angebot on Angebot_im_Warenkorb.Angebots_ID = Angebot.Angebots_ID "
				+ "JOIN Artikel on Angebot.Artikel_ID = Artikel.Artikel_ID "
				+ "JOIN Warenkorb on Angebot_im_Warenkorb.Warenkorb_ID = Warenkorb.Warenkorb_ID";
		
		if (filter != null && !filter.isEmpty()) {
			UnifiedLoggingHelper.logFilter(this.getClass().getName(), filter);
			selectQuery += " WHERE Warenkorb.E_Mail_Adresse LIKE '%" + filter + "%' OR "
					+ "Artikel.Bezeichnung LIKE '%" + filter + "%'";
		}
		
		UnifiedLoggingHelper.logShowDone(this.getClass().getName(), selectQuery);
		return selectQuery;
	}

	@Override
	public String getSelectQueryForRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logSelect(this.getClass().getName(), data);
		
		String selectQuery = "SELECT Angebot_im_Warenkorb.Warenkorb_ID, "
				+ "Angebot_im_Warenkorb.Angebots_ID, "
				+ "Angebot_im_Warenkorb.Anbieterbezeichnung, "
				+ "Angebot_im_Warenkorb.Anzahl "
				+ "FROM Angebot_im_Warenkorb "
				+ "WHERE Angebot_im_Warenkorb.Warenkorb_ID = '" + String.valueOf(data.get("Angebot_im_Warenkorb.Warenkorb_ID")) + "' AND "
				+ "Angebot_im_Warenkorb.Angebots_ID = '" + String.valueOf(data.get("Angebot_im_Warenkorb.Angebots_ID")) + "' AND "
				+ "Angebot_im_Warenkorb.Anbieterbezeichnung = '" + String.valueOf(data.get("Angebot_im_Warenkorb.Anbieterbezeichnung")) + "' AND "
				+ "Angebot_im_Warenkorb.Anzahl = '" + String.valueOf(data.get("Angebot_im_Warenkorb.Anzahl")) + "'";
		
		UnifiedLoggingHelper.logSelectDone(this.getClass().getName(), data, selectQuery);
		return selectQuery;
	}
	
	@Override
	public void insertRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logInsert(this.getClass().getName(), data);
		
		PreparedStatement insertAngebotImWarenkorbStatement = Project.getInstance().getConnection().prepareStatement(
				"INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung, Warenkorb_ID, Anzahl) "
				+ "VALUES (?, ?, ?, ?)");
		insertAngebotImWarenkorbStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Angebots_ID"))));
		insertAngebotImWarenkorbStatement.setString(2, String.valueOf(data.get("Angebot_im_Warenkorb.Anbieterbezeichnung")));
		insertAngebotImWarenkorbStatement.setInt(3, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Warenkorb_ID"))));
		insertAngebotImWarenkorbStatement.setInt(4, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Anzahl"))));
		insertAngebotImWarenkorbStatement.executeUpdate();
		
		UnifiedLoggingHelper.logInsertDone(this.getClass().getName(), data,
				String.valueOf(data.get("Angebot_im_Warenkorb.Warenkorb_ID")) + "-"
				+ String.valueOf(data.get("Angebot_im_Warenkorb.Angebots_ID"))
				+ String.valueOf(data.get("Angebot_im_Warenkorb.Anbieterbezeichnung")) + "-"
				+ String.valueOf(data.get("Angebot_im_Warenkorb.Anzahl")));
	}

	@Override
	public void updateRowWithData(Data oldData, Data newData) throws SQLException {
		UnifiedLoggingHelper.logUpdate(this.getClass().getName(), oldData, newData);
		
		PreparedStatement updateAngebotImWarenkorbStatement = Project.getInstance().getConnection().prepareStatement(
				"UPDATE Angebot_im_Warenkorb "
				+ "SET Warenkorb_ID = ?, Angebots_ID = ?, Anbieterbezeichnung = ?, Anzahl = ? "
				+ "WHERE Warenkorb_ID = ? AND Angebots_ID = ? AND Anbieterbezeichnung = ? AND Anzahl = ?");
				
		updateAngebotImWarenkorbStatement.setInt(1, Integer.valueOf(String.valueOf(newData.get("Angebot_im_Warenkorb.Warenkorb_ID"))));
		updateAngebotImWarenkorbStatement.setInt(2, Integer.valueOf(String.valueOf(newData.get("Angebot_im_Warenkorb.Angebots_ID"))));
		updateAngebotImWarenkorbStatement.setString(3, String.valueOf(newData.get("Angebot_im_Warenkorb.Anbieterbezeichnung")));
		updateAngebotImWarenkorbStatement.setInt(4, Integer.valueOf(String.valueOf(newData.get("Angebot_im_Warenkorb.Anzahl"))));
		updateAngebotImWarenkorbStatement.setInt(5, Integer.valueOf(String.valueOf(oldData.get("Angebot_im_Warenkorb.Warenkorb_ID"))));
		updateAngebotImWarenkorbStatement.setInt(6, Integer.valueOf(String.valueOf(oldData.get("Angebot_im_Warenkorb.Angebots_ID"))));
		updateAngebotImWarenkorbStatement.setString(7, String.valueOf(oldData.get("Angebot_im_Warenkorb.Anbieterbezeichnung")));
		updateAngebotImWarenkorbStatement.setInt(8, Integer.valueOf(String.valueOf(oldData.get("Angebot_im_Warenkorb.Anzahl"))));
		updateAngebotImWarenkorbStatement.executeUpdate();
		
		UnifiedLoggingHelper.logUpdateDone(this.getClass().getName(), oldData, newData,
				String.valueOf(newData.get("Angebot_im_Warenkorb.Warenkorb_ID")) + "-"
				+ String.valueOf(newData.get("Angebot_im_Warenkorb.Angebots_ID"))
				+ String.valueOf(newData.get("Angebot_im_Warenkorb.Anbieterbezeichnung")) + "-"
				+ String.valueOf(newData.get("Angebot_im_Warenkorb.Anzahl")));
	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		UnifiedLoggingHelper.logDelete(this.getClass().getName(), data);
		
		PreparedStatement deleteAngebotImWarenkorbStatement = Project.getInstance().getConnection().prepareStatement(
				"DELETE FROM Angebot_im_Warenkorb "
				+ "WHERE Warenkorb_ID = ? AND Angebots_ID = ? AND Anbieterbezeichnung = ? AND Anzahl = ?");
		deleteAngebotImWarenkorbStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Warenkorb_ID"))));
		deleteAngebotImWarenkorbStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Angebots_ID"))));
		deleteAngebotImWarenkorbStatement.setString(3, String.valueOf(data.get("Angebot_im_Warenkorb.Anbieterbezeichnung")));
		deleteAngebotImWarenkorbStatement.setInt(4, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Anzahl"))));
		deleteAngebotImWarenkorbStatement.executeUpdate();
		
		UnifiedLoggingHelper.logDeleteDone(this.getClass().getName(), data,
				String.valueOf(data.get("Angebot_im_Warenkorb.Warenkorb_ID")) + "-"
				+ String.valueOf(data.get("Angebot_im_Warenkorb.Angebots_ID"))
				+ String.valueOf(data.get("Angebot_im_Warenkorb.Anbieterbezeichnung")) + "-"
				+ String.valueOf(data.get("Angebot_im_Warenkorb.Anzahl")));
	}

}
