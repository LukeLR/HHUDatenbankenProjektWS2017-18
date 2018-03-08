package de.hhu.cs.dbs.internship.project.table.warenkorb;

import java.sql.SQLException;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;

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
				+ "JOIN Artikel on Angebot_im_Warenkorb.Artikel_ID = Artikel.Artikel_ID "
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRowWithData(Data data) throws SQLException {
		// TODO Auto-generated method stub

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
