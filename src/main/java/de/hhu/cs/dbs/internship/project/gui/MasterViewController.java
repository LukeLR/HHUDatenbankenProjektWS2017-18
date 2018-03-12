package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.gui.TableViewController;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.GUIHelpers;
import de.hhu.cs.dbs.internship.project.table.account.MeinAccount;
import de.hhu.cs.dbs.internship.project.table.account.AlleAdressen;
import de.hhu.cs.dbs.internship.project.table.account.AlleAccounts;
import de.hhu.cs.dbs.internship.project.table.account.AlleAngestellte;
import de.hhu.cs.dbs.internship.project.table.account.AllePremiumkunden;
import de.hhu.cs.dbs.internship.project.table.artikel.AlleAnbieter;
import de.hhu.cs.dbs.internship.project.table.artikel.AlleAnbieterBietetAn;
import de.hhu.cs.dbs.internship.project.table.artikel.AlleAngebote;
import de.hhu.cs.dbs.internship.project.table.artikel.AlleArtikel;
import de.hhu.cs.dbs.internship.project.table.artikel.AlleArtikelempfiehltArtikel;
import de.hhu.cs.dbs.internship.project.table.lieferdienst.AlleLieferdienste;
import de.hhu.cs.dbs.internship.project.table.newsletter.AlleArtikelImNewsletter;
import de.hhu.cs.dbs.internship.project.table.newsletter.AlleNewsletter;
import de.hhu.cs.dbs.internship.project.table.newsletter.MeineNewsletterabos;
import de.hhu.cs.dbs.internship.project.table.newsletter.AlleNewsletterabos;
import de.hhu.cs.dbs.internship.project.table.schlagwort.AlleArtikelGehoertZuSchlagwort;
import de.hhu.cs.dbs.internship.project.table.schlagwort.AlleSchlagworte;
import de.hhu.cs.dbs.internship.project.table.warenkorb.AlleWarenkoerbe;
import de.hhu.cs.dbs.internship.project.table.warenkorb.AlleAngeboteImWarenkorb;
import de.hhu.cs.dbs.internship.project.table.warenkorb.AlleLieferabos;
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
			
			TreeItem<TableViewController> artikel = GUIHelpers.addTableOfClassToTree(new AlleArtikel(), "Artikel", treeItems);
			GUIHelpers.addTableOfClassToTreeItem(new AlleAnbieter(), "Anbieter", artikel);
			GUIHelpers.addTableOfClassToTreeItem(new AlleAngebote(), "Angebote", artikel);
			GUIHelpers.addTableOfClassToTreeItem(new AlleAnbieterBietetAn(), "Anbieter bietet an", artikel);
			GUIHelpers.addTableOfClassToTreeItem(new AlleArtikelempfiehltArtikel(), "Artikelempfehlungen", artikel);
			
			GUIHelpers.addTableOfClassToTree(new AlleLieferdienste(), "Lieferdienste", treeItems);
			
			TreeItem<TableViewController> newsletter = GUIHelpers.addTableOfClassToTree(new AlleNewsletter(), "Newsletter", treeItems);
			GUIHelpers.addTableOfClassToTreeItem(new MeineNewsletterabos(), "Meine Newsletterabos", newsletter);
			GUIHelpers.addTableOfClassToTreeItem(new AlleArtikelImNewsletter(), "Artikel im Newsletter", newsletter);
			
			TreeItem<TableViewController> warenkorb = GUIHelpers.addTableOfClassToTree(new MeineWarenkoerbe(), "Meine Warenkörbe", treeItems);
			
			if (permissionLevel >= Permission.CUSTOMER) {
				logger.info("User is at least of permission level customer.");
				
				if (permissionLevel >= Permission.PREMIUM_CUSTOMER) {
					logger.info("User is at least of permission level premium customer.");
					
					GUIHelpers.addTableOfClassToTreeItem(new AlleLieferabos(), "Lieferabos", warenkorb);
					
					if (permissionLevel >= Permission.SHOP_ASSISTANT) {
						logger.info("User is at least of permission level shop assistant.");
						GUIHelpers.addTableOfClassToTreeItem(new AlleAccounts(), "Alle Accounts", accounts);
						GUIHelpers.addTableOfClassToTreeItem(new AlleAdressen(), "Adressen", accounts);
						GUIHelpers.addTableOfClassToTreeItem(new AllePremiumkunden(), "Premiumkunden", accounts);
						GUIHelpers.addTableOfClassToTreeItem(new AlleAngestellte(), "Angestellte", accounts);
						
						GUIHelpers.addTableOfClassToTreeItem(new AlleNewsletterabos(), "Alle Newsletterabos", newsletter);
						
						GUIHelpers.addTableOfClassToTreeItem(new AlleAngeboteImWarenkorb(), "Alle Angebote im Warenkorb", warenkorb);
						
						TreeItem<TableViewController> schlagwort = GUIHelpers.addTableOfClassToTree(new AlleSchlagworte(), "Schlagworte", treeItems);
						GUIHelpers.addTableOfClassToTreeItem(new AlleArtikelGehoertZuSchlagwort(), "Artikel hat Schlagwort", schlagwort);
						
						GUIHelpers.addTableOfClassToTreeItem(new AlleWarenkoerbe(), "Alle Warenkörbe", warenkorb);
					}
				}
			}
		}
		
		return treeItems;
	}
}
