package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.gui.TableViewController;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.GUIHelpers;
import de.hhu.cs.dbs.internship.project.table.account.MeinAccount;
import de.hhu.cs.dbs.internship.project.table.account.Adressen;
import de.hhu.cs.dbs.internship.project.table.account.AlleAccounts;
import de.hhu.cs.dbs.internship.project.table.account.Angestellter;
import de.hhu.cs.dbs.internship.project.table.account.Premiumkunde;
import de.hhu.cs.dbs.internship.project.table.artikel.Anbieter;
import de.hhu.cs.dbs.internship.project.table.artikel.AnbieterBietetAn;
import de.hhu.cs.dbs.internship.project.table.artikel.Angebot;
import de.hhu.cs.dbs.internship.project.table.artikel.Artikel;
import de.hhu.cs.dbs.internship.project.table.artikel.ArtikelempfiehltArtikel;
import de.hhu.cs.dbs.internship.project.table.lieferdienst.Lieferdienst;
import de.hhu.cs.dbs.internship.project.table.newsletter.ArtikelImNewsletter;
import de.hhu.cs.dbs.internship.project.table.newsletter.Newsletter;
import de.hhu.cs.dbs.internship.project.table.newsletter.MeineNewsletterabos;
import de.hhu.cs.dbs.internship.project.table.newsletter.AlleNewsletterabos;
import de.hhu.cs.dbs.internship.project.table.schlagwort.ArtikelGehoertZuSchlagwort;
import de.hhu.cs.dbs.internship.project.table.schlagwort.Schlagwort;
import de.hhu.cs.dbs.internship.project.table.warenkorb.AlleWarenkoerbe;
import de.hhu.cs.dbs.internship.project.table.warenkorb.AlleAngeboteImWarenkorb;
import de.hhu.cs.dbs.internship.project.table.warenkorb.Lieferabo;
import de.hhu.cs.dbs.internship.project.table.warenkorb.MeineWarenkoerbe;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("restriction")
public class MasterViewController extends com.alexanderthelen.applicationkit.gui.MasterViewController {
	protected MasterViewController(String name) {
		super(name);
	}

	public static MasterViewController createWithName(String name) throws IOException {
		MasterViewController controller = new MasterViewController(name);
		controller.loadView();
		return controller;
	}

	@Override
	protected ArrayList<TreeItem<TableViewController>> getTreeItems() {
		Logger logger = Logger.getLogger(this.getClass().getName());
		int permissionLevel = 0;
		ArrayList<TreeItem<TableViewController>> treeItems = new ArrayList<>();

		try {
			permissionLevel = Integer.valueOf(Project.getInstance().getData().get("permission").toString());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "User permission level not set!", ex);
		}
		
		if (permissionLevel >= Permission.READ_ONLY) {
			logger.info("User is at least of permission level read only.");
			TreeItem<TableViewController> accounts = GUIHelpers.addTableOfClassToTree(new MeinAccount(), "Mein Account", treeItems);
			
			if (permissionLevel >= Permission.CUSTOMER) {
				logger.info("User is at least of permission level customer.");
				TreeItem<TableViewController> artikel = GUIHelpers.addTableOfClassToTree(new Artikel(), "Artikel", treeItems);
				GUIHelpers.addTableOfClassToTreeItem(new Anbieter(), "Anbieter", artikel);
				GUIHelpers.addTableOfClassToTreeItem(new Angebot(), "Angebote", artikel);
				GUIHelpers.addTableOfClassToTreeItem(new AnbieterBietetAn(), "Anbieter bietet an", artikel);
				GUIHelpers.addTableOfClassToTreeItem(new ArtikelempfiehltArtikel(), "Artikelempfehlungen", artikel);
				
				GUIHelpers.addTableOfClassToTree(new Lieferdienst(), "Lieferdienste", treeItems);
				
				TreeItem<TableViewController> newsletter = GUIHelpers.addTableOfClassToTree(new Newsletter(), "Newsletter", treeItems);
				GUIHelpers.addTableOfClassToTreeItem(new MeineNewsletterabos(), "Meine Newsletterabos", newsletter);
				GUIHelpers.addTableOfClassToTreeItem(new ArtikelImNewsletter(), "Artikel im Newsletter", newsletter);
				
				TreeItem<TableViewController> warenkorb = GUIHelpers.addTableOfClassToTree(new MeineWarenkoerbe(), "Meine Warenkörbe", treeItems);
				
				if (permissionLevel >= Permission.PREMIUM_CUSTOMER) {
					logger.info("User is at least of permission level premium customer.");
					
					GUIHelpers.addTableOfClassToTreeItem(new Lieferabo(), "Lieferabos", warenkorb);
					
					if (permissionLevel >= Permission.SHOP_ASSISTANT) {
						logger.info("User is at least of permission level shop assistant.");
						GUIHelpers.addTableOfClassToTreeItem(new AlleAccounts(), "Alle Accounts", accounts);
						GUIHelpers.addTableOfClassToTreeItem(new Adressen(), "Adressen", accounts);
						GUIHelpers.addTableOfClassToTreeItem(new Premiumkunde(), "Premiumkunden", accounts);
						GUIHelpers.addTableOfClassToTreeItem(new Angestellter(), "Angestellte", accounts);
						
						GUIHelpers.addTableOfClassToTreeItem(new AlleNewsletterabos(), "Alle Newsletterabos", newsletter);
						
						GUIHelpers.addTableOfClassToTreeItem(new AlleAngeboteImWarenkorb(), "Alle Angebote im Warenkorb", warenkorb);
						
						TreeItem<TableViewController> schlagwort = GUIHelpers.addTableOfClassToTree(new Schlagwort(), "Schlagworte", treeItems);
						GUIHelpers.addTableOfClassToTreeItem(new ArtikelGehoertZuSchlagwort(), "Artikel hat Schlagwort", schlagwort);
						
						GUIHelpers.addTableOfClassToTreeItem(new AlleWarenkoerbe(), "Alle Warenkörbe", warenkorb);
					}
				}
			}
		}
		
		return treeItems;
	}
}
