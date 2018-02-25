package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.gui.TableViewController;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.GUIHelpers;
import de.hhu.cs.dbs.internship.project.table.account.Account;
import de.hhu.cs.dbs.internship.project.table.account.AlleAccounts;
import de.hhu.cs.dbs.internship.project.table.account.Angestellter;
import de.hhu.cs.dbs.internship.project.table.account.Premiumkunde;
import de.hhu.cs.dbs.internship.project.table.angebot.Anbieter;
import de.hhu.cs.dbs.internship.project.table.artikel.Artikel;
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

		try {
			permissionLevel = Integer.valueOf(Project.getInstance().getData().get("permission").toString());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "User permission level not set!", ex);
		}
		
		ArrayList<TreeItem<TableViewController>> treeItems = new ArrayList<>();
		GUIHelpers.addTableOfClassToTree(new Account(), "Account", treeItems);
		
		/*table = new Favorites();
		table.setTitle("Favoriten");
		try {
			tableViewController = TableViewController.createWithNameAndTable("favorites", table);
			tableViewController.setTitle("Favoriten");
		} catch (IOException e) {
			tableViewController = null;
		}
		subTreeItem = new TreeItem<>(tableViewController);
		treeItem.getChildren().add(subTreeItem);*/

		if (permissionLevel >= Permission.SHOP_ASSISTANT) {
			logger.info("User is at least of permission level shop assistant.");
			TreeItem<TableViewController> alleAccounts = GUIHelpers.addTableOfClassToTree(new AlleAccounts(), "Alle Accounts", treeItems);
			GUIHelpers.addTableOfClassToTreeItem(new Premiumkunde(), "Premiumkunden", alleAccounts);
			GUIHelpers.addTableOfClassToTreeItem(new Angestellter(), "Angestellte", alleAccounts);
			
			TreeItem<TableViewController> artikel = GUIHelpers.addTableOfClassToTree(new Artikel(), "Artikel", treeItems);
			GUIHelpers.addTableOfClassToTreeItem(new Anbieter(), "Anbieter", artikel);
			GUIHelpers.addTableOfClassToTreeItem(new Angebot(), "Angebote", artikel);
			GUIHelpers.addTableOfClassToTreeItem(new ArtikelempfiehltArtikel(), "Artikelempfehlungen", artikel);
		}
		
		if (permissionLevel >= Permission.CUSTOMER) {
			logger.info("User is at least of permission level customer.");
			
		}

		return treeItems;
	}
}
