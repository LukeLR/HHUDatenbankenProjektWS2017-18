package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;
import com.alexanderthelen.applicationkit.gui.ViewController;

import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.helpers.GUIHelpers;
import de.hhu.cs.dbs.internship.project.table.account.Account;
import de.hhu.cs.dbs.internship.project.table.account.AlleAccounts;
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
	protected ArrayList<TreeItem<ViewController>> getTreeItems() {
		Logger logger = Logger.getLogger(this.getClass().getName());
		int permissionLevel = 0;

		try {
			permissionLevel = Integer.valueOf(Project.getInstance().getData().get("permission").toString());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "User permission level not set!", ex);
		}
		
		ArrayList<TreeItem<ViewController>> treeItems = new ArrayList<>();
		GUIHelpers.addTableOfClassToTree(de.hhu.cs.dbs.internship.project.table.account.Account.class, "Account", treeItems);
		
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

		Table alleAccountsTable;
		if (permissionLevel >= 4) {
			alleAccountsTable = new AlleAccounts();
			alleAccountsTable.setTitle("Alle Accounts");
		}

		return treeItems;
	}
}
