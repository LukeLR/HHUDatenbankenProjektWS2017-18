package de.hhu.cs.dbs.internship.project;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hhu.cs.dbs.internship.project.helpers.MiscHelpers;

public class Permission {
	public static final int UNAUTHORIZED = 0;
	public static final int READ_ONLY = 1;
	public static final int CUSTOMER = 2;
	public static final int PREMIUM_CUSTOMER = 3;
	public static final int SHOP_ASSISTANT = 4;

	public static String permissionLevelToString (int permissionLevel) {
		switch (permissionLevel) {
		case UNAUTHORIZED: return "Unauthorized";
		case READ_ONLY: return "Read-only";
		case CUSTOMER: return "Customer";
		case PREMIUM_CUSTOMER: return "Premium customer";
		case SHOP_ASSISTANT: return "Shop assistant";
		default: return "Undefined.";
		}
	}

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
