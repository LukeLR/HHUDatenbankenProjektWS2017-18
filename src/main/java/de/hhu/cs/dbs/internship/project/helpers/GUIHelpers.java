package de.hhu.cs.dbs.internship.project.helpers;

import java.io.IOException;
import java.util.ArrayList;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;

import de.hhu.cs.dbs.internship.project.table.account.Account;
import javafx.scene.control.TreeItem;

@SuppressWarnings("restriction")
public class GUIHelpers {
	public static void addTableOfClassToTree(Table table, String title, ArrayList<TreeItem<TableViewController>> treeItems) {
		treeItems.add(createTreeItemForTableOfClass(table, title));
	}
	
	public static TreeItem<TableViewController> createTreeItemForTableOfClass(Table table, String title) {
		table.setTitle(title);
		TableViewController accountTableViewController;
		try {
			accountTableViewController = TableViewController.createWithNameAndTable(title, table);
			accountTableViewController.setTitle(title);
		} catch (IOException e) {
			accountTableViewController = null;
		}
		TreeItem<TableViewController> accountTreeItem = new TreeItem<>(accountTableViewController);
		//TODO: accountTreeItem.setExpanded(true);
		return accountTreeItem;
	}
	
	public static void addTableOfClassToTreeItem(Table table, String title, TreeItem<TableViewController> parentItem) {
		parentItem.getChildren().add(createTreeItemForTableOfClass(table, title));
	}
}
