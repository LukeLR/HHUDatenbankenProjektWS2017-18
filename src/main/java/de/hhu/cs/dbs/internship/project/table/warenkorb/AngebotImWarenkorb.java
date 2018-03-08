package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.UnifiedLoggingHelper;

public class AngebotImWarenkorb extends Table {

	@Override
	public String getSelectQueryForTableWithFilter(String filter) throws SQLException {
		UnifiedLoggingHelper.logShow(this.getClass().getName());
		
		String selectQuery = "SELECT Angebot_im_Warenkorb.Warenkorb_ID, "
				+ "Warenkorb.Bestelldatum, "
				+ "Warenkorb.Bestellstatus, "
				+ "Warenkorb.E_Mail_Adresse, "
				+ "Warenkorb.Lieferdienst_Bezeichnung, "
				+ "Warenkorb.Lieferdatum, "
				+ "Angebot_im_Warenkorb.Angebots_ID, "
				+ "Angebot.Artikel_ID, "
				+ "Angebot.Preis, "
				+ "Artikel.Bezeichnung, "
				+ "Artikel.Beschreibung, "
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
		insertAngebotImWarenkorbStatement.setInt(1, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Warenkorb_ID"))));
		insertAngebotImWarenkorbStatement.setInt(2, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Angebots_ID"))));
		insertAngebotImWarenkorbStatement.setInt(3, Integer.valueOf(String.valueOf(data.get("Angebot_im_Warenkorb.Anbieterbezeichnung"))));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

	}

}
