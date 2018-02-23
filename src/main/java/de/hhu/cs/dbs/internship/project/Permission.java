package de.hhu.cs.dbs.internship.project;

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
}
