package de.hhu.cs.dbs.internship.project.helpers;

import java.io.IOException;
import java.util.ArrayList;

import com.alexanderthelen.applicationkit.gui.TableViewController;
import com.alexanderthelen.applicationkit.gui.ViewController;

import de.hhu.cs.dbs.internship.project.table.account.Account;
import javafx.scene.control.TreeItem;

@SuppressWarnings("restriction")
public class GUIHelpers {
	@SuppressWarnings("unchecked")
	public static void addTableOfClassToTree(@SuppressWarnings("rawtypes") Class tableclass, String title, ArrayList<TreeItem<ViewController>> treeItems) {
		Account accountTable = new Account();
		accountTable.setTitle("Account");
		TableViewController accountTableViewController;
		try {
			accountTableViewController = TableViewController.createWithNameAndTable("account", accountTable);
			accountTableViewController.setTitle("Account");
		} catch (IOException e) {
			accountTableViewController = null;
		}
		@SuppressWarnings("rawtypes")
		TreeItem accountTreeItem = new TreeItem<>(accountTableViewController);
		accountTreeItem.setExpanded(true);
		treeItems.add(accountTreeItem);
	}
	
	public static void createTableViewControllerForTableOfClass(Class tableclass, String title) {
		
	}
}
