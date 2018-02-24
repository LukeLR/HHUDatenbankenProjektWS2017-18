package de.hhu.cs.dbs.internship.project.helpers;

import java.io.IOException;
import java.util.ArrayList;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;

import de.hhu.cs.dbs.internship.project.table.account.Account;
import javafx.scene.control.TreeItem;

@SuppressWarnings("restriction")
public class GUIHelpers {
	public static TreeItem<TableViewController> addTableOfClassToTree(Table table, String title, ArrayList<TreeItem<TableViewController>> treeItems) {
		TreeItem<TableViewController> treeItem = createTreeItemForTableOfClass(table, title);
		treeItems.add(treeItem);
		return treeItem;
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
	
	public static TreeItem<TableViewController> addTableOfClassToTreeItem(Table table, String title, TreeItem<TableViewController> parentItem) {
		TreeItem<TableViewController> treeItem = createTreeItemForTableOfClass(table, title);
		parentItem.getChildren().add(treeItem);
		return treeItem;
	}
}
