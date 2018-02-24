package de.hhu.cs.dbs.internship.project.helpers;

import java.io.IOException;
import java.util.ArrayList;

import com.alexanderthelen.applicationkit.gui.TableViewController;

import de.hhu.cs.dbs.internship.project.table.account.Account;
import javafx.scene.control.TreeItem;

@SuppressWarnings("restriction")
public class GUIHelpers {
	public static void addTableOfClassToTree(Class<?> tableclass, String title, ArrayList<TreeItem<TableViewController>> treeItems) {
		treeItems.add(createTreeItemForTableOfClass(tableclass, title));
	}
	
	public static TreeItem<TableViewController> createTreeItemForTableOfClass(Class<?> tableclass, String title) {
		Account accountTable = new Account();
		accountTable.setTitle("Account");
		TableViewController accountTableViewController;
		try {
			accountTableViewController = TableViewController.createWithNameAndTable("account", accountTable);
			accountTableViewController.setTitle("Account");
		} catch (IOException e) {
			accountTableViewController = null;
		}
		TreeItem<TableViewController> accountTreeItem = new TreeItem<>(accountTableViewController);
		//accountTreeItem.setExpanded(true);
		return accountTreeItem;
	}
	
	public static void addTableOfClassToTreeItem(Class<?> tableclass, String title, TreeItem<TableViewController> parentItem) {
		parentItem.getChildren().add(createTreeItemForTableOfClass(tableclass, title));
	}
}
