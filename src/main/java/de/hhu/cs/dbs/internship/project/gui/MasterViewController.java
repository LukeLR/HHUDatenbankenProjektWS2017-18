package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;
import com.alexanderthelen.applicationkit.gui.ViewController;

import de.hhu.cs.dbs.internship.project.Project;
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
        TreeItem<ViewController> accountTreeItem;
        //TreeItem<ViewController> subTreeItem;
        TableViewController accountTableViewController;
        Table accountTable;

        accountTable = new Account();
        accountTable.setTitle("Account");
        try {
            accountTableViewController = TableViewController.createWithNameAndTable("account", accountTable);
            accountTableViewController.setTitle("Account");
        } catch (IOException e) {
            accountTableViewController = null;
        }
        accountTreeItem = new TreeItem<>(accountTableViewController);
        accountTreeItem.setExpanded(true);
        treeItems.add(accountTreeItem);

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
        
        Table allAccountsTable;
		if (permissionLevel >= 4) {
			 allAccountsTable = new AlleAccounts();
		     allAccountsTable.setTitle("Alle Accounts");
		}        
        
        return treeItems;
    }
}
