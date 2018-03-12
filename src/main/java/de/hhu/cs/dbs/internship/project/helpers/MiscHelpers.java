package de.hhu.cs.dbs.internship.project.helpers;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hhu.cs.dbs.internship.project.Permission;
import de.hhu.cs.dbs.internship.project.Project;

public class MiscHelpers {
	public static boolean hasSufficientPermission (int requiredPermissionLevel, String parentClass) throws SQLException {
		Logger logger = Logger.getLogger(MiscHelpers.class.getName());
		int currentPermissionLevel = Integer.valueOf(String.valueOf(Project.getInstance().getData().get("permission")));
		if (currentPermissionLevel >= requiredPermissionLevel) {
			logger.info("Permissions ok for " + parentClass + ". Required: " + 
					Permission.permissionLevelToString(requiredPermissionLevel) + " (" +
					String.valueOf(requiredPermissionLevel) + "), user has: " + 
					Permission.permissionLevelToString(currentPermissionLevel) + " (" + 
					String.valueOf(currentPermissionLevel) + ").");
			return true;
		} else {
			SQLException ex = new SQLException("Insufficient permissions! Required: " +
					Permission.permissionLevelToString(requiredPermissionLevel) + " (" +
					String.valueOf(requiredPermissionLevel) + "), user has: " + 
					Permission.permissionLevelToString(currentPermissionLevel) + " (" + 
					String.valueOf(currentPermissionLevel) + ").");
			logger.log(Level.WARNING, "Insufficient permissions!", ex);
			throw ex;
		}
	}
}
